package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;


import java.util.List;

@Getter
@NoArgsConstructor
public class EventGetFundingListDto {
    List<EventGetFundingDto> eventFundingListDto;

    public EventGetFundingListDto(List<Event> eventList) {
        this.eventFundingListDto = eventList.stream()
                .map(EventGetFundingDto::new).toList();
    }
}
