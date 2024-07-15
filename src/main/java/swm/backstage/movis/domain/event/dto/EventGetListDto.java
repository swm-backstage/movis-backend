package swm.backstage.movis.domain.event.dto;

import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.member.dto.MemberGetDto;

import java.util.List;

public class EventGetListDto {
    private List<EventGetDto> eventList;

    public EventGetListDto(List<Event> eventList) {
        this.eventList = eventList.stream().map(EventGetDto::new).toList();
    }
}

