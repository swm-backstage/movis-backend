package swm.backstage.movis.domain.transaction_history.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryGetPagingListResDto;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactionHistories")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping()
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingList(@RequestParam String eventId,
                                                                                 @RequestParam LocalDateTime lastPaidAt,
                                                                                 @RequestParam(required = false, defaultValue = "first") String lastId,
                                                                                 @RequestParam(defaultValue = "20") int size){
        return transactionHistoryService.getTransactionHistoryPagingList(eventId,lastPaidAt, lastId, size);
    }
}
