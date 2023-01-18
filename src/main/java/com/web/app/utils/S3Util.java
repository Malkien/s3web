package com.web.app.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

            s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            //HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
            //        .bucket(BUCKET_NAME)
            //        .key(key)
            //        .build();
            //HeadObjectResponse headObjectResponse = s3.headObject(headObjectRequest);
            //Map<String, String> metadata = headObjectResponse.metadata();
            //RDSUtils.insertData(key, headObjectResponse.metadata());
            return "Image Upload";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {

        }
    }

    public static boolean deleteFile(String key){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
        if(!RDSUtils.delete(key)){
            return false;
        }
        return true;
    }

    public static boolean modifyFile(String key, MultipartFile file){
        boolean delete = deleteFile(key);
        String upload = uploadFile(key, file);
        if(delete && !upload.isEmpty()){
            return true;
        }else{
            return false;
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
