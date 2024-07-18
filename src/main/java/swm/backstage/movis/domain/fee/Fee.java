package swm.backstage.movis.domain.fee;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.fee.dto.FeeDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "fee", indexes = {
        @Index(name = "idx_event_id", columnList = "event_id"),
        @Index(name = "idx_event_id_fee_id", columnList = "event_id, id")})
@Getter
@NoArgsConstructor
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    private Long paidAmount;
    private LocalDateTime paidAt;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_member_id")
    private EventMember eventMember;

    public Fee(FeeDto feeDto, Club club) {
        this.paidAmount = feeDto.getPaidAmount();
        this.paidAt = feeDto.getPaidAt();
        this.club = club;
    }

    public Fee(String uuid, FeeDto feeDto, Club club, EventMember eventMember) {
        this.uuid = uuid;
        this.paidAmount = feeDto.getPaidAmount();
        this.paidAt = feeDto.getPaidAt();
        this.name = feeDto.getName();
        this.club = club;
        this.eventMember = eventMember;
        this.event = eventMember.getEvent();
    }
}
