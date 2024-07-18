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
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetPagingListDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateDto;
import swm.backstage.movis.domain.event_bill.repository.EventBillRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventBillService {
    private final EventBillRepository eventBillRepository;
    private final ClubService clubService;
    private final AccountBookService accountBookService;
    private final EventService eventService;

    @Transactional
    public void createEventBill(EventBillCreateDto eventBillCreateDto){
        eventBillRepository.save(new EventBill(UUID.randomUUID().toString(),eventBillCreateDto,clubService.getClubByUuId(eventBillCreateDto.getClubId())));
    }

    public EventBill getEventBillByUuid(String eventBillId){
        return eventBillRepository.findByUuid(eventBillId).orElseThrow(()-> new BaseException("eventBill is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void updateUnClassifiedEventBill(String eventBillId, EventBillUpdateDto eventBillUpdateDto){
        AccountBook accountBook = accountBookService.getAccountBookByClubId(eventBillUpdateDto.getClubId());
        EventBill eventBill = getEventBillByUuid(eventBillId);
        Event event = eventService.getEventByUuid(eventBillUpdateDto.getEventId());
        accountBook.updateBalance(eventBill.getAmount());
        event.updateBalance(eventBill.getAmount());
        eventBill.setEvent(event);
    }

    public EventBillGetPagingListDto getEventBIllPagingList(String eventId, String lastId, int size){
        Event event = eventService.getEventByUuid(eventId);
        List<EventBill> eventBillList;
        if(lastId.equals("first")){
            eventBillList = eventBillRepository.getFirstPage(event.getId(),size+1);
        }
        else{
            EventBill eventBill = getEventBillByUuid(lastId);
            eventBillList = eventBillRepository.getNextPageByEventIdAndLastId(event.getId(),eventBill.getId(),size+1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = eventBillList.size() < size + 1;
        if(!isLast){
            eventBillList.remove(eventBillList.size()-1);
        }
        return new EventBillGetPagingListDto(eventBillList, isLast);

    }
}
