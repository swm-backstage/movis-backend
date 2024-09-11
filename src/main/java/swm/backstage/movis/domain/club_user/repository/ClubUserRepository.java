package swm.backstage.movis.domain.club_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club_user.ClubUser;

import java.util.Optional;

public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

    Optional<ClubUser> findByIdentifierAndClub_Uuid(String identifier, String clubId);
}
