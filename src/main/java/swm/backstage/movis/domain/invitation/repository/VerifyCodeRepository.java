package swm.backstage.movis.domain.invitation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.backstage.movis.domain.invitation.VerifyCode;

import java.util.Optional;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    Optional<VerifyCode> findByPhoneNumber(String phoneNumber);
}
