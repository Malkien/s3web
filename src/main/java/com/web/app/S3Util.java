package com.web.app;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Service
public class S3Util {
    private static final String BUCKET_NAME = "my-bucket-practice";
    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
    public static void uploadFile(String name, MultipartFile file){
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            s3.putObject(BUCKET_NAME, name, file.getInputStream(), metadata);
        }catch (AmazonServiceException e){
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
