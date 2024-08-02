package swm.backstage.movis.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

import java.util.List;


@Getter
@NoArgsConstructor
public class EventGetPagingListResDto {
    private List<EventGetPagingListElementDto> eventList;
    private Boolean isLast;

    public EventGetPagingListResDto(List<Event> eventList, Boolean isLast) {
        this.isLast = isLast;
        this.eventList = eventList.stream().map(EventGetPagingListElementDto::new).toList();
    }
}

