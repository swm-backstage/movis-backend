package swm.backstage.movis.domain.event.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateDto;
import swm.backstage.movis.domain.event.repository.EventRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ClubService clubService;

    public void createEvent(EventCreateDto eventCreateDto) {
        Club club = clubService.getClubByUuId(eventCreateDto.getClubId());
        eventRepository.save(new Event(UUID.randomUUID().toString(),eventCreateDto,club,club.getAccountBook()));
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


    public List<Event> getEventList(String clubId) {
        return eventRepository.findAllByClub(clubService.getClubByUuId(clubId));
    }
}
