package swm.backstage.movis.domain.member.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.member.Member;
import swm.backstage.movis.domain.member.dto.MemberCreateListDto;
import swm.backstage.movis.domain.member.dto.MemberCreateReqDto;
import swm.backstage.movis.domain.member.repository.MemberJdbcRepository;
import swm.backstage.movis.domain.member.repository.MemberJpaRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
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
                .map(memberCreateDto -> new Member(club,memberCreateDto))
                .toList();

        memberJdbcRepository.bulkSave(memberList);
    }

    public Boolean existedByNameAndClubUuid(String name, String clubId){
        return memberJpaRepository.existsByNameAndClub_Ulid(name, clubId);
    }
    public void create(String clubUid, MemberCreateReqDto memberCreateReqDto) {
        Club club = clubService.getClubByUuId(clubUid);
        Member member = new Member( club, memberCreateReqDto);
        memberJpaRepository.save(member);
    }


    public List<Member> getMemberList(String clubId) {
        return memberJpaRepository.findAllByClub(clubService.getClubByUuId(clubId));
    }

    public List<Member> getMemberListByUuids(List<String> uuids) {
        return memberJpaRepository.findAllByUlidIn(uuids);
    }

    public boolean isMemberExist(String clubId, String name, String phoneNo) {
        return memberJpaRepository.findByClubAndNameAndPhoneNo(clubService.getClubByUuId(clubId), name, phoneNo)
                .orElseThrow(() -> new BaseException("일치하는 유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND)) != null;
    }
}
