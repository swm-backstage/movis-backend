package swm.backstage.movis.domain.invitation.service;

import net.nurigo.java_sdk.api.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class MessageService {
    @Value("${coolsms.key}")
    private String apiKey;
    @Value("${coolsms.secret}")
    private String apiSecret;
    @Value("${coolsms.number}")
    private String fromPhoneNumber;

    public String sendSms(String to) {
        try {
            String numStr = generateRandomNumber();
            Message coolsms = new Message(apiKey, apiSecret);

            HashMap<String, String> params = new HashMap<>();
            params.put("to", to); // 수신
            params.put("from", fromPhoneNumber); // 발신
            params.put("type", "SMS");
            params.put("text", "인증번호는 [" + numStr + "] 입니다.");

            coolsms.send(params); // 메시지 전송
            return numStr;
        } catch (Exception e) {
            throw new RuntimeException("SMS 전송에 실패했습니다.");
        }
    }

    // 인증번호 생성
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

}
