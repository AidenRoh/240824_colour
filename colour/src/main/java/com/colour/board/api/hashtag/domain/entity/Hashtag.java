package com.colour.board.api.hashtag.domain.entity;

import lombok.Data;

@Data
public class Hashtag {

    private static long DEFAULT_TAG_FREQUENCY = 1L;

    private Long hashtagId;
    private String hashtag;
    private Long tagFrequency;

    public Hashtag(String hashtag) {
        this.hashtag = hashtag;
        this.tagFrequency = DEFAULT_TAG_FREQUENCY;
    }

    public Hashtag() {
    }
}
