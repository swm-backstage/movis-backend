package swm.backstage.movis.global.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AWSService {
    private final AmazonS3 amazonS3;

    /** AWS S3 presigned url 생성
     *  반환되는 주소로 PUT 요청을 보내면 파일 업로드 가능
     * 영수증 이미지는 다음으로 조회 가능 -> https://movis-bucket.s3.ap-northeast-2.amazonaws.com/billImage/이미지명.확장자명
     */
    public String generatePreSignUrl(String fileName, String bucketName) {
        String filePath = "billImage/" + fileName; // 'billImage/' 폴더에 파일 저장
        HttpMethod httpMethod = HttpMethod.PUT; // 파일 업로드를 위한 HTTP 메소드

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분간 유효
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }
}
