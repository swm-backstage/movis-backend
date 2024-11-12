package swm.backstage.movis.domain.event.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventUpdateReqDto {

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @NotNull
    private LocalDate paymentDeadline;

    @NotNull
    private Long totalPaymentAmount;
}
