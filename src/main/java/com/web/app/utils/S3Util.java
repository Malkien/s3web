package com.web.app.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
public class S3Util {
    private static final String BUCKET_NAME = "my-bucket-practice";
    private static final S3Client s3= S3Client.builder().credentialsProvider(InstanceProfileCredentialsProvider.builder().build()).build();
    //private static final S3TransferManager transferManager = S3TransferManager.create();

    public static String uploadFile(String key, MultipartFile file) throws RuntimeException{
        try{
            RDSUtils.insertData(key, file.getInputStream());

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(),file.getSize()));

            // if you want to get all the metadata from the s3 object
            // HeadObjectRequest headObjectRequest= HeadObjectRequest.builder().bucket(BUCKET_NAME).key(key).build();
            // Map<String, String> headObjectResponse= s3.headObject(headObjectRequest).metadata();
            return "Image Upload";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<S3Object> listFile(){
        try{
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(BUCKET_NAME)
                    .build();
            ListObjectsResponse res = s3.listObjects(listObjects);
            return res.contents();
        } catch (AwsServiceException e) {
            throw new RuntimeException(e);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }
}
