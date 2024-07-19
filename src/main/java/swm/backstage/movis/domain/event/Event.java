package swm.backstage.movis.domain.event;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.dto.EventCreateDto;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event", indexes = {
        @Index(name = "idx_club_id", columnList = "club_id"),
        @Index(name = "idx_club_id_event_id", columnList = "event_id, id")})
@NoArgsConstructor
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<EventMember> eventMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_book_id")
    private AccountBook accountBook;

    @OneToMany(mappedBy = "event",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<EventBill> eventBills = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<TransactionHistory> transactionHistorys = new ArrayList<>();

    @Column(name="balance")
    private Long balance;

    @Column(name="total_payment_amount")
    private Long totalPaymentAmount;

    @Column(name="payment_deadline")
    private LocalDate paymentDeadline;



    public Event(String uuid, EventCreateDto eventCreateDto, Club club, AccountBook accountBook) {
        this.uuid = uuid;
        this.name = eventCreateDto.getEventName();
        this.club =club;
        this.accountBook = accountBook;
        this.balance = 0L;
        this.totalPaymentAmount = 0L;
    }

    public void updateBalance(Long balance) {
        this.balance +=  balance;
    }


}
