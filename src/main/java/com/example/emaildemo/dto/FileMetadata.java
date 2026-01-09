package com.example.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
    private String fileName;
    private String fileKey;
    private String fileUrl;
    private Long size;
    private Date lastModified;
    private String contentType;
}
