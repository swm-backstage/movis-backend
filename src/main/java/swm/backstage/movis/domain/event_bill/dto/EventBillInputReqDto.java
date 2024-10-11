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

    public EventBillInputReqDto(Long paidAmount, LocalDateTime paidAt, String name, String explanation, String image) {
        this.paidAmount = paidAmount;
        this.paidAt = paidAt;
        this.name = name;
        this.explanation = explanation;
        this.image = image;
    }
}
