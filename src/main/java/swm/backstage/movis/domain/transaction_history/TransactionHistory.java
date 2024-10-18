package swm.backstage.movis.domain.transaction_history;


import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateContentReqDto;
import swm.backstage.movis.domain.fee.dto.FeeUpdateContentReqDto;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryCreateDto;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.transaction_history.dto.TransactionStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history", indexes = {
        @Index(name = "event_id_paid_at", columnList = "event_id, paid_at"),
        @Index(name = "idx_event_id_paid_at_th_id", columnList = "event_id, paid_at , ulid"),
        @Index(name = "club_id_paid_at", columnList = "club_id, paid_at"),
        @Index(name = "idx_club_id_paid_at_th_id", columnList = "club_id, paid_at , ulid")
})
@NoArgsConstructor
@Getter
public class TransactionHistory {

    @Id
    @Column(nullable = false, unique = true, length = 26)
    private String ulid;
    private String elementUlid;

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

    public TransactionHistory( TransactionHistoryCreateDto dto) {
        this.club =dto.getClub();
        this.ulid = UlidCreator.getUlid().toString();
        this.elementUlid = dto.getElementUlid();
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
    public void updateContent(Object dto){
        if(dto instanceof FeeUpdateContentReqDto){
            FeeUpdateContentReqDto feeDto = (FeeUpdateContentReqDto) dto;
            this.name = feeDto.getName();
            this.paidAt = feeDto.getPaidAt();
            this.amount = feeDto.getPaidAmount();
        }
        else{
            EventBillUpdateContentReqDto eventBillDto = (EventBillUpdateContentReqDto) dto;
            this.name = eventBillDto.getPayName();
            this.paidAt = eventBillDto.getPaidAt();
            this.amount = eventBillDto.getAmount();
        }
    }
}

