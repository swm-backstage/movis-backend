package swm.backstage.movis.domain.event_bill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@NoArgsConstructor
public class EventBillUpdateDto {
    private String clubId;
    private String eventId;
}
