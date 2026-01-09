package com.example.emaildemo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.emaildemo.dto.FileMetadata;
import com.example.emaildemo.dto.FileUploadResponse;
import com.example.emaildemo.dto.S3FileListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    /**
     * Upload a file to S3
     */
    public FileUploadResponse uploadFile(MultipartFile file) {
        try {
            // Generate unique file key
            String originalFilename = file.getOriginalFilename();
            String fileKey = generateUniqueFileName(originalFilename);

            // Prepare metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Upload to S3
            amazonS3.putObject(new PutObjectRequest(
                    bucketName,
                    fileKey,
                    file.getInputStream(),
                    metadata
            ));

            // Generate file URL
            String fileUrl = amazonS3.getUrl(bucketName, fileKey).toString();

            log.info("File uploaded successfully: {} to bucket: {}", fileKey, bucketName);

            return new FileUploadResponse(
                    originalFilename,
                    fileUrl,
                    fileKey,
                    file.getSize(),
                    file.getContentType(),
                    "File uploaded successfully"
            );

        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Upload file from byte array (useful for generated files like PDFs, images)
     */
    public FileUploadResponse uploadFileFromBytes(byte[] fileData, String fileName, String contentType) {
        try {
            String fileKey = generateUniqueFileName(fileName);

            // Prepare metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileData.length);
            metadata.setContentType(contentType);

            // Upload to S3
            InputStream inputStream = new ByteArrayInputStream(fileData);
            amazonS3.putObject(new PutObjectRequest(
                    bucketName,
                    fileKey,
                    inputStream,
                    metadata
            ));

            String fileUrl = amazonS3.getUrl(bucketName, fileKey).toString();

            log.info("File uploaded from bytes: {} to bucket: {}", fileKey, bucketName);

            return new FileUploadResponse(
                    fileName,
                    fileUrl,
                    fileKey,
                    (long) fileData.length,
                    contentType,
                    "File uploaded successfully"
            );

        } catch (Exception e) {
            log.error("Error uploading file from bytes to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Download a file from S3
     */
    public S3Object downloadFile(String fileKey) {
        try {
            log.info("Downloading file: {} from bucket: {}", fileKey, bucketName);
            return amazonS3.getObject(bucketName, fileKey);
        } catch (Exception e) {
            log.error("Error downloading file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to download file: " + e.getMessage());
        }
    }

    /**
     * Delete a file from S3
     */
    public String deleteFile(String fileKey) {
        try {
            amazonS3.deleteObject(bucketName, fileKey);
            log.info("File deleted successfully: {} from bucket: {}", fileKey, bucketName);
            return "File deleted successfully: " + fileKey;
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    /**
     * List all files in the bucket
     */
    public S3FileListResponse listFiles() {
        try {
            ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
            List<S3ObjectSummary> objects = result.getObjectSummaries();

            List<FileMetadata> fileMetadataList = new ArrayList<>();
            for (S3ObjectSummary os : objects) {
                String fileUrl = amazonS3.getUrl(bucketName, os.getKey()).toString();
                
                FileMetadata metadata = new FileMetadata(
                        extractFileName(os.getKey()),
                        os.getKey(),
                        fileUrl,
                        os.getSize(),
                        os.getLastModified(),
                        getContentType(os.getKey())
                );
                fileMetadataList.add(metadata);
            }

            log.info("Listed {} files from bucket: {}", fileMetadataList.size(), bucketName);

            return new S3FileListResponse(
                    fileMetadataList,
                    fileMetadataList.size(),
                    bucketName
            );

        } catch (Exception e) {
            log.error("Error listing files from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to list files: " + e.getMessage());
        }
    }

    /**
     * Generate a presigned URL for temporary access
     */
    public String generatePresignedUrl(String fileKey, int expirationMinutes) {
        try {
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += (long) expirationMinutes * 60 * 1000;
            expiration.setTime(expTimeMillis);

            URL url = amazonS3.generatePresignedUrl(bucketName, fileKey, expiration);
            log.info("Generated presigned URL for: {} expires in {} minutes", fileKey, expirationMinutes);
            return url.toString();

        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage());
            throw new RuntimeException("Failed to generate presigned URL: " + e.getMessage());
        }
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String fileKey) {
        try {
            return amazonS3.doesObjectExist(bucketName, fileKey);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get file metadata
     */
    public FileMetadata getFileMetadata(String fileKey) {
        try {
            ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, fileKey);
            String fileUrl = amazonS3.getUrl(bucketName, fileKey).toString();

            return new FileMetadata(
                    extractFileName(fileKey),
                    fileKey,
                    fileUrl,
                    metadata.getContentLength(),
                    metadata.getLastModified(),
                    metadata.getContentType()
            );

        } catch (Exception e) {
            log.error("Error getting file metadata: {}", e.getMessage());
            throw new RuntimeException("Failed to get file metadata: " + e.getMessage());
        }
    }

    // Helper methods
    private String generateUniqueFileName(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        return timestamp + "_" + uuid + extension;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String extractFileName(String fileKey) {
        // If fileKey has timestamp_uuid_originalname.ext format, extract original name
        // Otherwise return fileKey as is
        if (fileKey.contains("_")) {
            String[] parts = fileKey.split("_", 3);
            if (parts.length >= 3) {
                return parts[2];
            }
        }
        return fileKey;
    }

    private String getContentType(String fileKey) {
        String extension = getFileExtension(fileKey).toLowerCase();
        switch (extension) {
            case ".pdf":
                return "application/pdf";
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".txt":
                return "text/plain";
            case ".doc":
                return "application/msword";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }
}
