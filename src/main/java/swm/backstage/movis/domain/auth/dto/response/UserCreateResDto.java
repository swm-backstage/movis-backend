package swm.backstage.movis.domain.auth.dto.response;

import lombok.Getter;
import swm.backstage.movis.domain.user.User;

@Getter
public class UserCreateResDto {

    private String name;

    public UserCreateResDto(User user) {
        this.name = user.getName();
    }
}
