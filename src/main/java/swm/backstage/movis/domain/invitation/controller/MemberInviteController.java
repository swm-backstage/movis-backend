package swm.backstage.movis.domain.invitation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.invitation.dto.CheckVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.GetVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.InviteClubInfoResDto;
import swm.backstage.movis.domain.invitation.dto.MemberInviteReqDto;
import swm.backstage.movis.domain.invitation.service.MemberInviteManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitations")
public class MemberInviteController {
    private final MemberInviteManager memberInviteManager;

    @GetMapping("/verify/{inviteCode}")
    public InviteClubInfoResDto getInviteClubInfo(@PathVariable String inviteCode) {
        return memberInviteManager.getInviteClubInfo(inviteCode);
    }

    // 전화번호 인증
    @PostMapping("/verify/request")
    public void requestVerifyPhoneNumber(@RequestBody GetVerifyCodeReqDto dto) {
        memberInviteManager.createVerifyCode(dto);
    }

    @PostMapping("/verify/check")
    public boolean checkVerifyPhoneNumber(@RequestBody CheckVerifyCodeReqDto dto) {
        return memberInviteManager.verifyPhoneNumber(dto);
    }

    // 멤버 가입
    @PostMapping("/member")
    public String createMember(@RequestBody MemberInviteReqDto dto) {
        return memberInviteManager.createMember(dto);
    }

}