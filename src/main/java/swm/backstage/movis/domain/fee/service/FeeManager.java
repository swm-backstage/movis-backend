package swm.backstage.movis.domain.fee.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.fee.repository.FeeRepository;

@Service
@RequiredArgsConstructor
public class FeeManager {

    private final FeeRepository feeRepository;
    /**
     * 입금 여러개 isDeleted 수정
     * */
    @Transactional
    public int updateIsDeleted(Long eventId){
        return feeRepository.updateIsDeletedByEventId(Boolean.TRUE,eventId);
    }
}
