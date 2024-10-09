package swm.backstage.movis.domain.club_user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.auth.enums.RoleType;
import swm.backstage.movis.domain.auth.enums.RoleTypeConverter;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.user.User;

@Entity
@Table(name = "club_user",
        indexes = {
                @Index(name = "idx_identifier_club_uuid", columnList = "identifier, club_uuid")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_identifier_club_uuid", columnNames = {"identifier", "club_uuid"})
        })
@NoArgsConstructor
@Getter
public class ClubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "uuid", length = 36)
    private String uuid;

    @Convert(converter = RoleTypeConverter.class)
    private RoleType roleType;

    @Column(name = "identifier", nullable = false, length = 255)
    private String identifier;

    @Column(name = "club_uuid", length = 36)
    private String clubUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public ClubUser(String uuid, RoleType roleType, User user, Club club) {
        this.uuid = uuid;
        this.roleType = roleType;
        this.identifier = user.getIdentifier();
        this.clubUuid = club.getUuid();
        this.user = user;
        this.club = club;
        club.addClubUser(this);
    }

    public void updateRole(RoleType roleType){
        this.roleType = roleType;
    }
}
