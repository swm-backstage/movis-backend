package swm.backstage.movis.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventGatherFeeReqDto {

    private String eventId;

    private Long totalPaymentAmount;

    private LocalDate paymentDeadline;
}
