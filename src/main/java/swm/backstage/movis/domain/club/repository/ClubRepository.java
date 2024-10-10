package swm.backstage.movis.domain.club.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, String> {
    Optional<Club> findByUlidAndIsDeleted(String id, Boolean deleted);
    Optional<Club> findByEntryCode(String entryCode);
    Optional<Club> findByInviteCode(String inviteCode);
}
