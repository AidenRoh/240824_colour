package com.colour.files;

import io.minio.MinioAsyncClient;

public class minio {

    public void example() {
        MinioAsyncClient client = MinioAsyncClient.builder()
                .endpoint("https://play.min.io")
                .credentials("1234", "12345")
                .build();
    }
}
