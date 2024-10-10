package swm.backstage.movis.domain.event_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.EventMember;

import java.util.List;
import java.util.Optional;

public interface EventMemberJpaRepository extends JpaRepository<EventMember, String> {
    Optional<EventMember> findByUlid(String eventMemberId);

    Optional<EventMember> findByEventAndMemberName(Event event, String memberName);

    List<EventMember> findAllByEvent(Event event);
}
