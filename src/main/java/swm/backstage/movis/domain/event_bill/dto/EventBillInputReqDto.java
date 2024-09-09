package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventBillInputReqDto {
    private Long paidAmount;
    private LocalDateTime paidAt;
    private String name;
    private String explanation;
    private String image;
}
