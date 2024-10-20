package swm.backstage.movis.domain.event_bill.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvenBillIdResDto {
    private String eventBillId;

    public EvenBillIdResDto(String eventBillId) {
        this.eventBillId = eventBillId;
    }
}
