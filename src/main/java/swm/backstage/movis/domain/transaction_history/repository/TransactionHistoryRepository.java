package swm.backstage.movis.domain.transaction_history.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;
import swm.backstage.movis.domain.transaction_history.dto.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    Optional<TransactionHistory> findByUuid(String uuid);

    // 1 회차용 Event로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.id = :eventId " +
            "AND th.paidAt <= :lastPaidAt " +
            "ORDER BY th.paidAt DESC, th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getFirstPageByEvent(@Param("eventId") Long eventId,
                                          @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                          @Param("size") int size);

    // n 회차용 Event로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.id = :eventId " +
            "AND ((th.paidAt = :lastPaidAt AND th.id > :lastId) OR (th.paidAt < :lastPaidAt)) " +
            "ORDER BY th.paidAt DESC , th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getNextPageByEvent(@Param("eventId") Long eventId,
                                            @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                            @Param("lastId") Long lastId,
                                            @Param("size") int size);

    // 1 회차용 Club으로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.club.id = :clubId " +
            "AND th.paidAt <= :lastPaidAt " +
            "ORDER BY th.paidAt DESC, th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getFirstPageByClub(@Param("clubId") Long clubId,
                                                 @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                                 @Param("size") int size);

    // n 회차용 Club으로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.club.id = :clubId " +
            "AND ((th.paidAt = :lastPaidAt AND th.id > :lastId) OR (th.paidAt < :lastPaidAt)) " +
            "ORDER BY th.paidAt DESC , th.id ASC " +
            "LIMIT :size")
    List<TransactionHistory> getNextPageByClub(@Param("clubId") Long clubId,
                                                @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                                @Param("lastId") Long lastId,
                                                @Param("size") int size);

    Optional<TransactionHistory> findByElementUuid(String elementUuid);
    List<TransactionHistory> findAllByClubUuidAndIsClassifiedOrderByPaidAtDesc(String clubId, boolean isClassified);
}
