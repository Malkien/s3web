package com.web.app.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Util {
    private static final String BUCKET_NAME = "my-bucket-practice";
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
}
