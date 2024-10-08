package swm.backstage.movis.domain.auth.enums;

public enum PlatformType {

    APP("APP"),
    WEB("WEB")
    ;

    private final String platformType;

    PlatformType(String platformType) {

        this.platformType = platformType;
    }

    public String value() {

        return platformType;
    }
}
