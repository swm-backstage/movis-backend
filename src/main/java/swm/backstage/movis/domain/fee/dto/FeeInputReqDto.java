package swm.backstage.movis.domain.fee.dto;

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
    private String name;
    private String explanation;
}
