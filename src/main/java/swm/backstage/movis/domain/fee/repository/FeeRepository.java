package swm.backstage.movis.domain.fee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.fee.Fee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {


    Optional<Fee> findByUuid(String id);

    // 1 회차용
    @Query("SELECT f FROM Fee f " +
            "WHERE f.event.id = :eventId " +
            "ORDER BY f.id DESC " +
            "LIMIT :size")
    List<Fee> getFirstPage(@Param("eventId") Long eventId,
                           @Param("size") int size);

    // n 회차용
    @Query("SELECT f FROM Fee f " +
            "WHERE f.event.id = :eventId AND f.id < :lastId  " +
            "ORDER BY f.id DESC " +
            "LIMIT :size")
    List<Fee> getNextPageByEventIdAndLastId(@Param("eventId") Long eventId,
                                                @Param("lastId") Long lastId,
                                                @Param("size") int size);
}
