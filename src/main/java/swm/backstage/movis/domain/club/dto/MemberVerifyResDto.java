package swm.backstage.movis.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberVerifyResDto {

    private String clubId;
    private String accessToken;
    private String refreshToken;

    public MemberVerifyResDto(String clubId, String accessToken, String refreshToken) {
        this.clubId = clubId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
