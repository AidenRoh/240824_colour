package com.colour.files.service;

import com.colour.files.repository.MinIoRepository;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {

    MinIoRepository repository;

    public String getPresignedUrl(String fileName, String fileType) {
        return repository.IssuePresignedUrl(fileName, fileType);
    }
}
