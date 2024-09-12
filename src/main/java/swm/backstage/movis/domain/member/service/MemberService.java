package swm.backstage.movis.domain.member.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.array.JsonArrayViaElementArgumentReturnTypeResolver;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.member.Member;
import swm.backstage.movis.domain.member.dto.MemberCreateListDto;
import swm.backstage.movis.domain.member.repository.MemberJdbcRepository;
import swm.backstage.movis.domain.member.repository.MemberJpaRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final ClubService clubService;
    private final MemberJdbcRepository memberJdbcRepository;

    /**
     * List로 생성시
     * */
    @Transactional
    public void createAll(MemberCreateListDto memberCreateListDto) {
        Club club = clubService.getClubByUuId(memberCreateListDto.getClubId());

        //member리스트 조회 후 Map 자료구조로 변경
        Set<String> existMemberSet = memberJpaRepository.findAllByClub(club).stream()
                .map(o-> o.getPhoneNo()+"+"+o.getName()).collect(Collectors.toSet());

        // memberCreateListDto에 받아온 Member 중복 비교
        List<Member> memberList =  memberCreateListDto.getMemberList().stream()
                .filter(memberCreateDto -> !existMemberSet.contains(memberCreateDto.getPhoneNo()+"+"+memberCreateDto.getName()))
                .map(memberCreateDto -> new Member(UUID.randomUUID().toString(),club,memberCreateDto))
                .toList();

        memberJdbcRepository.bulkSave(memberList);
    }

    public Boolean existedByNameAndClubUuid(String name, String clubId){
        return memberJpaRepository.existsByNameAndClub_Uuid(name, clubId);
    }

    public List<Member> getMemberList(String clubId) {
        return memberJpaRepository.findAllByClub(clubService.getClubByUuId(clubId));
    }

    public List<Member> getMemberListByUuids(List<String> uuids) {
        return memberJpaRepository.findAllByUuidIn(uuids);
    }
}
