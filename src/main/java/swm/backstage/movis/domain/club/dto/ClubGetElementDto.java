package swm.backstage.movis.domain.club.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class ClubGetElementDto {
    private String clubId;
    private String name;
    private String description;
    private String accountNumber;
    private Long balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String bankCode;
    private int memberCnt;

    public ClubGetElementDto(Club club) {
        this.clubId = club.getUuid();
        this.name = club.getName();
        this.description = club.getDescription();
        this.accountNumber = club.getAccountNumber();
        this.balance = club.getAccountBook().getBalance();
        this.createdAt = club.getCreatedAt();
        this.updatedAt = club.getUpdatedAt();
        this.bankCode = club.getBankCode();
        this.memberCnt = club.getMemberList().size();
    }
}
