package swm.backstage.movis.domain.auth;

public enum RoleType {

    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_EXECUTIVE("ROLE_EXECUTIVE"),
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_AUTHENTICATED("ROLE_AUTHENTICATED");
    ;

    private final String role;

    RoleType(String role) {

        this.role = role;
    }

    public String value() {

        return role;
    }
}