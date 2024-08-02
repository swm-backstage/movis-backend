package swm.backstage.movis.domain.auth.dto;

import lombok.Getter;

@Getter
public class RSAKeyPairDto {

    private String publicKeyString;
    private String privateKeyString;

    public RSAKeyPairDto(String publicKeyString, String privateKeyString) {
        this.publicKeyString = publicKeyString;
        this.privateKeyString = privateKeyString;
    }
}
