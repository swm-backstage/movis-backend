package swm.backstage.movis.global.aws.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.global.aws.service.AWSService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AWSController {


    // TODO: production의 경우, yml 없이 OIDC로 설정하기
    @Value("${amazon.aws.s3.bucket}")
    private String bucketName;
    private final AWSService awsService;


    // presigned url 생성
    @GetMapping("/url-generate")
    public String generatePresignedUrl(@RequestParam String billUid, @RequestParam String extension) {
        return awsService.generatePreSignUrl(billUid + "." + extension, bucketName);
    }
}
