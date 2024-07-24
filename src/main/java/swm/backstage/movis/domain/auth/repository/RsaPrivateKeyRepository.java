package swm.backstage.movis.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.auth.RsaPrivateKey;

public interface RsaPrivateKeyRepository extends JpaRepository<RsaPrivateKey, Long> {
}
