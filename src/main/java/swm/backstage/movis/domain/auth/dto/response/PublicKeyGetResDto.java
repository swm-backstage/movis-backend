package swm.backstage.movis.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class PublicKeyGetResDto {

    Long rsaPrivateKeyId;
    String publicKeyString;

    public PublicKeyGetResDto(Long rsaPrivateKeyId, String publicKeyString) {
        this.rsaPrivateKeyId = rsaPrivateKeyId;
        this.publicKeyString = publicKeyString;
    }
}
