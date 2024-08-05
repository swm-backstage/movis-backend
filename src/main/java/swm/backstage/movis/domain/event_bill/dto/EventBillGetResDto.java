package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventBillGetResDto {
    private Long amount;
    private String payName;
    private String image;
    private String explanation;
    private LocalDateTime paidAt;

    public EventBillGetResDto(EventBill eventBill) {
        this.amount = eventBill.getAmount();
        this.payName = eventBill.getPayName();
        this.image = eventBill.getImage();
        this.explanation = eventBill.getExplanation();
        this.paidAt = eventBill.getPaidAt();
    }
}
