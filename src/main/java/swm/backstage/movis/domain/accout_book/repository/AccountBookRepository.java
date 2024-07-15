package swm.backstage.movis.domain.accout_book.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.Club;

import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.query.timeout", value = "10000")})
    @Query("SELECT a FROM AccountBook a WHERE a.club = :club ")
    Optional<AccountBook> findByClubWithLock(Club club);

}
