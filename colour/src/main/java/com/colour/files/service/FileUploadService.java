package com.colour.files.service;

import com.colour.files.repository.MinIoRepository;
import io.minio.http.Method;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {

    private final MinIoRepository repository;

    public FileUploadService(MinIoRepository repository) {
        this.repository = repository;
    }

    public String getPresignedUrl(String fileName, String fileType) {
        return repository.IssuePresignedUrl(fileName, fileType, Method.PUT);
    }

}
