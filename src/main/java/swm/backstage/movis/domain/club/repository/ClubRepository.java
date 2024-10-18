package swm.backstage.movis.domain.club.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, String> {
    Optional<Club> findByUlidAndIsDeleted(String id, Boolean isDeleted);
    Optional<Club> findByEntryCodeAndIsDeleted(String entryCode, Boolean isDeleted);
    Optional<Club> findByInviteCodeAndIsDeleted(String inviteCode, Boolean isDeleted);
}
