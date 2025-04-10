package com.colour.files.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadNotifyRequest {
    String bucket;
    String object;
}
