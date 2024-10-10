package swm.backstage.movis.domain.event.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventGetDto {

    private String eventId;

    private String name;

    private Long balance;

    private Long totalPaymentAmount;

    private LocalDate paymentDeadline;

    public EventGetDto(Event event) {
        this.eventId = event.getUlid();
        this.name = event.getName();
        this.balance = event.getBalance();
        this.totalPaymentAmount = event.getTotalPaymentAmount();
        this.paymentDeadline = event.getPaymentDeadline();
    }
}
