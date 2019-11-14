package com.realhedin.s3.examples;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Random;

public class S3WorkWithObjects {

    private static S3Client s3Client;

    public static void main(String[] args) {

        Region region = Region.US_EAST_2;
        s3Client = S3Client.builder().region(region).build();

        String bucket = "testbucket1573756687081";

        //create bucket
//        CreateBucketResponse bucket1 = createBucket(bucket, region);

        //create and put an object in it
        String key = "key1";
        String value = "This is a test object into S3 storage";
        byte[] array = {Integer.valueOf(123454).byteValue(), Integer.valueOf(123).byteValue()};
//        PutObjectResponse putObjectResponse = putObjectIntoS3(bucket, key, array);
//        PutObjectResponse putObjectResponse = putObjectIntoS3(bucket, key, value);
//        PutObjectResponse putObjectResponse = putObjectIntoS3(bucket, key,
//                Paths.get("C:\\Users\\Dmitrii_Korolev\\Projects\\AWS\\AWS_SDK_test\\ReadMe.md"));

        //read object from bucket
        //save to file
//        GetObjectResponse fromS3File = s3Client.getObject(
//                GetObjectRequest.builder()
//                        .bucket(bucket)
//                        .key(key)
//                        .build(),
//                ResponseTransformer.toFile(Paths.get("fromS3File")));
//        System.out.println(fromS3File.toString());
        //save as bytes
//        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build());
//        System.out.println(objectAsBytes.asUtf8String());
//        ResponseBytes<GetObjectResponse> test = s3Client.getObjectAsBytes(
//                GetObjectRequest.builder().bucket(bucket).key("test.txt").build()
//        );
//        System.out.println(test.asUtf8String());


        //upload object in multipart files
        //create multipart upload and uploadId
        CreateMultipartUploadRequest multipartRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        CreateMultipartUploadResponse multipartUploadResponse = s3Client.createMultipartUpload(multipartRequest);
        String uploadId = multipartUploadResponse.uploadId();
        System.out.println(uploadId);
        //upload all parts of the object
        UploadPartRequest uploadRequest1 = UploadPartRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .partNumber(1)
                .build();
        String eTag1 = s3Client.uploadPart(uploadRequest1, RequestBody.fromByteBuffer(getRandomByteBuffer(5 * 1024 * 1024))).eTag();
        CompletedPart part1 = CompletedPart.builder()
                .partNumber(1)
                .eTag(eTag1)
                .build();
        UploadPartRequest uploadRequest2 = UploadPartRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .partNumber(2)
                .build();
        String eTag2 = s3Client.uploadPart(uploadRequest2, RequestBody.fromByteBuffer(getRandomByteBuffer(3 * 1024 * 1024))).eTag();
        CompletedPart part2 = CompletedPart.builder()
                .partNumber(2)
                .eTag(eTag2)
                .build();
        // call completeMultipartUpload to merge all parts
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(part1, part2)
                .build();
        CompleteMultipartUploadRequest completedMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .build();
        s3Client.completeMultipartUpload(completedMultipartUploadRequest);


        //delete object from bucket
//        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(
//                DeleteObjectRequest.builder().bucket(bucket).key(key).build()
//        );


    }


    //test method helpers
    private static ByteBuffer getRandomByteBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }

    private static PutObjectResponse putObjectIntoS3(String bucket, String key, byte[] array) {
        return s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build(), RequestBody.fromByteBuffer(ByteBuffer.wrap(array)));
    }

    private static PutObjectResponse putObjectIntoS3(String bucket, String key, String value) {
        return s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build(), RequestBody.fromString(value));
    }

    private static PutObjectResponse putObjectIntoS3(String bucket, String key, Path path) {
        return s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build(), RequestBody.fromFile(path));
    }

    private static CreateBucketResponse createBucket(String bucketName, Region region) {
        return s3Client.createBucket(
                CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .createBucketConfiguration(
                                CreateBucketConfiguration.builder()
                                        .locationConstraint(region.id())
                                        .build()
                        )
                        .build()
        );
    }

}
