package com.colour.files;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;

public class minio {

    public void example() {
        MinioClient minioClient = MinioAsyncClient.builder()
                .endpoint("https://play.min.io")
                .credentials("1234", "12345")
                .build();

    }
}
