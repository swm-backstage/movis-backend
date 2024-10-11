package swm.backstage.movis.domain.member.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberCreateListDto {

    @NotNull
    private String clubId;

    private List<MemberCreateReqDto> memberList;

    public MemberCreateListDto(String clubId, List<MemberCreateReqDto> memberList) {
        this.clubId = clubId;
        this.memberList = memberList;
    }
}
