package com.example.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3FileListResponse {
    private List<FileMetadata> files;
    private int totalFiles;
    private String bucketName;
}
