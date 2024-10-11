package swm.backstage.movis.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {
    Optional<Event> findByUlidAndIsDeleted(String ulid, Boolean no);

    @Query("SELECT e from Event e where e.club.ulid= :clubId and e.totalPaymentAmount= :amount")
    List<Event> findByClubIdAndTotalPaymentAmount(String clubId, Long amount);

    // 1 회차용
    @Query("SELECT e FROM Event e " +
            "WHERE e.club.ulid = :clubId " +
            "ORDER BY e.ulid DESC " +
            "LIMIT :size")
    List<Event> getFirstPage(@Param("clubId") String clubId,
                           @Param("size") int size);

    // n 회차용
    @Query("SELECT e FROM Event e " +
            "WHERE e.club.ulid = :clubId AND e.ulid < :lastId  " +
            "ORDER BY e.ulid DESC " +
            "LIMIT :size")
    List<Event> getNextPageByEventIdAndLastId(@Param("clubId") String clubId,
                                            @Param("lastId") String lastId,
                                            @Param("size") int size);

    @Query("SELECT e FROM Event e " +
            "WHERE e.club = :club " +
            "AND e.paymentDeadline > :now " +
            "order by e.paymentDeadline ASC ")
    List<Event> getCollectingMoneyEventByClub(@Param("club") Club club,
                                              @Param("now") LocalDate now);
}
