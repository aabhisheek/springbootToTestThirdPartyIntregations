package com.example.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
    
    private String fileName;
    private String contentType; // e.g., "application/pdf"
    private byte[] content; // File content as byte array
    private String base64Content; // Alternative: base64 encoded string
    
    public byte[] getFileContent() {
        if (content != null && content.length > 0) {
            return content;
        }
        if (base64Content != null && !base64Content.isEmpty()) {
            return java.util.Base64.getDecoder().decode(base64Content);
        }
        return new byte[0];
    }
}
