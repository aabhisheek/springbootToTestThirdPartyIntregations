package com.example.emaildemo.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.emaildemo.dto.ApiResponse;
import com.example.emaildemo.dto.FileMetadata;
import com.example.emaildemo.dto.FileUploadResponse;
import com.example.emaildemo.dto.S3FileListResponse;
import com.example.emaildemo.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/storage")
@Slf4j
public class StorageController {

    @Autowired
    private S3Service s3Service;

    /**
     * Upload a file to S3
     * POST /api/storage/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        
        try {
            log.info("Received file upload request: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(false, "File is empty", null, LocalDateTime.now())
                );
            }

            FileUploadResponse response = s3Service.uploadFile(file);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "File uploaded successfully", response, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to upload file: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Upload multiple files to S3
     * POST /api/storage/upload/multiple
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files) {
        
        try {
            log.info("Received multiple file upload request: {} files", files.length);

            if (files.length == 0) {
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(false, "No files provided", null, LocalDateTime.now())
                );
            }

            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failedCount = 0;

            for (MultipartFile file : files) {
                try {
                    FileUploadResponse response = s3Service.uploadFile(file);
                    result.put(file.getOriginalFilename(), response);
                    successCount++;
                } catch (Exception e) {
                    result.put(file.getOriginalFilename(), "Failed: " + e.getMessage());
                    failedCount++;
                }
            }

            result.put("totalFiles", files.length);
            result.put("successCount", successCount);
            result.put("failedCount", failedCount);

            String message = String.format("Uploaded %d/%d files successfully", successCount, files.length);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, message, result, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error uploading multiple files: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to upload files: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Download a file from S3
     * GET /api/storage/download/{fileKey}
     */
    @GetMapping("/download/{fileKey}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileKey) {
        try {
            log.info("Received file download request: {}", fileKey);

            S3Object s3Object = s3Service.downloadFile(fileKey);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileKey + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, s3Object.getObjectMetadata().getContentType());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(s3Object.getObjectMetadata().getContentLength())
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a file from S3
     * DELETE /api/storage/delete/{fileKey}
     */
    @DeleteMapping("/delete/{fileKey}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable String fileKey) {
        try {
            log.info("Received file delete request: {}", fileKey);

            String message = s3Service.deleteFile(fileKey);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, message, fileKey, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to delete file: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * List all files in S3 bucket
     * GET /api/storage/files
     */
    @GetMapping("/files")
    public ResponseEntity<ApiResponse<S3FileListResponse>> listFiles() {
        try {
            log.info("Received request to list all files");

            S3FileListResponse response = s3Service.listFiles();

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Files retrieved successfully", response, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error listing files: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to list files: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Get file metadata
     * GET /api/storage/metadata/{fileKey}
     */
    @GetMapping("/metadata/{fileKey}")
    public ResponseEntity<ApiResponse<FileMetadata>> getFileMetadata(@PathVariable String fileKey) {
        try {
            log.info("Received request to get metadata for file: {}", fileKey);

            FileMetadata metadata = s3Service.getFileMetadata(fileKey);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "File metadata retrieved successfully", metadata, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error getting file metadata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to get file metadata: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Generate a presigned URL for temporary file access
     * GET /api/storage/presigned-url/{fileKey}?minutes=60
     */
    @GetMapping("/presigned-url/{fileKey}")
    public ResponseEntity<ApiResponse<Map<String, String>>> generatePresignedUrl(
            @PathVariable String fileKey,
            @RequestParam(defaultValue = "60") int minutes) {
        
        try {
            log.info("Received request to generate presigned URL for: {} (expires in {} minutes)", fileKey, minutes);

            String presignedUrl = s3Service.generatePresignedUrl(fileKey, minutes);

            Map<String, String> response = new HashMap<>();
            response.put("fileKey", fileKey);
            response.put("presignedUrl", presignedUrl);
            response.put("expiresInMinutes", String.valueOf(minutes));

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Presigned URL generated successfully", response, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to generate presigned URL: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Check if file exists
     * GET /api/storage/exists/{fileKey}
     */
    @GetMapping("/exists/{fileKey}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkFileExists(@PathVariable String fileKey) {
        try {
            log.info("Received request to check if file exists: {}", fileKey);

            boolean exists = s3Service.fileExists(fileKey);

            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);

            String message = exists ? "File exists" : "File does not exist";

            return ResponseEntity.ok(
                    new ApiResponse<>(true, message, response, LocalDateTime.now())
            );

        } catch (Exception e) {
            log.error("Error checking file existence: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Failed to check file existence: " + e.getMessage(), null, LocalDateTime.now())
            );
        }
    }

    /**
     * Health check endpoint
     * GET /api/storage/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Storage service is running", "OK", LocalDateTime.now())
        );
    }
}
