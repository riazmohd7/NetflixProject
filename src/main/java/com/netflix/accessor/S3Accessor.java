package com.netflix.accessor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Repository
public class S3Accessor {

    private String bucketName = "javab5netflixproject";
    @Autowired
    private AwsCredentialsProvider awsCredentialsProvider;

    public String getPreSignedURL(final String path, final int durationInSeconds){
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(awsCredentialsProvider)
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(path)
                .bucket(bucketName)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofSeconds(durationInSeconds))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url().toString();

    }

}
