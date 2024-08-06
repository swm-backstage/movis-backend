package swm.backstage.movis.domain.accout_book;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_book")
@Getter
public class AccountBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long balance;
    private Long totalDeposit;
    private Long totalWithdrawal;

    public AccountBook() {
        this.balance = 0L;
        this.totalDeposit = 0L;
        this.totalWithdrawal = 0L;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Event> eventList = new ArrayList<>();

    public void setClub(Club club) {
        this.club = club;
    }

    public AccountBook(Long balance) {
        this.balance = balance;

    }

    public void updateBalanceWithFee(Long paidAmount) {
        this.balance += paidAmount;
        this.totalDeposit += paidAmount;
    }

    public void updateBalanceWithEventBill(Long paidAmount) {
        this.balance += paidAmount;
        this.totalWithdrawal += paidAmount;
    }
}
