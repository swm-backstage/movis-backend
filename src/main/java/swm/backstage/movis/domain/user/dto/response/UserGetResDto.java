package swm.backstage.movis.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.user.User;

@Getter
@NoArgsConstructor
public class UserGetResDto {

    private String identifier;
    private String name;
    private String phoneNo;

    public UserGetResDto(User user) {
        this.identifier = user.getIdentifier();
        this.name = user.getName();
        this.phoneNo = user.getPhoneNo();
    }
}
