package swm.backstage.movis.domain.club.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.dto.*;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.member.service.MemberService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final MemberService memberService;

    @PostMapping()
    public ClubGetResDto createClub(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                    @RequestBody @Validated ClubCreateReqDto clubCreateReqDto) {
        return new ClubGetResDto(clubService.createClub(clubCreateReqDto, principal.getIdentifier()));
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/{clubId}")
    public ClubGetResDto getClub(@PathVariable("clubId") @Param("clubId") String clubId){
        return new ClubGetResDto(clubService.getClubByUuId(clubId));
    }

    @GetMapping()
    @Transactional
    public ClubGetListResDto getClubList(@AuthenticationPrincipal AuthenticationPrincipalDetails principal){
        List<Club> clubList = clubService.getClubList((principal.getIdentifier()));
        return new ClubGetListResDto(clubService.getClubList(principal.getIdentifier()));
    }

    @GetMapping("/forAlert")
    public ClubGetUidResDto getClubUid(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                       @RequestParam("accountNumber") String accountNumber){
        return new ClubGetUidResDto(clubService.getClubUid(accountNumber, principal.getIdentifier()));
    }

    /**
     * 입장 코드
     */
    @PostMapping("/{clubId}/entryCode")
    public String updateEntryCode(@PathVariable("clubId") String clubId){
        return clubService.updateEntryCode(clubId);
    }

    @GetMapping("/entryCode/{entryCode}")
    public ClubInfoResDto getEntryClubInfo(@PathVariable("entryCode") String entryCode){
        return clubService.getClubInfoByEntryCode(entryCode); // 없으면 메소드 내에서 예외처리 됨
    }

    // 해당 멤버가 있는지 확인 후, clubId 반환
    // TODO : 멤버에게 JWT 토큰 전달
    @PostMapping("/entryCode/verify")
    public String verifyMember(@RequestBody ClubEntryReqDto clubEntryReqDto){
        String clubId = clubService.getClubUuidByEntryCode(clubEntryReqDto.getEntryCode());
        memberService.isMemberExist(clubId, clubEntryReqDto.getName(), clubEntryReqDto.getPhoneNumber());
        return clubId;
    }

    /**
     * 초대 코드
     */
    @PostMapping("/{clubId}/inviteCode")
    public String updateInviteCode(@PathVariable("clubId") String clubId){
        return clubService.updateInviteCode(clubId);
    }
}
