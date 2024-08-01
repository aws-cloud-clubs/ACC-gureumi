package com.goormy.hackathon.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoService {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  /*
  url 생성 함수
   */
  public String getPreSignedUrl(Long prefix, String imageName, HttpMethod httpMethod) {
    String photoPath = String.format("%d%s", prefix, imageName);
    GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket,
        photoPath, httpMethod);
    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName,
      HttpMethod httpMethod) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(httpMethod)
            .withExpiration(getPresignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL,
        CannedAccessControlList.PublicRead.toString());
    return generatePresignedUrlRequest;
  }

  /*
  유효기간 설정
   */
  private Date getPresignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 2;
    expiration.setTime(expTimeMillis);
    return expiration;
  }
}
