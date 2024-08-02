package swm.backstage.movis.domain.transaction_history.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.util.List;

@Getter
@NoArgsConstructor
public class TransactionHistoryListDto {
    List<TransactionHistoryGetElementResDto> transactionHistoryDtoList;

    public TransactionHistoryListDto(List<TransactionHistory> transactionHistoryListDto) {
        this.transactionHistoryDtoList = transactionHistoryListDto.stream()
                .map(TransactionHistoryGetElementResDto::new).toList();
    }
}
