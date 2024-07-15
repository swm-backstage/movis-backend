package swm.backstage.movis.domain.event.dto;


import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event.Event;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EventGetDto {

    private String uuid;

    private String name;

    private Long balance;

    private Long totalPaymentAmount;

    private LocalDate paymentDeadline;

    public EventGetDto(Event event) {
        this.uuid = event.getUuid();
        this.name = event.getName();
        this.balance = event.getBalance();
        this.totalPaymentAmount = event.getTotalPaymentAmount();
        this.paymentDeadline = event.getPaymentDeadline();
    }
}
