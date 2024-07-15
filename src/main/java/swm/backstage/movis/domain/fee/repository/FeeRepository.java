package swm.backstage.movis.domain.fee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.fee.Fee;

public interface FeeRepository extends JpaRepository<Fee, Long> {
}
