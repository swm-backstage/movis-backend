package swm.backstage.movis.domain.member.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateDto {

    @NotNull
    private String name;

    @NotNull
    private String phoneNo;
}
