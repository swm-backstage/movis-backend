package swm.backstage.movis.domain.transaction_history.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.fee.Fee;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionHistoryCreateDto {

    private String elementUuid;
    private String name;
    private Long amount;
    private LocalDateTime paidAt;
    private Event event;
    private TransactionStatus status;

    public TransactionHistoryCreateDto(String elementUuid, String name, Long amount, Event event, LocalDateTime paidAt,TransactionStatus status) {
        this.elementUuid = elementUuid;
        this.name = name;
        this.amount = amount;
        this.event = event;
        this.paidAt = paidAt;
        this.status = status;
    }

    public static TransactionHistoryCreateDto fromEventBill(EventBill eventBill) {
        return new TransactionHistoryCreateDto(
                eventBill.getUuid(),
                eventBill.getPayName(),
                eventBill.getAmount(),
                eventBill.getEvent(),
                eventBill.getPaidAt(),
                TransactionStatus.BILL);
    }
    public static TransactionHistoryCreateDto fromFee(Fee fee) {
        return new TransactionHistoryCreateDto(
                fee.getUuid(),
                fee.getName(),
                fee.getPaidAmount(),
                fee.getEvent(),
                fee.getPaidAt(),
                TransactionStatus.FEE);
    }
}
