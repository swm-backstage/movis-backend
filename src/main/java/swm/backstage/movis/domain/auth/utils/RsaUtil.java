package swm.backstage.movis.domain.auth.utils;


import org.springframework.stereotype.Component;
import swm.backstage.movis.domain.auth.dto.RSAKeyPairDto;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


@Component
public class RsaUtil {

    public RSAKeyPairDto getRsaKeyPairDto() {

        try{
            SecureRandom secureRandom = new SecureRandom();

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            return new RSAKeyPairDto(
                    Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()),
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())
            );
        } catch (NoSuchAlgorithmException e) {

            throw new BaseException(e.getMessage(), ErrorCode.DECRYPTION_FAILED);
        }
    }

    public String decryptData(String encryptedData, String privateKeyString)  {

        byte[] decryptedBytes;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKeyFromBase64String(privateKeyString));

            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        } catch (Exception e) {

            throw new BaseException(e.getMessage(), ErrorCode.DECRYPTION_FAILED);
        }

        return new String(decryptedBytes);
    }

    private PrivateKey getPrivateKeyFromBase64String(String privateKeyString) {

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new BaseException(e.getMessage(), ErrorCode.DECRYPTION_FAILED);
        }
    }
}
