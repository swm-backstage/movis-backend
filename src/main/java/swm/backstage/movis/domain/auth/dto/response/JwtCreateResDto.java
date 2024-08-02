package swm.backstage.movis.domain.auth.dto.response;


import lombok.Getter;

@Getter
public class JwtCreateResDto {

    String accessToken;
    String refreshToken;

    public JwtCreateResDto(String accessToken, String refreshToken) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
