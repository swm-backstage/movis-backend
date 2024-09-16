package swm.backstage.movis.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubEntryReqDto {
    private String name;
    private String phoneNumber;
    private String entryCode;
}
