package swm.backstage.movis.domain.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
    Boolean existsByClubAndPhoneNo(Club clubId, String phoneNo);
    Boolean existsByNameAndClub_Ulid(String name, String clubUid);
    List<Member> findAllByClub(Club club);
    List<Member> findAllByUlidIn(List<String> ulids);
    Optional<Member> findByClubAndNameAndPhoneNo(Club club, String name, String phoneNo);
}
