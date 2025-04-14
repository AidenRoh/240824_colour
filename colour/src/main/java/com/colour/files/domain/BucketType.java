package com.colour.files.domain;

import lombok.Getter;

public enum BucketType {
    IMAGE("colour_image", new String[]{"jpg", "jpeg", "png", "gif"}),
    VIDEO("colour_video", new String[]{"mp4", "mov", "mkv", "avi"}),
    THUMBNAIL("colour_thumbnail", new String[]{"thumb"}),
    TRANSCODE("colour_transcode", new String[]{"transcode"}),
    ;

    @Getter
    private final String bucket;
    private final String[] extensions;

    BucketType(String bucket, String[] extensions) {
        this.bucket = bucket;
        this.extensions = extensions;
    }

    public static String getValidBucket(String extension) {
        for (BucketType eachType : values()) {
            for (String eachExtension : eachType.extensions) {
                if (eachExtension.equalsIgnoreCase(extension)) return eachType.bucket;
            }
        }
        throw new IllegalArgumentException("Invalid extension: " + extension);
    }

}
