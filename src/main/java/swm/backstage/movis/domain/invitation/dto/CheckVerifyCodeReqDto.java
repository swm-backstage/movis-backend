package swm.backstage.movis.domain.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckVerifyCodeReqDto {
    private String phoneNumber;
    private String verifyCode;
}
