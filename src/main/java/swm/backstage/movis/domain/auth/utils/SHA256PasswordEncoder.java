package swm.backstage.movis.domain.auth.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SHA256PasswordEncoder {

    // salt의 경우, 우선 사용자 UUID를 활용하여 서버에서 암호화를 진행할 계획.
    // TODO: 클라이언트에서 암호화 진행시, 별도의 랜덤값이 필요
    public String encodeWithSalt(CharSequence rawPassword, String salt) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(rawPassword.toString().getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm found", e);
        }
    }

    public boolean matchesWithSalt(CharSequence rawPassword, String encodedPassword, String salt) {

        String hashedRawPassword = encodeWithSalt(rawPassword, salt);
        return hashedRawPassword.equals(encodedPassword);
    }
}