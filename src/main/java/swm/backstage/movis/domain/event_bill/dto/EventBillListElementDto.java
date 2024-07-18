package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_bill.EventBill;

@Getter
@NoArgsConstructor
public class EventBillListElementDto {
    private String uuid;
    private String name;
    private Long amount;

    public EventBillListElementDto(EventBill eventBill) {
        this.uuid = eventBill.getUuid();
        this.name = eventBill.getPayName();
        this.amount = eventBill.getAmount();
    }
}
