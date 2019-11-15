package com.realhedin.s3.examples;

import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class S3AsyncObjectUsage {

    private static S3AsyncClient s3AsyncClient;

    public static void main(String[] args) {

        Region region = Region.US_EAST_2;
        s3AsyncClient = S3AsyncClient.builder().region(region).build();

        String bucket = "testbucket1573756687081";
        String key = "keyAsync3";


        //put object into S3 in asynchronous way
        CompletableFuture<PutObjectResponse> s3PutFileFuture = s3AsyncClient.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                AsyncRequestBody.fromFile(Paths.get("s3File"))
        );

        s3PutFileFuture.whenComplete((resp, err) -> {
            System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
            if (resp != null) {
                System.out.println("response: " + resp);
            } else {
                System.err.println("error message: " + err);
            }
            System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
        });


        //get object in asynchronous way
        CompletableFuture<GetObjectResponse> s3GetFileFuture = s3AsyncClient.getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key("key1")
                        .build(),
                AsyncResponseTransformer.toFile(Paths.get("key1"))
        );
        s3GetFileFuture.whenComplete((resp, err) -> {
            System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
            try {
                if (resp != null) {
                    System.out.println("Response from getObject: " + resp);
                } else {
                    System.err.println("Error from getObject: " + err);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
                s3AsyncClient.close();
            }
        });

        System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
        s3PutFileFuture.join();
        System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());
        s3GetFileFuture.join();
        System.out.println(Thread.currentThread().getName() +" : " +System.currentTimeMillis());

    }
}
