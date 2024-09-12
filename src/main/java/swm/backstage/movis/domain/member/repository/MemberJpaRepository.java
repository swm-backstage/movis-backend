package swm.backstage.movis.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.member.Member;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Boolean existsByClubAndPhoneNo(Club clubId, String phoneNo);
    Boolean existsByNameAndClub_Uuid(String name, String clubUid);
    List<Member> findAllByClub(Club club);
    List<Member> findAllByUuidIn(List<String> uuids);
}
