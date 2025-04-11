package com.colour.files.service;

import com.colour.files.domain.VideoResolution;
import com.colour.files.repository.MinIoRepository;
import io.minio.http.Method;
import jakarta.annotation.PreDestroy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.colour.files.domain.BucketType.VIDEO;
import static com.colour.files.domain.BucketType.getValidBucket;

public class PostUploadService {

    private final MinIoRepository repository;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public PostUploadService(MinIoRepository repository) {
        this.repository = repository;
    }

    public void proceedThumbnailUpload(String minioPath, String fileType, Integer thumbnailTime) {
        executor.submit(() -> {
            try {
                createThumbnail(minioPath, fileType, thumbnailTime);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause()); //TODO: 실패시 다시 시도해주십시오 같은 로직이 필요함.
            }
        });
    }

    public void proceedTranscode(String filePath, String fileType) {
        if (getValidBucket(fileType).equals(VIDEO.getBucket())) {
            executor.submit(() -> {

            });
        }
    }

    // metadata extraction
    private void saveTranscodingFootage(String minioPath, VideoResolution resolution) {
        repository.initializeTranscodingBucket();


    }

    // thumbnail creation
    private void createThumbnail(String minioPath, String fileType, Integer thumbnailTime) throws IOException, InterruptedException {
        repository.initializeThumbnailBucket();
        String timeStamp = getThumbnailTime(thumbnailTime);
        String presignedUrl = repository.IssuePresignedUrl(minioPath, fileType, Method.GET);
        Path thumbnailPath = Files.createTempFile("thumbnail-", ".jpg");

        ProcessBuilder ffmpegBuilder = new ProcessBuilder(
                "ffmpeg", "-i", presignedUrl,
                "-ss", timeStamp,
                "-vframes", "1",
                "-vf", "scale=320:-1", // 썸네일 사이즈 또한 defualt value 따로 관리하기
                thumbnailPath.toString()
        );

        ffmpegBuilder.redirectErrorStream(true);
        Process ffmpegProcess = ffmpegBuilder.start();
        ffmpegProcess.waitFor();

        try (InputStream is = new FileInputStream(thumbnailPath.toFile())) {
            repository.uploadThumbnail(minioPath, is, thumbnailPath);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        } finally {
            Files.deleteIfExists(thumbnailPath);
        }
    }

    private String getThumbnailTime(Integer thumbnailTime) {
        if (thumbnailTime == null) {
            return "00:00:01";
        }
        int hours = thumbnailTime / 3600;
        int minutes = (thumbnailTime % 3600) / 60;
        int seconds = thumbnailTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private VideoResolution getVideoResolution(String minioPath) throws IOException {
        ProcessBuilder ffprobeBuilder = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "default=noprint_wrappers=1:nokey=1",
                minioPath
        );

        Process ffprobeProcess = ffprobeBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()));
        int width = Integer.parseInt(br.readLine());
        int height = Integer.parseInt(br.readLine());
        return new VideoResolution(width, height);
    }

    @PreDestroy
    public void destroy() {
        executor.shutdownNow();
    }

}
