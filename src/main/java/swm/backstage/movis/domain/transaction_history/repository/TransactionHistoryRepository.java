package swm.backstage.movis.domain.transaction_history.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    Optional<TransactionHistory> findByUuid(String uuid);
    // 1 회차용
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.id = :eventId " +
            "AND th.paidAt <= :lastPaidAt " +
            "ORDER BY th.paidAt DESC, th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getFirstPage(@Param("eventId") Long eventId,
                                          @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                          @Param("size") int size);

    // n 회차용
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.id = :eventId " +
            "AND ((th.paidAt = :lastPaidAt AND th.id > :lastId) OR (th.paidAt < :lastPaidAt)) " +
            "ORDER BY th.paidAt DESC , th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getNextPageByEventIdAndLastId(@Param("eventId") Long eventId,
                                            @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                            @Param("lastId") Long lastId,
                                            @Param("size") int size);
}
