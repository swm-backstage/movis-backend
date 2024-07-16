package swm.backstage.movis.domain.event_bill;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_bill")
@NoArgsConstructor
@Getter
public class EventBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid",unique = true,nullable = false)
    private String uuid;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "pay_name")
    private String payName;

    private LocalDateTime paidAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "image", length = 500)
    private String image;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public EventBill(String uuid,EventBillCreateDto eventBillCreateDto, Club club) {
        this.uuid = uuid;
        this.payName = eventBillCreateDto.getPayName();
        this.amount = eventBillCreateDto.getAmount();
        this.club = club;
        this.paidAt = eventBillCreateDto.getPaidAt();
        this.isDeleted = false;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
