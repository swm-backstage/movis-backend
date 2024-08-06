package swm.backstage.movis.domain.transaction_history.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionHistoryGetPagingListResDto {
    List<TransactionHistoryGetElementResDto> feeElements;
    private Boolean isLast;
    private Long totalBalance;
    private Long totalDeposit;
    private Long totalWithdraw;

    public TransactionHistoryGetPagingListResDto(List<TransactionHistory> feeList, Boolean isLast, AccountBook accountBook) {
        this.isLast = isLast;
        this.feeElements = feeList.stream()
                .map(TransactionHistoryGetElementResDto::new)
                .toList();
        if(accountBook!=null) {
            this.totalBalance = accountBook.getBalance();
            this.totalDeposit = accountBook.getTotalDeposit();
            this.totalWithdraw = accountBook.getTotalWithdrawal();
        }
    }
}
