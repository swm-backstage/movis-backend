package swm.backstage.movis.domain.club.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByUuidAndIsDeleted(String id, Boolean deleted);
}
