package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_bill.EventBill;

@Getter
@NoArgsConstructor
public class EventBillGetElementResDto {
    private String eventId;
    private String name;
    private Long amount;

    public EventBillGetElementResDto(EventBill eventBill) {
        this.eventId = eventBill.getUuid();
        this.name = eventBill.getPayName();
        this.amount = eventBill.getAmount();
    }
}
