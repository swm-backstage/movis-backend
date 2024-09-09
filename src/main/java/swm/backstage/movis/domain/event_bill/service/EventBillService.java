package swm.backstage.movis.domain.event_bill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_bill.dto.*;
import swm.backstage.movis.domain.event_bill.repository.EventBillRepository;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryCreateDto;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryUpdateDto;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventBillService {
    private final EventBillRepository eventBillRepository;
    private final ClubService clubService;
    private final AccountBookService accountBookService;
    private final EventService eventService;
    private final TransactionHistoryService transactionHistoryService;
    /**
     * 지출 내역 추가 (수동 입력)
     */
    @Transactional
    public void createEventBillByInput(String eventId, EventBillInputReqDto dto) {
        Event event = eventService.getEventByUuid(eventId);
        AccountBook accountBook = accountBookService.getAccountBookByClubId(event.getClub().getUuid());
        EventBill eventBill = eventBillRepository.save(new EventBill(UUID.randomUUID().toString(), dto, event.getClub(), event));
        accountBook.updateBalance(dto.getPaidAmount());
        accountBook.updateClassifiedWithdrawal(dto.getPaidAmount());
        transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromEventBill(eventBill));
    }
    /**
     * 지출 내역 추가 (알림)
     */
    @Transactional
    public void createEventBill(EventBillCreateReqDto eventBillCreateReqDto) {
        EventBill eventBill = eventBillRepository.save(new EventBill(UUID.randomUUID().toString(), eventBillCreateReqDto, clubService.getClubByUuId(eventBillCreateReqDto.getClubId())));
        AccountBook accountBook = accountBookService.getAccountBookByClubId(eventBillCreateReqDto.getClubId());
        accountBook.updateUnClassifiedWithdrawal(eventBillCreateReqDto.getAmount());
        accountBook.updateBalance(eventBillCreateReqDto.getAmount());
        transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromEventBill(eventBill));
    }

    public EventBill getEventBillByUuid(String eventBillId) {
        return eventBillRepository.findByUuid(eventBillId).orElseThrow(() -> new BaseException("eventBill is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void updateUnClassifiedEventBill(String eventBillId, EventBillUpdateReqDto eventBillUpdateReqDto) {
        AccountBook accountBook = accountBookService.getAccountBookByClubId(eventBillUpdateReqDto.getClubId());
        EventBill eventBill = getEventBillByUuid(eventBillId);
        Event event = eventService.getEventByUuid(eventBillUpdateReqDto.getEventId());
        accountBook.updateClassifiedWithdrawal(eventBill.getAmount());
        accountBook.updateUnClassifiedWithdrawal(-eventBill.getAmount());
        event.updateBalance(eventBill.getAmount());
        eventBill.setEvent(event);
        transactionHistoryService.updateTransactionHistory(new TransactionHistoryUpdateDto(eventBillId, eventBill.getPayName(), event));
    }

    public EventBillGetPagingListResDto getEventBIllPagingList(String eventId, LocalDateTime lastPaidAt, String lastId, int size) {
        Event event = eventService.getEventByUuid(eventId);
        List<EventBill> eventBillList;
        if (lastId.equals("first")) {
            eventBillList = eventBillRepository.getFirstPage(event.getId(), lastPaidAt, size + 1);
        } else {
            EventBill eventBill = getEventBillByUuid(lastId);
            eventBillList = eventBillRepository.getNextPage(event.getId(), lastPaidAt, eventBill.getId(), size + 1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = eventBillList.size() < size + 1;
        if (!isLast) {
            eventBillList.remove(eventBillList.size() - 1);
        }
        return new EventBillGetPagingListResDto(eventBillList, isLast);

    }

    /**
     * 설명 추가
     */
    @Transactional
    public EventBill createEventBillExplanation(EventBIllCreateExplanationReqDto eventBIllCreateExplanationReqDto) {
        EventBill eventBill = getEventBillByUuid(eventBIllCreateExplanationReqDto.getEventBillId());
        eventBill.setExplanation(eventBIllCreateExplanationReqDto.getExplanation());
        return eventBill;
    }
}
