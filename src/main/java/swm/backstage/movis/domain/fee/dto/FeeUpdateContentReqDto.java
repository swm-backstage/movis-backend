package swm.backstage.movis.domain.fee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FeeUpdateContentReqDto {
    private Long paidAmount;
    private LocalDateTime paidAt;
    private String name;
    private String explanation;
}
