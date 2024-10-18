package swm.backstage.movis.domain.transaction_history.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.transaction_history.repository.TransactionHistoryRepository;

@Service
@RequiredArgsConstructor
public class TransactionHistoryManager {

    private final TransactionHistoryRepository transactionHistoryRepository;
    /**
     * 입금 여러개 isDeleted 수정
     * */
    @Transactional
    public int updateIsDeleted(String eventId){
        return transactionHistoryRepository.updateIsDeletedByEventId(Boolean.TRUE,eventId,Boolean.FALSE);
    }
}
