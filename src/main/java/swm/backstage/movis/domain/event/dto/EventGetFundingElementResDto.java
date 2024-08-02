package swm.backstage.movis.domain.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventGetFundingElementResDto {
    private String eventId;

    private String name;

    private Long totalPaymentAmount;

    private LocalDate paymentDeadline;

    public EventGetFundingElementResDto(Event event) {
        this.eventId = event.getUuid();
        this.name = event.getName();
        this.totalPaymentAmount = event.getTotalPaymentAmount();
        this.paymentDeadline = event.getPaymentDeadline();
    }
}
