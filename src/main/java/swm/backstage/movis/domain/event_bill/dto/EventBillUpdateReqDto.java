package swm.backstage.movis.domain.event_bill.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class EventBillUpdateReqDto {
    private String clubId;
    private String eventId;
}
