package swm.backstage.movis.domain.club_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.club_user.ClubUser;

import java.util.List;
import java.util.Optional;

public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

    Optional<ClubUser> findByIdentifierAndClub_Ulid(String identifier, String clubId);

    boolean existsByIdentifierAndClub_Ulid(String identifier, String clubId);

    List<ClubUser> findAllByClub_Ulid(String clubId);

    @Query("SELECT count(u.id) FROM ClubUser u where u.identifier = :identifier ")
    Integer countClubUserByIdentifier(@Param("identifier") String identifier);
}
