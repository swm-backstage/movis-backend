package swm.backstage.movis.domain.event_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.EventMember;

import java.util.Optional;

public interface EventMemberJpaRepository extends JpaRepository<EventMember, Long> {
    Optional<EventMember> findByUuid(String eventId);

    Optional<EventMember> findByEventAndMemberName(Event event, String memberName);
}
