package swm.backstage.movis.domain.fee.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.fee.Fee;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FeeGetResDto {
    private Long amount;
    private String payName;
    private String explanation;
    private LocalDateTime paidAt;

    public FeeGetResDto(Fee fee) {
        this.amount = fee.getPaidAmount();
        this.payName = fee.getName();
        this.explanation = fee.getExplanation();
        this.paidAt = fee.getPaidAt();
    }
}
