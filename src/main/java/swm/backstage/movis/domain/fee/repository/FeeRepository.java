package swm.backstage.movis.domain.fee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            "AND f.paidAt <= :lastPaidAt " +
            "ORDER BY f.id DESC " +
            "LIMIT :size")
    List<Fee> getFirstPage(@Param("eventId") Long eventId,
                           @Param("lastPaidAt") LocalDateTime lastPaidAt,
                           @Param("size") int size);

    // n 회차용
    @Query("SELECT f FROM Fee f " +
            "WHERE f.event.id = :eventId AND f.id < :lastId  " +
            "AND ((f.paidAt = :lastPaidAt AND f.id > :lastId) OR (f.paidAt < :lastPaidAt)) " +
            "ORDER BY f.id DESC " +
            "LIMIT :size")
    List<Fee> getNextPageByEventIdAndLastId(@Param("eventId") Long eventId,
                                            @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                            @Param("lastId") Long lastId,
                                            @Param("size") int size);

    @Modifying
    @Query("UPDATE Fee f " +
            "SET f.isDeleted = :status " +
            "WHERE f.event.id = :eventId ")
    int updateIsDeletedByEventId(@Param("status") Boolean status,
                                 @Param("eventId") Long eventId);
}
