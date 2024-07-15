package swm.backstage.movis.domain.fee;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.fee.dto.FeeDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "fee")
@Getter
@NoArgsConstructor
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paidAmount;
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_member_id")
    private EventMember eventMember;

    public Fee(FeeDto feeDto, Club club) {
        this.paidAmount = feeDto.getPaidAmount();
        this.paidAt = feeDto.getPaidAt();
        this.club = club;
    }

    public Fee(FeeDto feeDto, Club club, EventMember eventMember) {
        this.paidAmount = feeDto.getPaidAmount();
        this.paidAt = feeDto.getPaidAt();
        this.club = club;
        this.eventMember = eventMember;
    }
}
