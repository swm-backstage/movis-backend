package swm.backstage.movis.domain.club_user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club_user.ClubUser;

@Getter
@NoArgsConstructor
public class ClubUserGetResDto {

    private String role;

    public ClubUserGetResDto(ClubUser clubUser) {
        this.role = clubUser.getRole();
    }
}
