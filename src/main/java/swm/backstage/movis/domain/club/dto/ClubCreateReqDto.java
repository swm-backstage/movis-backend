package swm.backstage.movis.domain.club.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class ClubCreateReqDto {

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Account number must be exactly 4 digits")
    private String accountNumber;

    @NotNull
    @Pattern(regexp = "^\\d{3}$", message = "Bank code must be exactly 3 digits")
    private String bankCode;

    private String thumbnail;
}
