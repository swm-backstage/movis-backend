package swm.backstage.movis.domain.event_member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_member.EventMember;

@Getter
@NoArgsConstructor
public class EventMemberListElementDto {
    private String eventMemberId;
    private Boolean isPaid;
    private Long amountToPay;

    public EventMemberListElementDto(EventMember eventMember) {
        this.eventMemberId = eventMember.getUlid();
        this.isPaid = eventMember.getIsPaid();
        this.amountToPay = eventMember.getAmountToPay();
    }
}
