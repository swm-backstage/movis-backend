package swm.backstage.movis.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.auth.AuthToken;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByIdentifier(String identifier);

    void deleteAllByIdentifier(String identifier);
}