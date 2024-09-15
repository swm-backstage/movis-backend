package swm.backstage.movis.domain.club.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.club.dto.*;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.domain.user.service.UserService;


@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
    private final MemberService memberService;

    @PostMapping()
    public ClubGetResDto createClub(Authentication authentication,@RequestBody @Validated ClubCreateReqDto clubCreateReqDto) {
        return new ClubGetResDto(clubService.createClub(clubCreateReqDto,authentication.getName()));
    }
    @GetMapping("/{clubId}")
    public ClubGetResDto getClub(@PathVariable("clubId") String clubId){
        return new ClubGetResDto(clubService.getClubByUuId(clubId));
    }

    @GetMapping()
    public ClubGetListResDto getClubList(Authentication authentication){
        return new ClubGetListResDto(clubService.getClubList(authentication.getName()));
    }

    @GetMapping("/forAlert")
    public ClubGetUidResDto getClubUid(Authentication authentication, @RequestParam("accountNumber") String accountNumber){
        return new ClubGetUidResDto(clubService.getClubUid(accountNumber, authentication.getName()));
    }

    /**
     * 입장 코드
     */
    @PostMapping("/{clubId}/entryCode")
    public String updateEntryCode(@PathVariable("clubId") String clubId){
        return clubService.updateEntryCode(clubId);
    }

    @GetMapping("/entryCode/{entryCode}")
    public boolean isEntryCodeValid(@PathVariable("entryCode") String entryCode){
        clubService.getClubUuidByEntryCode(entryCode); // 없으면 메소드 내에서 예외처리 됨
        return true;
    }

    // 해당 멤버가 있는지 확인 후, clubId 반환
    // TODO : 멤버에게 JWT 토큰 전달
    @GetMapping("/entryCode/verify")
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
