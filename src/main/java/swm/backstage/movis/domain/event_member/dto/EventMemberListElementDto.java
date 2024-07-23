package swm.backstage.movis.domain.event_member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_member.EventMember;

@Getter
@NoArgsConstructor
public class EventMemberListElementDto {
    private String uuid;
    private Boolean isPaid;
    private Long amountToPay;

    public EventMemberListElementDto(EventMember eventMember) {
        this.uuid = eventMember.getUuid();
        this.isPaid = eventMember.getIsPaid();
        this.amountToPay = eventMember.getAmountToPay();
    }
}
