package swm.backstage.movis.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByIdentifier(String identifier);

    Optional<User> findByIdentifier(String identifier);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.clubUserList cu " +
            "JOIN FETCH cu.club c " +
            "JOIN FETCH c.accountBook ab " +
            "WHERE u.identifier = :identifier ")
    Optional<User> findUserWithClubUserAndClubAndAccountBook(@Param("identifier") String identifier);
}
