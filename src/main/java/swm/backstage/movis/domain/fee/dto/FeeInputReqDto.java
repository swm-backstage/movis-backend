package swm.backstage.movis.domain.fee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class FeeInputReqDto {
    private String eventMemberId;
    private Long paidAmount;
    private LocalDateTime paidAt;

    @NotNull
    @Size(min = 1,max =10)
    private String name;
    private String explanation;

    public FeeInputReqDto(String eventMemberId, Long paidAmount, LocalDateTime paidAt, String name, String explanation) {
        this.eventMemberId = eventMemberId;
        this.paidAmount = paidAmount;
        this.paidAt = paidAt;
        this.name = name;
        this.explanation = explanation;
    }
}
