package swm.backstage.movis.domain.club;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.member.Member;
import swm.backstage.movis.domain.transaction_history.TransactionHistory;
import swm.backstage.movis.global.common.DateTimeField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "club")
@Getter
@NoArgsConstructor
public class Club extends DateTimeField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true ,name = "uuid", length = 36)
    private String uuid;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ClubUser> clubUserList = new ArrayList<>();

    @OneToOne(mappedBy = "club", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private AccountBook accountBook;

    @Column(name = "account_number",nullable = false,length = 4)
    private String accountNumber;

    @Column(name = "bank_code", length = 10)
    private String bankCode;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Event> eventList = new ArrayList<>();

    @OneToMany(mappedBy = "club",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<EventBill> eventBills = new ArrayList<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<Fee> feeList = new ArrayList<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<TransactionHistory> transactionHistoryList = new ArrayList<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Setter
    @Column(name = "entry_code", length = 6)
    private String entryCode;

    @Setter
    @Column(name = "invite_code", length = 6)
    private String inviteCode;


    public Club(ClubCreateReqDto clubCreateReqDto, String uuid, AccountBook accountBook) {
        this.uuid = uuid;
        this.description = clubCreateReqDto.getDescription();
        this.name = clubCreateReqDto.getName();
        this.accountNumber = clubCreateReqDto.getAccountNumber();
        this.bankCode = clubCreateReqDto.getBankCode();
        this.isDeleted = Boolean.FALSE;
        this.accountBook = accountBook;
        this.thumbnail = clubCreateReqDto.getThumbnail();
        accountBook.setClub(this);
    }

    public void addClubUser(ClubUser clubUser) {
        clubUserList.add(clubUser);
    }
}
