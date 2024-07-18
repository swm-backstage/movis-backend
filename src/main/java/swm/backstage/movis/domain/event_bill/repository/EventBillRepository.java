package swm.backstage.movis.domain.event_bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.util.List;
import java.util.Optional;

public interface EventBillRepository extends JpaRepository<EventBill, Long> {
    Optional<EventBill> findByUuid(String uuid);

    // 1 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.id = :eventId " +
            "ORDER BY eb.id DESC " +
            "LIMIT :size")
    List<EventBill> getFirstPage(@Param("eventId") Long eventId,
                           @Param("size") int size);

    // n 회차용
    @Query("SELECT eb FROM EventBill eb " +
            "WHERE eb.event.id = :eventId AND eb.id < :lastId  " +
            "ORDER BY eb.id DESC " +
            "LIMIT :size")
    List<EventBill> getNextPageByEventIdAndLastId(@Param("eventId") Long eventId,
                                            @Param("lastId") Long lastId,
                                            @Param("size") int size);
}
