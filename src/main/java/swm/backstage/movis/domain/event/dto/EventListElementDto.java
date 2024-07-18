package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

@Getter
@NoArgsConstructor
public class EventListElementDto {
    private String uuid;

    private String name;

    private Long balance;

    public EventListElementDto(Event event) {
        this.uuid = event.getUuid();
        this.name = event.getName();
        this.balance = event.getBalance();
    }
}
