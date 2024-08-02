package swm.backstage.movis.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.member.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberGetElementResDto {
    private String memberId;
    private String clubId;
    private String name;
    private String phoneNo;
    private Boolean isDeleted;
    private Boolean isEnrolled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberGetElementResDto(Member member) {
        this.memberId = member.getUuid();
        this.clubId = member.getClub().getUuid();
        this.name = member.getName();
        this.phoneNo = member.getPhoneNo();
        this.isDeleted = member.getIsDeleted();
        this.isEnrolled = member.getIsEnrolled();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}
