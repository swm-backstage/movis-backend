package swm.backstage.movis.domain.transaction_history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.util.List;

@Getter
@NoArgsConstructor
public class TransactionHistoryGetPagingListResDto {
    List<TransactionHistoryElementDto> feeElements;
    private Boolean isLast;

    public TransactionHistoryGetPagingListResDto(List<TransactionHistory> feeList, Boolean isLast) {
        this.isLast = isLast;
        this.feeElements = feeList.stream()
                .map(TransactionHistoryElementDto::new)
                .toList();
    }
}
