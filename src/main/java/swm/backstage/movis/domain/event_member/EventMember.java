package swm.backstage.movis.domain.event_member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.member.Member;
import swm.backstage.movis.global.common.DateTimeField;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_member")
@NoArgsConstructor
@Getter
public class EventMember extends DateTimeField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    private Long amountToPay;

    private Boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "eventMember", fetch = FetchType.LAZY)
    private List<Fee> feeList = new ArrayList<>();

    public void updateEventMember() {
        this.amountToPay = 0L;
        this.isPaid = true;
    }

    public EventMember(String uuid, Member member, Event event) {
        this.uuid = uuid;
        this.member = member;
        this.event = event;
        isPaid = false;
        amountToPay = 0L;
    }
}