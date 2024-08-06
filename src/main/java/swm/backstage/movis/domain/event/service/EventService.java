package swm.backstage.movis.domain.event.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateReqDto;
import swm.backstage.movis.domain.event.dto.EventGatherFeeReqDto;
import swm.backstage.movis.domain.event.dto.EventGetPagingListResDto;
import swm.backstage.movis.domain.event.repository.EventRepository;
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
}
