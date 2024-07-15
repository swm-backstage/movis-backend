package swm.backstage.movis.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByUuid(String uuid);
    List<Event> findAllByClub(Club club);
}
