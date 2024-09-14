package swm.backstage.movis.domain.event_bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventBillRepository extends JpaRepository<EventBill, Long> {
    Optional<EventBill> findByUuid(String uuid);

    // 1 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.id = :eventId " +
            "AND eb.paidAt <= :lastPaidAt " +
            "ORDER BY eb.paidAt DESC , eb.id ASC " +
            "LIMIT :size")
    List<EventBill> getFirstPage(@Param("eventId") Long eventId,
                                 @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                 @Param("size") int size);

    // n 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.id = :eventId AND eb.id < :lastId  " +
            "AND ((eb.paidAt = :lastPaidAt AND eb.id > :lastId) OR (eb.paidAt < :lastPaidAt)) " +
            "ORDER BY eb.paidAt DESC , eb.id ASC " +
            "LIMIT :size")
    List<EventBill> getNextPage(@Param("eventId") Long eventId,
                                @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                @Param("lastId") Long lastId,
                                @Param("size") int size);

    @Modifying
    @Query("UPDATE EventBill e " +
            "SET e.isDeleted = :status " +
            "WHERE e.event.id = :eventId ")
    int updateIsDeletedByEventId(@Param("status") Boolean status,
                                 @Param("eventId") Long eventId);
}
