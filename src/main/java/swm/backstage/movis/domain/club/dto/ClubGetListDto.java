package swm.backstage.movis.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubGetListDto {
    private List<ClubGetElementDto> clubGetListDto;

    public ClubGetListDto(List<Club> clubs) {
        this.clubGetListDto = clubs.stream().map(ClubGetElementDto::new).toList();
    }
}
