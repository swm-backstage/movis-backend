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

    private List<MemberCreateDto> memberList;
}
