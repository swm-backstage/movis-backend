package swm.backstage.movis.domain.transaction_history.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;
import swm.backstage.movis.domain.transaction_history.dto.*;
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
    private final ClubService clubService;

    public TransactionHistory getTransactionHistory(String elementId){
        return transactionHistoryRepository.findByElementUlid(elementId).orElseThrow(()->new BaseException("해당 거래내역이 존재하지 않습니다.",ErrorCode.ELEMENT_NOT_FOUND));
    }
    /**
     * 거래내역 저장
     */
    public void saveTransactionHistory(TransactionHistoryCreateDto dto) {
        transactionHistoryRepository.save(new TransactionHistory(dto));
    }
    /**
     * 전체 거래내역 페이징 (Event로)
     */
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByEvent(String eventId, LocalDateTime lastPaidAt, String lastId, int size) {
        Event event = eventService.getEventByUuid(eventId);
        List<TransactionHistory> transactionHistoryListList;
        if (lastId.equals("first")) {
            transactionHistoryListList = transactionHistoryRepository.getFirstPageByEvent(event.getUlid(), lastPaidAt, size + 1);
        } else {
            TransactionHistory transactionHistory = transactionHistoryRepository.findByUlid(lastId)
                    .orElseThrow(() -> new BaseException("TransactionHistory is not found", ErrorCode.ELEMENT_NOT_FOUND));
            transactionHistoryListList = transactionHistoryRepository.getNextPageByEvent(event.getUlid(), lastPaidAt, transactionHistory.getUlid(), size + 1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = transactionHistoryListList.size() < size + 1;
        if (!isLast) {
            transactionHistoryListList.remove(transactionHistoryListList.size() - 1);
        }
        return new TransactionHistoryGetPagingListResDto(transactionHistoryListList, isLast, null);
    }
    /**
     * 전체 거래내역 페이징 (Club 으로)
     */
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByClub(String clubId, LocalDateTime lastPaidAt, String lastId, int size) {
        Club club = clubService.getClubByUuId(clubId);
        List<TransactionHistory> transactionHistoryListList;


        if (lastId.equals("first")) {
            transactionHistoryListList = transactionHistoryRepository.getFirstPageByClub(club.getUlid(), lastPaidAt, Boolean.FALSE,size + 1);
        } else {
            TransactionHistory transactionHistory = transactionHistoryRepository.findByUlid(lastId)
                    .orElseThrow(() -> new BaseException("TransactionHistory is not found", ErrorCode.ELEMENT_NOT_FOUND));
            transactionHistoryListList = transactionHistoryRepository.getNextPageByClub(club.getUlid(), lastPaidAt, transactionHistory.getElementUlid(), Boolean.FALSE,size + 1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = transactionHistoryListList.size() < size + 1;
        if (!isLast) {
            transactionHistoryListList.remove(transactionHistoryListList.size() - 1);
        }
        return new TransactionHistoryGetPagingListResDto(transactionHistoryListList, isLast, club.getAccountBook());

    }

    @Transactional
    public void updateTransactionHistory(TransactionHistoryUpdateDto dto) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findByElementUlid(dto.getElementId())
                .orElseThrow(()->new BaseException("TransactionHistory not found", ErrorCode.ELEMENT_NOT_FOUND));
        transactionHistory.updateTransactionHistory(dto.getEvent(), dto.getName());
    }
    /**
     * 미분류 리스트 조회 ( not 페이징 )
     * */
    public List<TransactionHistory> getUnclassifiedTransactionHistory(String clubId) {
        return transactionHistoryRepository.findAllByClubUlidAndIsClassifiedAndIsDeletedOrderByPaidAtDesc(clubId,false,false);
    }
    /**
     *  미분류 개수 조회
     * */
    public Long getTransactionHistoryCount(String clubId) {
        return transactionHistoryRepository.countByClubUlidAndIsClassifiedAndIsDeleted(clubId,false,false);
    }
}