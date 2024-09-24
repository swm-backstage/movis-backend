package swm.backstage.movis.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.auth.AuthToken;

@Getter
@NoArgsConstructor
public class AuthTokenDto {

    private String accessToken;
    private String refreshToken;

    public AuthTokenDto(AuthToken authToken) {
        this.accessToken = authToken.getAccessToken();
        this.refreshToken = authToken.getRefreshToken();
    }
}
