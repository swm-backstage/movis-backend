package swm.backstage.movis.domain.transaction_history.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryGetPagingListResDto;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryListDto;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryService;
import swm.backstage.movis.global.common.response.EntityCntResDto;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactionHistories")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/fromEvent")
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByEvent(@RequestParam @Param("eventId") String eventId,
                                                                                 @RequestParam LocalDateTime lastPaidAt,
                                                                                 @RequestParam(required = false, defaultValue = "first") String lastId,
                                                                                 @RequestParam(defaultValue = "20") int size){
        return transactionHistoryService.getTransactionHistoryPagingListByEvent(eventId,lastPaidAt, lastId, size);
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/fromClub")
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByFestival(@RequestParam @Param("clubId") String clubId,
                                                                                 @RequestParam LocalDateTime lastPaidAt,
                                                                                 @RequestParam(required = false, defaultValue = "first") String lastId,
                                                                                 @RequestParam(defaultValue = "20") int size){
        return transactionHistoryService.getTransactionHistoryPagingListByClub(clubId,lastPaidAt, lastId, size);
    }
    /**
     *  미분류 리스트 조회
     * */
    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/unClassification")
    public TransactionHistoryListDto getUnclassifiedTransactionHistory(@RequestParam("clubId") @Param("clubId") String clubId){
        return new TransactionHistoryListDto(transactionHistoryService.getUnclassifiedTransactionHistory(clubId));
    }
    /**
     *  미분류 개수 조회
     * */
    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/unClassification/count")
    public EntityCntResDto getUnClassificationCount(@RequestParam("clubId") @Param("clubId") String clubId){
        return new EntityCntResDto(transactionHistoryService.getTransactionHistoryCount(clubId));
    }
}
