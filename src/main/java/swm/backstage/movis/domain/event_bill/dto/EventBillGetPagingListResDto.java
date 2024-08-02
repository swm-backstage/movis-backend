package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_bill.EventBill;

import java.util.List;


@Getter
@NoArgsConstructor
public class EventBillGetPagingListResDto {
    List<EventBillGetElementResDto> feeElements;
    private Boolean isLast;

    public EventBillGetPagingListResDto(List<EventBill> feeList, Boolean isLast) {
        this.isLast = isLast;
        this.feeElements = feeList.stream()
                .map(EventBillGetElementResDto::new)
                .toList();
    }
}
