package com.colour.files.controller;

import com.colour.files.domain.dto.UploadNotifyRequest;
import com.colour.files.service.FileUploadService;
import com.colour.files.service.PostUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {

    FileUploadService fileUploadService;
    PostUploadService postUploadService;


    @GetMapping("uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam String fileName) {
        String issuedUrl = fileUploadService.getPresignedUrl(fileName, getFileType(fileName));
        return ResponseEntity.ok(issuedUrl);
    }

    @PostMapping("notify")
    public ResponseEntity<String> notifyCompletion(@RequestBody UploadNotifyRequest uploadNotifyRequest,
                                                   @RequestParam(required = false) Integer thumbnailTime) {
        //TODO: post db 에 파일 링크 저장 / 썸네일 생성 /
        return ResponseEntity.ok("successfully uploaded");

    }


    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
