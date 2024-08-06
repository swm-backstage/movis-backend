package swm.backstage.movis.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateReqDto;
import swm.backstage.movis.domain.event_member.dto.EventMemberListReqDto;
import swm.backstage.movis.domain.event_member.service.EventMemberService;

@Service
@RequiredArgsConstructor
public class EventManager {
    private final EventService eventService;
    private final EventMemberService eventMemberService;

    public void createEvent(EventCreateReqDto eventCreateReqDto) {
        // 1. event를 생성한다. (클럽id, 이벤트 이름, 입금기한, 입금금액)
        Event event = eventService.createEvent(eventCreateReqDto);

        // 2. 생성된 event로 eventMemberList가 있다면 eventMemberList도 등록한다.
        eventMemberService.addEventMembers(new EventMemberListReqDto(event.getUuid(),eventCreateReqDto.getEventMemberIdList()));

    }
}
