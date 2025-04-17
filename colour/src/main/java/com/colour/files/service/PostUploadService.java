package com.colour.files.service;

import com.colour.files.domain.VideoResolution;
import com.colour.files.repository.MinIoRepository;
import io.minio.http.Method;
import jakarta.annotation.PreDestroy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static com.colour.files.domain.BucketType.VIDEO;
import static com.colour.files.domain.BucketType.getValidBucket;
import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

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

    public void proceedTranscode(String minioPath, String fileType, VideoResolution resolution) throws ExecutionException, InterruptedException {
        repository.initializeTranscodingBucket();
        if (getValidBucket(fileType).equals(VIDEO.getBucket())) {
            String presignedUrl = repository.IssuePresignedUrl(minioPath, fileType, Method.GET);
            List<String[]> targetResolutions = resolution.getValidResolutions();
            List<Future<String>> consumers = new ArrayList<>();

            for (String[] each : targetResolutions) {
                consumers.add(executor.submit(() -> {
                    try {
                        String width = each[0];
                        String height = each[1];
                        String resDir = "./transcoded/" + getCurrentMemberId() + "/" + height;

                        new File(resDir).mkdirs();

                        List<String> command = List.of(
                                "ffmpeg",
                                "-i", presignedUrl,
                                "-vf", "scale=" + width + ":" + height,
                                "-c:a", "aac",
                                "-ar", "48000",
                                "-b:a", "128k",
                                "-c:v", "h264",
                                "-profile:v", "main",
                                "-crf", "20",
                                "-sc_threshold", "0",
                                "-g", "48",
                                "-keyint_min", "48",
                                "-hls_time", "6",
                                "-hls_playlist_type", "vod",
                                "-f", "hls",
                                resDir + "/index.m3u8"
                        );
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.inheritIO();
                        Process process = builder.start();
                        process.waitFor();
                        return resDir;
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            saveTranscodingFootage(consumers);
        }
    }

    // metadata extraction
    private void saveTranscodingFootage(List<Future<String>> consumers) throws ExecutionException, InterruptedException {
        Path baseDir = Paths.get("transcoded");

        for (Future<String> each : consumers) {
            Path resolutionDir = Path.of(each.get());

            try (Stream<Path> files = Files.walk(resolutionDir)) {
                files
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            Path relative = baseDir.relativize(file);
                            String filePath = relative.toString().replace("\\", "/");
                            try (InputStream is = new FileInputStream(file.toFile())) {
                                repository.uploadTranscodedFootage(filePath, is, file);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
            throw new RuntimeException(e);
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
