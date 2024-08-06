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
    /**
     * 거래내역 저장
     */
    public void saveTransactionHistory(TransactionHistoryCreateDto dto) {
        transactionHistoryRepository.save(new TransactionHistory(UUID.randomUUID().toString(), dto));
    }
    /**
     * 전체 거래내역 페이징 (Event로)
     */
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByEvent(String eventId, LocalDateTime lastPaidAt, String lastId, int size) {
        Event event = eventService.getEventByUuid(eventId);
        List<TransactionHistory> transactionHistoryListList;
        if (lastId.equals("first")) {
            transactionHistoryListList = transactionHistoryRepository.getFirstPageByEvent(event.getId(), lastPaidAt, size + 1);
        } else {
            TransactionHistory transactionHistory = transactionHistoryRepository.findByUuid(lastId)
                    .orElseThrow(() -> new BaseException("TransactionHistory is not found", ErrorCode.ELEMENT_NOT_FOUND));
            transactionHistoryListList = transactionHistoryRepository.getNextPageByEvent(event.getId(), lastPaidAt, transactionHistory.getId(), size + 1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = transactionHistoryListList.size() < size + 1;
        if (!isLast) {
            transactionHistoryListList.remove(transactionHistoryListList.size() - 1);
        }
        return new TransactionHistoryGetPagingListResDto(transactionHistoryListList, isLast);
    }

    /**
     * 전체 거래내역 페이징 (Club 으로)
     */
    public TransactionHistoryGetPagingListResDto getTransactionHistoryPagingListByClub(String clubId, LocalDateTime lastPaidAt, String lastId, int size) {
        Club club = clubService.getClubByUuId(clubId);
        List<TransactionHistory> transactionHistoryListList;

        if (lastId.equals("first")) {
            transactionHistoryListList = transactionHistoryRepository.getFirstPageByClub(club.getId(), lastPaidAt, size + 1);
        } else {
            TransactionHistory transactionHistory = transactionHistoryRepository.findByUuid(lastId)
                    .orElseThrow(() -> new BaseException("TransactionHistory is not found", ErrorCode.ELEMENT_NOT_FOUND));
            transactionHistoryListList = transactionHistoryRepository.getNextPageByClub(club.getId(), lastPaidAt, transactionHistory.getId(), size + 1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = transactionHistoryListList.size() < size + 1;
        if (!isLast) {
            transactionHistoryListList.remove(transactionHistoryListList.size() - 1);
        }
        return new TransactionHistoryGetPagingListResDto(transactionHistoryListList, isLast);

    }

    @Transactional
    public void updateTransactionHistory(TransactionHistoryUpdateDto dto) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findByElementUuid(dto.getElementId())
                .orElseThrow(()->new BaseException("TransactionHistory not found", ErrorCode.ELEMENT_NOT_FOUND));
        transactionHistory.updateTransactionHistory(dto.getEvent(), dto.getName());
    }
    /**
     * 미분류 리스트 조회 ( not 페이징 )
     * */
    public List<TransactionHistory> getUnclassifiedTransactionHistory(String clubId) {
        return transactionHistoryRepository.findAllByClubUuidAndIsClassifiedOrderByPaidAtDesc(clubId,false);
    }
    /**
     *  미분류 개수 조회
     * */
    public Long getTransactionHistoryCount(String clubId) {
        return transactionHistoryRepository.countByClubUuidAndIsClassified(clubId,false);
    }
}