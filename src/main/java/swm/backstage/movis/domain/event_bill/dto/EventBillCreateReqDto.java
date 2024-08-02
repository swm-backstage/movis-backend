package swm.backstage.movis.domain.event_bill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventBillCreateReqDto {
    private String clubId;
    private Long amount;
    private String payName;
    private LocalDateTime paidAt;
}
