package swm.backstage.movis.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubGetListResDto {
    private List<ClubGetElementResDto> clubGetListDto;

    public ClubGetListResDto(List<Club> clubs) {
        this.clubGetListDto = clubs.stream().map(ClubGetElementResDto::new).toList();
    }
}
