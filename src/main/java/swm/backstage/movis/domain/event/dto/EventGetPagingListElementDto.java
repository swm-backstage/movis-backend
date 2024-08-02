package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

@Getter
@NoArgsConstructor
public class EventGetPagingListElementDto {
    private String eventId;

    private String name;

    private Long balance;

    public EventGetPagingListElementDto(Event event) {
        this.eventId = event.getUuid();
        this.name = event.getName();
        this.balance = event.getBalance();
    }
}
