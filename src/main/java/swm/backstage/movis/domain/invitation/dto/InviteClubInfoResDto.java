package swm.backstage.movis.domain.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class InviteClubInfoResDto {
    public String clubName;
    public String clubImageUrl;
    public String clubDescription;
}
