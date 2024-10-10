package swm.backstage.movis.domain.event_bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventBillRepository extends JpaRepository<EventBill, String> {
    Optional<EventBill> findByUlid(String ulid);

    // 1 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.ulid = :eventId " +
            "AND eb.paidAt <= :lastPaidAt " +
            "ORDER BY eb.paidAt DESC , eb.ulid ASC " +
            "LIMIT :size")
    List<EventBill> getFirstPage(@Param("eventId") String eventId,
                                 @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                 @Param("size") int size);

    // n 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.ulid = :eventId AND eb.ulid < :lastId  " +
            "AND ((eb.paidAt = :lastPaidAt AND eb.ulid > :lastId) OR (eb.paidAt < :lastPaidAt)) " +
            "ORDER BY eb.paidAt DESC , eb.ulid ASC " +
            "LIMIT :size")
    List<EventBill> getNextPage(@Param("eventId") String eventId,
                                @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                @Param("lastId") String lastId,
                                @Param("size") int size);

    @Modifying
    @Query("UPDATE EventBill e " +
            "SET e.isDeleted = :status " +
            "WHERE e.event.ulid = :eventId ")
    int updateIsDeletedByEventId(@Param("status") Boolean status,
                                 @Param("eventId") String eventId);
}
