package swm.backstage.movis.domain.fee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeeCreateExplanationReqDto {
    private String feeId;
    private String explanation;
}
