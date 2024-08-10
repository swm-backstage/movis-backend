package swm.backstage.movis.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubGetUidResDto {
    private String clubId;

    public ClubGetUidResDto(String clubId) {
        this.clubId = clubId;
    }
}
