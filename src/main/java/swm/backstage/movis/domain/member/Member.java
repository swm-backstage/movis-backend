package swm.backstage.movis.domain.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.member.dto.MemberCreateReqDto;
import swm.backstage.movis.global.common.DateTimeField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member",
        indexes = {
                @Index(name = "idx_name_club_uuid", columnList = "name, club_uuid")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_name_club_uuid", columnNames = {"name", "club_uuid"})
        })
@NoArgsConstructor
@Getter
public class Member extends DateTimeField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Column(unique = true, name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "phone_no", nullable = false, length = 255)
    private String phoneNo;

    @Column(name = "is_enrolled", nullable = false)
    private Boolean isEnrolled;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Column(name = "club_uuid", length = 36)
    private String clubUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<EventMember> eventMembers = new ArrayList<>();

    public Member(String uuid, Club club, MemberCreateReqDto memberCreateReqDto) {
        this.uuid = uuid;
        this.club = club;
        this.clubUuid = club.getUuid();
        this.name = memberCreateReqDto.getName();
        this.phoneNo = memberCreateReqDto.getPhoneNo();
        this.isEnrolled = Boolean.TRUE;
        this.isDeleted = Boolean.FALSE;
        this.deletedAt = LocalDateTime.now();
    }
}
