package swm.backstage.movis.domain.invitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.invitation.dto.CheckVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.GetVerifyCodeReqDto;
import swm.backstage.movis.domain.invitation.dto.InviteClubInfoResDto;
import swm.backstage.movis.domain.invitation.dto.MemberInviteReqDto;
import swm.backstage.movis.domain.member.dto.MemberCreateReqDto;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@Component
@RequiredArgsConstructor
public class MemberInviteManager {
    private final MessageService messageService;
    private final VerifyService verifyService;
    private final MemberService memberService;
    private final ClubService clubService;

    // 초대장 조회
    public InviteClubInfoResDto getInviteClubInfo(String inviteCode) {
        Club selectedClub = clubService.getClubByInviteCode(inviteCode);
        return new InviteClubInfoResDto(selectedClub.getName(), "", selectedClub.getDescription());
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
            throw new BaseException("인증되지 않은 번호입니다 : "+dto.getPhoneNumber(), ErrorCode.UNAUTHENTICATED_REQUEST);
        }
        String clubId = clubService.getClubUuidByInviteCode(dto.getInviteCode());

        try {
            MemberCreateReqDto memberCreateReqDto = new MemberCreateReqDto(dto.getName(), dto.getPhoneNumber());
            memberService.create(clubId, memberCreateReqDto);
        } catch (Exception e) {
            throw new BaseException("멤버 가입에 실패했습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        verifyService.deleteVerifyCode(dto.getPhoneNumber());
        return clubId;
    }

}
