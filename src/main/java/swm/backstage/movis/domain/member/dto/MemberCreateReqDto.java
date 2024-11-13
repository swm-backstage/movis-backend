package swm.backstage.movis.domain.member.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateReqDto {

    @NotNull
    @Size(min = 1, max = 10)
    private String name;

    @NotNull
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-1234-1234 형식이어야 합니다.")
    private String phoneNo;
}
