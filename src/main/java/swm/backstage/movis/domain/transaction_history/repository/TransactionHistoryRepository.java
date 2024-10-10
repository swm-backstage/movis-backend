package swm.backstage.movis.domain.transaction_history.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {

    Optional<TransactionHistory> findByUlid(String ulid);

    // 1 회차용 Event로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.ulid = :eventId " +
            "AND th.paidAt <= :lastPaidAt " +
            "ORDER BY th.paidAt DESC, th.ulid ASC " +
            "LIMIT :size")
    List<TransactionHistory> getFirstPageByEvent(@Param("eventId") String eventId,
                                          @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                          @Param("size") int size);

    // n 회차용 Event로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.event.ulid = :eventId " +
            "AND ((th.paidAt = :lastPaidAt AND th.ulid > :lastId) OR (th.paidAt < :lastPaidAt)) " +
            "ORDER BY th.paidAt DESC , th.ulid ASC " +
            "LIMIT :size")
    List<TransactionHistory> getNextPageByEvent(@Param("eventId") String eventId,
                                            @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                            @Param("lastId") String lastId,
                                            @Param("size") int size);

    // 1 회차용 Club으로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.club.ulid = :clubId " +
            "AND th.paidAt <= :lastPaidAt " +
            "ORDER BY th.paidAt DESC, th.ulid ASC " +
            "LIMIT :size")
    List<TransactionHistory> getFirstPageByClub(@Param("clubId") String clubId,
                                                 @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                                 @Param("size") int size);

    // n 회차용 Club으로 조회
    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.club.ulid = :clubId " +
            "AND ((th.paidAt = :lastPaidAt AND th.ulid > :lastId) OR (th.paidAt < :lastPaidAt)) " +
            "ORDER BY th.paidAt DESC , th.ulid ASC " +
            "LIMIT :size")
    List<TransactionHistory> getNextPageByClub(@Param("clubId") String clubId,
                                                @Param("lastPaidAt") LocalDateTime lastPaidAt,
                                                @Param("lastId") String lastId,
                                                @Param("size") int size);

    @Modifying
    @Query("UPDATE TransactionHistory t " +
            "SET t.isDeleted = :status " +
            "WHERE t.event.ulid = :eventId ")
    int updateIsDeletedByEventId(@Param("status") Boolean status,
                                 @Param("eventId") String eventId);

    Optional<TransactionHistory> findByElementUlid(String elementUlid);
    List<TransactionHistory> findAllByClubUlidAndIsClassifiedOrderByPaidAtDesc(String clubId, boolean isClassified);
    Long countByClubUlidAndIsClassified(String clubId, boolean isClassified);

}
