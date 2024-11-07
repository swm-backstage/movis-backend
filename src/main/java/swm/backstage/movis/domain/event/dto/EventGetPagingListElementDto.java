package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventGetPagingListElementDto {
    private String eventId;

    private String name;

    private Long balance;

    private LocalDateTime createdAt;


    public EventGetPagingListElementDto(Event event) {
        this.eventId = event.getUlid();
        this.name = event.getName();
        this.balance = event.getBalance();
        this.createdAt = event.getCreatedAt();
    }
}
