package swm.backstage.movis.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Boolean existsByClubAndPhoneNo(Club clubId, String phoneNo);
    List<Member> findAllByClub(Club club);
    List<Member> findAllByUuidIn(List<String> uuids);
    Optional<Member> findByClubAndNameAndPhoneNo(Club club, String name, String phoneNo);
}
