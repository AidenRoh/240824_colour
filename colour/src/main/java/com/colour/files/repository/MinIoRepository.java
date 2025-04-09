package com.colour.files.repository;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.http.Method;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public class MinIoRepository {

    MinioAsyncClient client;

    public MinIoRepository() {
        this.client = MinioAsyncClient.builder()
                .endpoint("http://localhost:9001")
                .credentials("colour", "wkrlrhksfl")
                .build();
    }

    public String IssuePresignedUrl(String fileName, String fileType) {
        String targetBucket = "colour_video";
        if (fileType.equals("image")) {
            targetBucket = "colour_image";
        }
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(targetBucket)
                            .object(fileName)
                            .expiry(60 * 10)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Void> initializeVideoBucket() {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket("colour_video").build())
                    .thenCompose(exists -> {
                        if (!exists) {
                            try {
                                return client.makeBucket(MakeBucketArgs.builder().bucket("colour_video").build());
                            } catch (Exception e) {
                                return CompletableFuture.failedFuture(e);
                            }
                        } else {
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private CompletableFuture<Void> initializeImageBucket() {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket("colour_image").build())
                    .thenCompose(exists -> {
                        if (!exists) {
                            try {
                                return client.makeBucket(MakeBucketArgs.builder().bucket("colour_image").build());
                            } catch (Exception e) {
                                return CompletableFuture.failedFuture(e);
                            }
                        } else {
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}
