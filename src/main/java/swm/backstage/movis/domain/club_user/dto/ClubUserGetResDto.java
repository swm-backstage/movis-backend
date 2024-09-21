package swm.backstage.movis.domain.club_user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubUserGetResDto {

    String clubUserId;
    String identifier;
    String role;

    public ClubUserGetResDto(String clubUserId, String identifier, String role) {
        this.clubUserId = clubUserId;
        this.identifier = identifier;
        this.role = role;
    }
}
