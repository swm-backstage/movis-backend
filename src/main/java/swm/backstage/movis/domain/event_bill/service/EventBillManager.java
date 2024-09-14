package swm.backstage.movis.domain.event_bill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.event_bill.repository.EventBillRepository;

@Service
@RequiredArgsConstructor
public class EventBillManager {

    private final EventBillRepository eventBillRepository;
    /**
     * 출금 여러개 isDeleted 수정
     * */
    @Transactional
    public int updateIsDeleted(Long eventId){
        return eventBillRepository.updateIsDeletedByEventId(Boolean.TRUE,eventId);
    }

}
