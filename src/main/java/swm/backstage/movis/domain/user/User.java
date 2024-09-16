package swm.backstage.movis.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.auth.dto.request.UserCreateReqDto;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.global.common.DateTimeField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user", indexes = {
    @Index(name = "idx_user_identifier", columnList = "identifier")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends DateTimeField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, length = 36)
    private String uuid;

    @Column(name = "identifier", nullable = false, length = 255)
    private String identifier;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "phone_no", nullable = false, length = 255)
    private String phoneNo;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ClubUser> clubUserList = new ArrayList<>();

    public User(String uuid, UserCreateReqDto userCreateReqDto) {

        this.uuid = uuid;
        this.identifier = userCreateReqDto.getIdentifier();
        this.password =  userCreateReqDto.getPassword();
        this.name = userCreateReqDto.getName();
        this.phoneNo = userCreateReqDto.getPhoneNo();

        // TODO: soft delete
        this.isDeleted = Boolean.FALSE;
        this.deletedAt = null;
    }

    public void setUserDeleted() {

        this.isDeleted = Boolean.TRUE;
        this.deletedAt = LocalDateTime.now();
    }

    public void setUserRestored() {

        this.isDeleted = Boolean.FALSE;
        this.deletedAt = null;
    }
}



