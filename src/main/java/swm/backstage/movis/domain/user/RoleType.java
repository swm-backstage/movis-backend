package swm.backstage.movis.domain.user;

public enum RoleType {

    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_COORDINATOR("ROLE_COORDINATOR")
    ;

    private final String role;

    RoleType(String role) {

        this.role = role;
    }

    public String value() {

        return role;
    }
}