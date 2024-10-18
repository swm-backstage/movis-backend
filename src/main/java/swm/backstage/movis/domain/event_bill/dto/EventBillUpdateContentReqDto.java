package swm.backstage.movis.domain.event_bill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventBillUpdateContentReqDto {
    private Long amount;
    private LocalDateTime paidAt;
    private String payName;
    private String explanation;
    private String image;
}
