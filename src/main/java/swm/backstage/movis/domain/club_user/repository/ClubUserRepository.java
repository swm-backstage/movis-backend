package swm.backstage.movis.domain.club_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.club_user.ClubUser;

import java.util.List;
import java.util.Optional;

public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

    Optional<ClubUser> findByIdentifierAndClub_Ulid(String identifier, String clubId);

    Optional<ClubUser> findByIdentifierAndClub_UlidAndIsDeleted(String identifier, String clubId, Boolean isDeleted);

    List<ClubUser> findAllByClub_UlidAndIsDeleted(String clubId, Boolean isDeleted);

    @Query("SELECT count(u.id) FROM ClubUser u WHERE u.identifier = :identifier AND u.isDeleted = false")
    Integer countClubUserByIdentifier(@Param("identifier") String identifier);
}
