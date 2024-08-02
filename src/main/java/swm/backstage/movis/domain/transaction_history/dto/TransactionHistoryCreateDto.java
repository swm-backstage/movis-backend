package swm.backstage.movis.domain.transaction_history.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.fee.Fee;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionHistoryCreateDto {

    private Club club;
    private String elementUuid;
    private String name;
    private Long amount;
    private LocalDateTime paidAt;
    private Event event;
    private TransactionStatus status;
    private Boolean isClassified;

    public TransactionHistoryCreateDto(Club club, Event event, String elementUuid, String name, Long amount, LocalDateTime paidAt, TransactionStatus status, Boolean isClassified) {
        this.club = club;
        this.event = event;
        this.elementUuid = elementUuid;
        this.name = name;
        this.amount = amount;
        this.paidAt = paidAt;
        this.status = status;
        this.isClassified = isClassified;
    }


    public static TransactionHistoryCreateDto fromEventBill(EventBill eventBill) {
        return new TransactionHistoryCreateDto(
                eventBill.getClub(),
                eventBill.getEvent(),
                eventBill.getUuid(),
                eventBill.getPayName(),
                eventBill.getAmount(),
                eventBill.getPaidAt(),
                TransactionStatus.BILL,
                Boolean.FALSE);
    }
    public static TransactionHistoryCreateDto fromFee(Fee fee, Boolean isClassified) {
        return new TransactionHistoryCreateDto(
                fee.getClub(),
                fee.getEvent(),
                fee.getUuid(),
                fee.getName(),
                fee.getPaidAmount(),
                fee.getPaidAt(),
                TransactionStatus.FEE,
                isClassified);
    }
}
