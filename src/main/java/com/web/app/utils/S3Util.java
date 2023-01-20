package com.web.app.utils;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${bucket.name")
    private static String BUCKET_NAME;
    /**
     * CLIENT used to access the bucket
     */
    private static final S3Client s3= S3Client.builder().credentialsProvider(InstanceProfileCredentialsProvider.builder().build()).build();
    //private static final S3TransferManager transferManager = S3TransferManager.create();

    /**
     * Insert the iamge metadata in a database and upload the image to the bucket
     * @param key key od the object
     * @param file the image
     * @return MEssage String if all went ok
     * @throws RuntimeException
     */
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

    /**
     * delete the object in the bucket by the key is provided
     * @param key the key
     * @return is all ok true if error false
     */
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

    /**
     * Modify the image of the object by key (To do this you need to delete and create a new object in bucket because you can't modify an object when is upload)
     * @param key the key
     * @param file the new image
     * @return true if all  ok false if error
     */
    public static boolean modifyFile(String key, MultipartFile file){
        boolean delete = deleteFile(key);
        String upload = uploadFile(key, file);
        if(delete && !upload.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Get a list of al the object in the bucket
     * @return the list of S3Objects
     */
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
