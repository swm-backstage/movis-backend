package swm.backstage.movis.domain.event.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateReqDto;
import swm.backstage.movis.domain.event.dto.EventGatherFeeReqDto;
import swm.backstage.movis.domain.event.dto.EventGetPagingListResDto;
import swm.backstage.movis.domain.event.dto.EventUpdateReqDto;
import swm.backstage.movis.domain.event.repository.EventRepository;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_bill.service.EventBillManager;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.fee.service.FeeManager;
import swm.backstage.movis.domain.fee.service.FeeService;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryManager;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ClubService clubService;
    private final AccountBookService accountBookService;
    private final FeeManager feeManager;
    private final EventBillManager eventBillManager;
    private final TransactionHistoryManager transactionHistoryManager;

    @Transactional
    public Event createEvent(EventCreateReqDto eventCreateReqDto) {
        Club club = clubService.getClubByUuId(eventCreateReqDto.getClubId());
        return eventRepository.save(new Event(UUID.randomUUID().toString(), eventCreateReqDto,club,club.getAccountBook()));
    }

    /**
     * null 허용 X
     * */
    public Event getEventByUuid(String eventId) {
        return eventRepository.findByUuid(eventId).orElseThrow(()->new BaseException("eventId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    /**
     * 금액, 모임 id로 event 추정하기
     * */
    public Event findEventByClubIdAndAmount(String clubId, Long amount) {
        List<Event> events = eventRepository.findByClubIdAndTotalPaymentAmount(clubId,amount);
        if(events.size() != 1){
            return null;
        }
        return events.get(0);
    }

    /**
     * null 허용 O
     * */
    public Event findEventByUuid(String eventUuid) {
        return eventRepository.findByUuid(eventUuid).orElse(null);
    }


    /**
     * 이벤트 리스트 페이징 조회
     * */
    public EventGetPagingListResDto getEventPagingList(String clubId, String lastId, int size) {
        Club club = clubService.getClubByUuId(clubId);
        List<Event> eventList;
        if(lastId.equals("first")){
            eventList = eventRepository.getFirstPage(club.getId(),size+1);
        }
        else{
            Event event = getEventByUuid(lastId);
            eventList = eventRepository.getNextPageByEventIdAndLastId(club.getId(),event.getId(),size+1);
        }
        Boolean isLast = eventList.size() < size + 1;
        if(!isLast){
            eventList.remove(eventList.size()-1);
        }
        return new EventGetPagingListResDto(eventList,isLast);
    }

    /**
     * 현재 회비를 모으고 있는 이벤트 리스트 조회
     * */
    public List<Event> getCollectingMoneyEventList(String clubId, LocalDate now){
        Club club = clubService.getClubByUuId(clubId);
        return eventRepository.getCollectingMoneyEventByClub(club, now);
    }

    @Transactional
    public void enrollGatherFee(String eventId, EventGatherFeeReqDto eventGatherFeeReqDto) {
        Event event = getEventByUuid(eventId);
        event.updateGatherFeeInfo(eventGatherFeeReqDto.getTotalPaymentAmount(),eventGatherFeeReqDto.getPaymentDeadline());
    }

    @Transactional
    public Event updateEvent(String eventId, EventUpdateReqDto eventUpdateReqDto) {
        Event event = getEventByUuid(eventId);
        event.updateEvent(eventUpdateReqDto);
        return event;
    }

    @Transactional
    public void deleteEvent(String clubId, String eventId) {

        //1. lock 걸고
        AccountBook accountBook = accountBookService.getAccountBookByClubId(clubId);
        Event event = getEventByUuid(eventId);
        event.updateIsDeleted(Boolean.TRUE);
        accountBook.updateBalance(-event.getBalance());

        //2. deposit 관련 수정
        long deposit = 0;
        feeManager.updateIsDeleted(event.getId());
        for(Fee fee : event.getFees()){
           deposit = deposit + fee.getPaidAmount();
        }
        accountBook.updateClassifiedDeposit(-deposit);

        //3. eventBill 수정
        eventBillManager.updateIsDeleted(event.getId());
        long withdraw = 0L;
        for(EventBill eventBill : event.getEventBills()){
            withdraw = withdraw + eventBill.getAmount();
        }
        accountBook.updateClassifiedWithdrawal(-withdraw);

        //4. transactionHistory 수정
        transactionHistoryManager.updateIsDeleted(event.getId());
    }


}
