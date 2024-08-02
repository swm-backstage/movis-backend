package swm.backstage.movis.domain.fee;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;

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

    public Fee(String uuid, FeeReqDto feeReqDto, Club club) {
        this.uuid = uuid;
        this.paidAmount = feeReqDto.getPaidAmount();
        this.paidAt = feeReqDto.getPaidAt();
        this.name = feeReqDto.getName();
        this.club = club;
    }

    public Fee(String uuid, FeeReqDto feeReqDto, Club club, EventMember eventMember) {
        this.uuid = uuid;
        this.paidAmount = feeReqDto.getPaidAmount();
        this.paidAt = feeReqDto.getPaidAt();
        this.name = feeReqDto.getName();
        this.club = club;
        this.eventMember = eventMember;
        this.event = eventMember.getEvent();
    }

    public void updateBlankElement(EventMember eventMember, FeeReqDto feeReqDto) {
        this.eventMember = eventMember;
        this.event = eventMember.getEvent();
        this.name = feeReqDto.getName();
    }
}

