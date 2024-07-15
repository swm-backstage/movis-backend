package swm.backstage.movis.domain.fee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FeeDto {
    private String clubId;
    private String eventMemberId;
    private String name;
    private Long paidAmount;
    private LocalDateTime paidAt;
}
