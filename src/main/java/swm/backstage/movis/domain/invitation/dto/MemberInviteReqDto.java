package swm.backstage.movis.domain.invitation.dto;

import lombok.Getter;

@Getter
public class MemberInviteReqDto {
    private String name;
    private String phoneNumber;
    private String inviteCode;
}
