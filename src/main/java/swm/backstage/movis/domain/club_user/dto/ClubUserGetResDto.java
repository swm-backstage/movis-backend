package swm.backstage.movis.domain.club_user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.auth.enums.RoleType;

@Getter
@NoArgsConstructor
public class ClubUserGetResDto {

    String clubUserId;
    String identifier;
    RoleType role;

    public ClubUserGetResDto(String clubUserId, String identifier, RoleType role) {
        this.clubUserId = clubUserId;
        this.identifier = identifier;
        this.role = role;
    }
}
