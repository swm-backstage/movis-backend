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
@NoArgsConstructor
public class AccountBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long balance;

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

    public void updateBalance(Long paidAmount) {
        this.balance += paidAmount;
    }
}
