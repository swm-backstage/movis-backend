package swm.backstage.movis.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventUpdateReqDto {
    private String name;
    private LocalDate paymentDeadline;
    private Long totalPaymentAmount;
}
