package com.realhedin.s3.examples;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Paths;

public class S3PutObjectInS3 {

    private static S3Client s3Client;

    public static void main(String[] args) {

        Region region = Region.US_EAST_2;
        s3Client = S3Client.builder().region(region).build();

        String bucket = "testbucket1573756687081";

        PutObjectResponse putObjectResponse = s3Client.putObject(PutObjectRequest.builder()
                .key("syncTxtFile2")
                .bucket(bucket)
                .build(), Paths.get("gradesCSV2.csv"));
        System.out.println(putObjectResponse.eTag());

    }
}
