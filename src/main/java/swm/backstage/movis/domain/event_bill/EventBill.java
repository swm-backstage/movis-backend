package swm.backstage.movis.domain.event_bill;


import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateReqDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillInputReqDto;
import swm.backstage.movis.global.common.DateTimeField;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_bill", indexes = {
        @Index(name = "idx_event_id", columnList = "event_id"),
        @Index(name = "idx_event_id_eb_id", columnList = "event_id, ulid")
})
@NoArgsConstructor
@Getter
public class EventBill extends DateTimeField {

    @Id
    @Column(name = "uuid",unique = true,nullable = false)
    private String ulid;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "pay_name")
    private String payName;

    private LocalDateTime paidAt;

    @Column(name = "image", length = 500)
    private String image;


    @Setter
    @Column(length = 100)
    private String explanation;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public EventBill(EventBillCreateReqDto eventBillCreateReqDto, Club club) {
        this.ulid = UlidCreator.getUlid().toString();
        this.payName = eventBillCreateReqDto.getPayName();
        this.amount = eventBillCreateReqDto.getAmount();
        this.club = club;
        this.paidAt = eventBillCreateReqDto.getPaidAt();
        this.isDeleted = false;
    }

    public EventBill(EventBillInputReqDto dto, Club club, Event event) {
        this.ulid = UlidCreator.getUlid().toString();
        this.payName = dto.getName();
        this.amount = dto.getPaidAmount();
        this.club = club;
        this.event = event;
        this.isDeleted = false;
        this.paidAt = dto.getPaidAt();
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void updateIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
