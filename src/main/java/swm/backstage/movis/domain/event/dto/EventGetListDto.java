package swm.backstage.movis.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.member.dto.MemberGetDto;

import java.util.List;


@Getter
@NoArgsConstructor
public class EventGetListDto {
    private List<EventListElementDto> eventList;
    private Boolean isLast;

    public EventGetListDto(List<Event> eventList, Boolean isLast) {
        this.isLast = isLast;
        this.eventList = eventList.stream().map(EventListElementDto::new).toList();
    }
}

