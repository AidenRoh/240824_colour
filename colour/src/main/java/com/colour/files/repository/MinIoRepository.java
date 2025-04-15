package com.colour.files.repository;

import com.colour.files.domain.BucketType;
import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@Repository
public class MinIoRepository {

    MinioAsyncClient client;

    public MinIoRepository() {
        this.client = MinioAsyncClient.builder()
                .endpoint("http://localhost:9001")
                .credentials("colour", "wkrlrhksfl")
                .build();
    }

    public void uploadThumbnail(String filePath, InputStream inputStream, Path thumbnailPath)
            throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(BucketType.THUMBNAIL.getBucket())
                        .object(getCurrentMemberId() + "/" + filePath + ".jpg")
                        .stream(inputStream, thumbnailPath.toFile().length(), -1)
                        .contentType("image/jpeg")
                        .build()
        );
    }

    public void uploadTranscodedFootage(String filePath, InputStream inputStream, Path transcodedFootagePath)
            throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(BucketType.TRANSCODE.getBucket())
                        .object(filePath)
                        .stream(inputStream, transcodedFootagePath.toFile().length(), -1)
                        .contentType("")
                        .build()
        );
    }

    public String IssuePresignedUrl(String fileName, String fileType, Method method) {
        String targetBucket = BucketType.getValidBucket(fileType);
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(method)
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

    public CompletableFuture<Void> initializeVideoBucket() {
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

    public CompletableFuture<Void> initializeImageBucket() {
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

    public CompletableFuture<Void> initializeThumbnailBucket() {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket("colour_thumbnail").build())
                    .thenCompose(exists -> {
                        if (!exists) {
                            try {
                                return client.makeBucket(MakeBucketArgs.builder().bucket("colour_thumbnail").build());
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

    public CompletableFuture<Void> initializeTranscodingBucket() {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket("colour_transcode").build())
                    .thenCompose(exists -> {
                        if (!exists) {
                            try {
                                return client.makeBucket(MakeBucketArgs.builder().bucket("colour_transcode").build());
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
