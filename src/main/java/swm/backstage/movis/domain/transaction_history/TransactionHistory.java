package swm.backstage.movis.domain.transaction_history;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryCreateDto;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.transaction_history.dto.TransactionStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history", indexes = {
        @Index(name = "event_id_paid_at", columnList = "event_id, paid_at"),
        @Index(name = "idx_event_id_paid_at_th_id", columnList = "event_id, paid_at , id"),
        @Index(name = "club_id_paid_at", columnList = "club_id, paid_at"),
        @Index(name = "idx_club_id_paid_at_th_id", columnList = "club_id, paid_at , id")
})
@NoArgsConstructor
@Getter
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String uuid;
    private String elementUuid;

    private String name;
    private Long amount;
    private LocalDateTime paidAt;
    private Boolean isClassified;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public TransactionHistory(String uuid, TransactionHistoryCreateDto dto) {
        this.club =dto.getClub();
        this.uuid = uuid;
        this.elementUuid = dto.getElementUuid();
        this.name = dto.getName();
        this.amount = dto.getAmount();
        this.event = dto.getEvent();
        this.paidAt = dto.getPaidAt();
        this.isClassified = dto.getIsClassified();
        this.status = dto.getStatus();
        this.isDeleted = Boolean.FALSE;
    }
    public void updateTransactionHistory(Event event, String name){
        this.event = event;
        this.name = name;
        this.isClassified = Boolean.TRUE;
    }
    public void updateIsDeleted(Boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}

