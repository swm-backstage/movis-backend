package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;


import java.util.List;

@Getter
@NoArgsConstructor
public class EventGetFundingListResDto {
    List<EventGetFundingElementResDto> eventFundingListDto;

    public EventGetFundingListResDto(List<Event> eventList) {
        this.eventFundingListDto = eventList.stream()
                .map(EventGetFundingElementResDto::new).toList();
    }
}
