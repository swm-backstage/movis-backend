package swm.backstage.movis.domain.invitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.invitation.dto.CheckVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.GetVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.MemberInviteReqDto;
import swm.backstage.movis.domain.member.dto.MemberCreateReqDto;
import swm.backstage.movis.domain.member.service.MemberService;

@Component
@RequiredArgsConstructor
public class MemberInviteManager {
    private final MessageService messageService;
    private final VerifyService verifyService;
    private final MemberService memberService;
    private final ClubService clubService;

    // 초대장 조회
    public boolean isInviteCodeValid(String inviteCode) {
        // 오류 발생 시, 메소드 내에서 예외처리 됨
        clubService.getClubUuidByInviteCode(inviteCode);
        return true;
    }

    // 전화번호 인증코드 생성
    public void createVerifyCode(GetVerifyCodeReqDto dto) {
        String verifyCode = messageService.sendSms(dto.getPhoneNumber());
        verifyService.saveVerifyCode(dto.getPhoneNumber(), verifyCode);
    }

    // 전화번호 인증코드 확인
    public boolean verifyPhoneNumber(CheckVerifyCodeReqDto dto) {
        return verifyService.verifyPhoneNumber(dto.getPhoneNumber(), dto.getVerifyCode());
    }

    // 멤버 가입
    public String createMember(MemberInviteReqDto dto) {
        if (!verifyService.isVerifiedPhoneNumber(dto.getPhoneNumber())) {
            throw new RuntimeException("인증되지 않은 번호입니다 : "+dto.getPhoneNumber());
        }
        verifyService.deleteVerifyCode(dto.getPhoneNumber());
        String clubId = clubService.getClubUuidByInviteCode(dto.getInviteCode());

        MemberCreateReqDto memberCreateReqDto = new MemberCreateReqDto(dto.getName(), dto.getPhoneNumber());
        memberService.create(clubId, memberCreateReqDto);
        return clubId;
    }

}
