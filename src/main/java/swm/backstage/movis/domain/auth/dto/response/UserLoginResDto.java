package swm.backstage.movis.domain.auth.dto.response;


import lombok.Getter;

@Getter
public class UserLoginResDto {

    String accessToken;
    String refreshToken;

    public UserLoginResDto(String accessToken, String refreshToken) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
