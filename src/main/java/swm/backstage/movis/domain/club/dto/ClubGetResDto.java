package swm.backstage.movis.domain.club.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import swm.backstage.movis.domain.club.Club;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class ClubGetResDto {
    private String clubId;
    private String name;
    private String description;
    private String accountNumber;
    private Long balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String bankCode;

    public ClubGetResDto(Club club) {
        this.clubId = club.getUlid();
        this.name = club.getName();
        this.description = club.getDescription();
        this.accountNumber = club.getAccountNumber();
        this.balance = club.getAccountBook().getBalance();
        this.createdAt = club.getCreatedAt();
        this.updatedAt = club.getUpdatedAt();
        this.bankCode = club.getBankCode();
    }
}
