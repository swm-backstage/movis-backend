package swm.backstage.movis.domain.club_user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club_user.ClubUser;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubUserListGetResDto {

    private List<ClubUserGetResDto> clubUserGetResDtoList;

    public ClubUserListGetResDto(List<ClubUserGetResDto> clubUserGetResDtoList) {
        this.clubUserGetResDtoList = clubUserGetResDtoList;
    }
}
