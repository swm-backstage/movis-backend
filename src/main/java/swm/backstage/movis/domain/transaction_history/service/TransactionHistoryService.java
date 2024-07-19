package swm.backstage.movis.domain.transaction_history.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.fee.dto.FeeGetPagingListResDto;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryCreateDto;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryGetPagingListResDto;
import swm.backstage.movis.domain.transaction_history.repository.TransactionHistoryRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final EventService eventService;
    /**
     * 거래내역 저장
     * */
    public void saveTransactionHistory(TransactionHistoryCreateDto dto){
        transactionHistoryRepository.save(new TransactionHistory(UUID.randomUUID().toString(),dto));
    }
    /**
     * 전체 거래내역 페이징
     * */
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingList(String eventId, LocalDateTime lastPaidAt, String lastId, int size) {
        Event event = eventService.getEventByUuid(eventId);
        List<TransactionHistory> transactionHistoryListList;
        if(lastId.equals("first")){
            transactionHistoryListList = transactionHistoryRepository.getFirstPage(event.getId(),lastPaidAt,size+1);
        }
        else{
            TransactionHistory transactionHistory = transactionHistoryRepository.findByUuid(lastId)
                    .orElseThrow(()->new BaseException("TransactionHistory is not found", ErrorCode.ELEMENT_NOT_FOUND));
            transactionHistoryListList = transactionHistoryRepository.getNextPageByEventIdAndLastId(event.getId(),lastPaidAt,transactionHistory.getId(),size+1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = transactionHistoryListList.size() < size + 1;
        if(!isLast){
            transactionHistoryListList.remove(transactionHistoryListList.size()-1);
        }
        return new TransactionHistoryGetPagingListResDto(transactionHistoryListList, isLast);
    }
}
