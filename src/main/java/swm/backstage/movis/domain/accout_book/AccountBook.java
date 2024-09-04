package swm.backstage.movis.domain.accout_book;


import jakarta.persistence.*;
import lombok.Getter;
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
    private Long classifiedDeposit;
    private Long unClassifiedDeposit;
    private Long classifiedWithdrawal;
    private Long unClassifiedWithdrawal;

    public AccountBook() {
        this.balance = 0L;
        this.classifiedDeposit = 0L;
        this.unClassifiedDeposit = 0L;
        this.classifiedWithdrawal = 0L;
        this.unClassifiedWithdrawal = 0L;
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

    public void updateClassifiedDeposit(Long paidAmount) {
        this.classifiedDeposit += paidAmount;
    }

    public void updateUnClassifiedDeposit(Long paidAmount) {
        this.unClassifiedDeposit += paidAmount;
    }

    public void updateUnClassifiedWithdrawal(Long paidAmount) {
        this.unClassifiedWithdrawal += paidAmount;
    }

    public void updateBalance(Long paidAmount) {
        this.balance += paidAmount;
    }

    public void updateClassifiedWithdrawal(Long amount) {
        this.classifiedWithdrawal +=amount;
    }
}
