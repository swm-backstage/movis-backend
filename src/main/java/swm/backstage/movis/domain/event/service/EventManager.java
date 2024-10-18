package swm.backstage.movis.domain.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateReqDto;
import swm.backstage.movis.domain.event_bill.service.EventBillManager;
import swm.backstage.movis.domain.event_member.dto.EventMemberListReqDto;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.service.FeeManager;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventManager {
    private final EventService eventService;
    private final EventMemberService eventMemberService;
    private final AccountBookService accountBookService;
    private final FeeManager feeManager;
    private final EventBillManager eventBillManager;
    private final TransactionHistoryManager transactionHistoryManager;

    public Event createEvent(EventCreateReqDto eventCreateReqDto) {
        // 1. event를 생성한다. (클럽id, 이벤트 이름, 입금기한, 입금금액)
        Event event = eventService.createEvent(eventCreateReqDto);

        // 2. 생성된 event로 eventMemberList가 있다면 eventMemberList도 등록한다.
        eventMemberService.addEventMembers(new EventMemberListReqDto(event.getUlid(),eventCreateReqDto.getEventMemberIdList()));
        return event;
    }

    @Transactional
    public void deleteEvent(String clubId, String eventId) {
        log.info("deleteEvent Start");
        //1. lock 걸고
        AccountBook accountBook = accountBookService.getAccountBookByClubId(clubId);
        log.info("Catch lock");

        Event event = eventService.getEventByUuid(eventId);
        event.updateIsDeleted(Boolean.TRUE);

        log.info("이벤트 soft delete");

        //2. deposit 관련 수정
        feeManager.updateIsDeleted(event.getUlid());

        log.info("deposit soft delete");

        //3. eventBill 수정
        eventBillManager.updateIsDeleted(event.getUlid());

        log.info("eventBill soft delete");

        //4. transactionHistory 수정
        transactionHistoryManager.updateIsDeleted(event.getUlid());

        log.info("transactionHistory soft delete");
    }
}
