package swm.backstage.movis.domain.event_bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.util.Optional;

public interface EventBillRepository extends JpaRepository<EventBill, Long> {
    Optional<EventBill> findByUuid(String uuid);
}
