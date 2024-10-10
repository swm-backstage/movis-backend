package swm.backstage.movis.domain.transaction_history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionHistoryGetElementResDto {
    private String transactionHistoryId;
    private String status;
    private String elementId;
    private String name;
    private Long amount;
    private LocalDateTime paidAt;

    public TransactionHistoryGetElementResDto(TransactionHistory transactionHistory) {
        this.transactionHistoryId = transactionHistory.getUlid();
        this.elementId = transactionHistory.getElementUlid();
        this.name = transactionHistory.getName();
        this.amount =transactionHistory.getAmount();
        this.paidAt = transactionHistory.getPaidAt();
        this.status = transactionHistory.getStatus().toString();
    }
}
