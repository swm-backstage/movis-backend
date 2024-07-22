package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_bill.EventBill;

@Getter
@NoArgsConstructor
public class EventBillListElementDto {
    private String id;
    private String name;
    private Long amount;

    public EventBillListElementDto(EventBill eventBill) {
        this.id = eventBill.getUuid();
        this.name = eventBill.getPayName();
        this.amount = eventBill.getAmount();
    }
}
