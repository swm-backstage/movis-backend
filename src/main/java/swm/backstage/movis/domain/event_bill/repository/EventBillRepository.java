package swm.backstage.movis.domain.event_bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.event_bill.EventBill;

public interface EventBillRepository extends JpaRepository<EventBill, Long> {
}
