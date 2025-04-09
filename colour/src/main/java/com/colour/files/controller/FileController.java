package com.colour.files.controller;

import com.colour.files.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {

    FileUploadService service;

    @GetMapping("uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam String fileName, @RequestParam String fileType) {
        String issuedUrl = service.getPresignedUrl(fileName, fileType);
        return ResponseEntity.ok(issuedUrl);
    }

    @PostMapping("notify")
    public ResponseEntity<String> notifyCompletion(@RequestParam String completedFileName) {
        //TODO: post db 에 파일 링크 저장 / 썸네일 생성 /
        return ResponseEntity.ok("successfully uploaded");
    }
}
