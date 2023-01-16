package com.web.app.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class S3Util {
    private static final String BUCKET_NAME = "my-bucket-practice";
    private static final S3Client s3= S3Client.builder().credentialsProvider(InstanceProfileCredentialsProvider.builder().build()).build();
    private static final S3TransferManager transferManager = S3TransferManager.create();
    public static String uploadFile(String name, MultipartFile file){
        try{
            UploadFileRequest uploadFileRequest =
                    UploadFileRequest.builder()
                            .putObjectRequest(b -> b.bucket(BUCKET_NAME).key(name))
                            .addTransferListener(LoggingTransferListener.create())
                            .source(file.getResource().getFile())
                            .build();
            FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

            CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
            return uploadResult.response().eTag();
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
