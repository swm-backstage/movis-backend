package swm.backstage.movis.domain.fee;


import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.fee.dto.FeeInputReqDto;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;
import swm.backstage.movis.domain.fee.dto.FeeUpdateContentReqDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "fee", indexes = {
        @Index(name = "idx_event_id", columnList = "event_id"),
        @Index(name = "idx_event_id_fee_id", columnList = "event_id, ulid")})
@Getter
@NoArgsConstructor
public class Fee {

    @Id
    @Column(nullable = false, unique = true, length = 26)
    private String ulid;

    private Long paidAmount;
    private LocalDateTime paidAt;
    private String name;
    private Boolean isDeleted;

    @Setter
    @Column(length = 100)
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_member_id")
    private EventMember eventMember;



    public Fee(FeeReqDto feeReqDto, Club club) {
        this.ulid = UlidCreator.getUlid().toString();
        this.paidAmount = feeReqDto.getPaidAmount();
        this.paidAt = feeReqDto.getPaidAt();
        this.name = feeReqDto.getName();
        this.club = club;
        this.isDeleted = false;
    }

    public Fee(FeeReqDto feeReqDto, Club club, EventMember eventMember) {
        this.ulid = UlidCreator.getUlid().toString();
        this.paidAmount = feeReqDto.getPaidAmount();
        this.paidAt = feeReqDto.getPaidAt();
        this.name = feeReqDto.getName();
        this.club = club;
        this.eventMember = eventMember;
        this.event = eventMember.getEvent();
        this.isDeleted = false;
    }

    public Fee(FeeInputReqDto feeInputReqDto, Club club, Event event, EventMember eventMember) {
        this.ulid = UlidCreator.getUlid().toString();
        this.paidAmount = feeInputReqDto.getPaidAmount();
        this.paidAt = feeInputReqDto.getPaidAt();
        this.name = feeInputReqDto.getName();
        this.explanation = feeInputReqDto.getExplanation();
        this.club = club;
        this.event = event;
        this.eventMember = eventMember;
        this.isDeleted = false;
    }

    public void updateBlankElement(EventMember eventMember, FeeReqDto feeReqDto) {
        this.eventMember = eventMember;
        this.event = eventMember.getEvent();
        this.name = feeReqDto.getName();
    }

    public void updateContent(FeeUpdateContentReqDto feeUpdateContentReqDto){
        this.paidAmount =feeUpdateContentReqDto.getPaidAmount();
        this.paidAt =feeUpdateContentReqDto.getPaidAt();
        this.name = feeUpdateContentReqDto.getName();
        this.explanation = feeUpdateContentReqDto.getExplanation();
    }

    public void updateIsDeleted(Boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}

