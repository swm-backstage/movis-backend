package swm.backstage.movis.domain.fee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.fee.Fee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, String> {


    Optional<Fee> findByUlid(String id);

    // 1 회차용
    @Query("SELECT f FROM Fee f " +
            "WHERE f.event.ulid = :eventId " +
            "AND f.paidAt <= :lastPaidAt " +
            "ORDER BY f.ulid DESC " +
            "LIMIT :size")
    List<Fee> getFirstPage(@Param("eventId") String eventId,
                           @Param("lastPaidAt") LocalDateTime lastPaidAt,
                           @Param("size") int size);

    // n 회차용
    @Query("SELECT f FROM Fee f " +
            "WHERE f.event.ulid = :eventId AND f.ulid < :lastId  " +
            "AND ((f.paidAt = :lastPaidAt AND f.ulid> :lastId) OR (f.paidAt < :lastPaidAt)) " +
            "ORDER BY f.ulid DESC " +
            "LIMIT :size")
    List<Fee> getNextPageByEventIdAndLastId(@Param("eventId") String eventId,
                                            @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                            @Param("lastId") String lastId,
                                            @Param("size") int size);

    @Modifying
    @Query("UPDATE Fee f " +
            "SET f.isDeleted = :status " +
            "WHERE f.event.ulid = :eventId ")
    int updateIsDeletedByEventId(@Param("status") Boolean status,
                                 @Param("eventId") String eventId);
}
