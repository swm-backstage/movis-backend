package swm.backstage.movis.domain.auth.enums;

import lombok.Getter;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Arrays;

@Getter
public enum RoleType {

    // 앱 사용자
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_EXECUTIVE("ROLE_EXECUTIVE"),
    // 웹 사용자
    ROLE_MEMBER("ROLE_MEMBER");

    private final String role;

    RoleType(String role) {

        this.role = role;
    }

    public static RoleType ofRole(String role) {

        return Arrays.stream(RoleType.values())
                .filter(v -> v.getRole().equals(role))
                .findAny()
                .orElseThrow(() -> new BaseException("role is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }
}