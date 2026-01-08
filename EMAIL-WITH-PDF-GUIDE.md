# 📧 Email with PDF Attachment - Complete Guide

## ✅ Feature Added Successfully!

Your Spring Boot application now supports sending emails with **PDF attachments** using both **AWS SES** and **SendGrid**.

---

## 🎯 New API Endpoints

### 1. **AWS SES with PDF Attachment**
```
POST http://localhost:8080/api/email/send-ses-with-attachment
```

### 2. **SendGrid with PDF Attachment**
```
POST http://localhost:8080/api/email/send-sendgrid-with-attachment
```

### 3. **Generic Endpoint with Provider Selection**
```
POST http://localhost:8080/api/email/send-with-attachment
```

---

## 📮 Postman Request Format

### **Method:** POST

### **Headers:**
```
Content-Type: application/json
```

### **Request Body Structure:**

```json
{
  "to": "recipient@example.com",
  "subject": "Email Subject",
  "body": "<h1>Email Body</h1><p>Your message here</p>",
  "provider": "ses",
  "attachments": [
    {
      "fileName": "invoice.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9UeXBlL..."
    }
  ]
}
```

---

## 📄 Complete Examples

### Example 1: AWS SES - Invoice Email with PDF

**Endpoint:** `POST http://localhost:8080/api/email/send-ses-with-attachment`

**Request Body:**
```json
{
  "to": "customer@example.com",
  "subject": "Your Invoice - Order #12345",
  "body": "<html><body><h2>Invoice Attached</h2><p>Dear Customer,</p><p>Please find your invoice attached to this email.</p><p>Thank you for your business!</p></body></html>",
  "attachments": [
    {
      "fileName": "invoice_12345.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9UeXBlIC9QYWdlcyAvS2lkcyBbNCAwIFJdIC9Db3VudCAxID4+CmVuZG9iago0IDAgb2JqCjw8L1R5cGUgL1BhZ2UgL1BhcmVudCAzIDAgUiAvTWVkaWFCb3ggWzAgMCA2MTIgNzkyXSAvQ29udGVudHMgNSAwIFIgL1Jlc291cmNlcyA8PC9Gb250IDw8L0YxIDcgMCBSPj4+Pj4+CmVuZG9iago1IDAgb2JqCjw8L0xlbmd0aCA0NT4+CnN0cmVhbQpCVAovRjEgMjQgVGYKMTAwIDcwMCBUZAooSGVsbG8gV29ybGQhKSBUagpFVAplbmRzdHJlYW0KZW5kb2JqCjcgMCBvYmoKPDwvVHlwZSAvRm9udCAvU3VidHlwZSAvVHlwZTEgL0Jhc2VGb250IC9UaW1lcy1Sb21hbj4+CmVuZG9iagp4cmVmCjAgOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDAwMDkgMDAwMDAgbiAKMDAwMDAwMDA1OCAwMDAwMCBuIAowMDAwMDAwMTE1IDAwMDAwIG4gCjAwMDAwMDAxNzQgMDAwMDAgbiAKMDAwMDAwMDMwMSAwMDAwMCBuIAowMDAwMDAwMzk1IDAwMDAwIG4gCjAwMDAwMDA0NzQgMDAwMDAgbiAKdHJhaWxlcgo8PC9TaXplIDggL1Jvb3QgMSAwIFI+PgpzdGFydHhyZWYKNTY1CiUlRU9G"
    }
  ]
}
```

---

### Example 2: SendGrid - Report Email with Multiple PDFs

**Endpoint:** `POST http://localhost:8080/api/email/send-sendgrid-with-attachment`

**Request Body:**
```json
{
  "to": "manager@example.com",
  "subject": "Monthly Report - January 2026",
  "body": "<html><body><h2>Monthly Report</h2><p>Hi Team,</p><p>Please find the monthly reports attached.</p><ul><li>Sales Report</li><li>Financial Summary</li></ul><p>Best regards</p></body></html>",
  "attachments": [
    {
      "fileName": "sales_report.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJeLjz9MK..."
    },
    {
      "fileName": "financial_summary.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJcOkw7zD..."
    }
  ]
}
```

---

### Example 3: Generic Endpoint - Choose Provider at Runtime

**Endpoint:** `POST http://localhost:8080/api/email/send-with-attachment`

**Request Body:**
```json
{
  "to": "user@example.com",
  "subject": "Document Attached",
  "body": "<h1>Your Document</h1><p>The requested document is attached.</p>",
  "provider": "sendgrid",
  "attachments": [
    {
      "fileName": "document.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJeLjz9MK..."
    }
  ]
}
```

---

## 🔧 How to Get Base64 Content for PDF

### Method 1: Using Online Tools
1. Go to https://base64.guru/converter/encode/pdf
2. Upload your PDF file
3. Copy the base64 string
4. Paste it in the `base64Content` field

### Method 2: Using PowerShell (Windows)
```powershell
[Convert]::ToBase64String([System.IO.File]::ReadAllBytes("C:\path\to\your\file.pdf"))
```

### Method 3: Using Python
```python
import base64

with open("document.pdf", "rb") as pdf_file:
    encoded_string = base64.b64encode(pdf_file.read()).decode('utf-8')
    print(encoded_string)
```

### Method 4: Using Node.js
```javascript
const fs = require('fs');
const base64 = fs.readFileSync('document.pdf', 'base64');
console.log(base64);
```

### Method 5: Using Java
```java
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

byte[] fileContent = Files.readAllBytes(Paths.get("document.pdf"));
String base64 = Base64.getEncoder().encodeToString(fileContent);
System.out.println(base64);
```

---

## 🧪 Testing in Postman

### Step-by-Step Guide:

1. **Open Postman**

2. **Create New Request**
   - Method: `POST`
   - URL: `http://localhost:8080/api/email/send-ses-with-attachment`

3. **Set Headers**
   - Key: `Content-Type`
   - Value: `application/json`

4. **Add Request Body**
   - Select `Body` tab
   - Choose `raw`
   - Select `JSON` format
   - Paste the JSON with your base64 PDF content

5. **Send Request**

---

## 📋 Sample PDF Base64 (Small Test PDF)

Use this minimal PDF for testing:

```json
{
  "to": "test@example.com",
  "subject": "Test PDF Attachment",
  "body": "<h1>Test Email</h1><p>This is a test email with a PDF attachment.</p>",
  "attachments": [
    {
      "fileName": "test.pdf",
      "contentType": "application/pdf",
      "base64Content": "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9UeXBlIC9QYWdlcyAvS2lkcyBbNCAwIFJdIC9Db3VudCAxID4+CmVuZG9iago0IDAgb2JqCjw8L1R5cGUgL1BhZ2UgL1BhcmVudCAzIDAgUiAvTWVkaWFCb3ggWzAgMCA2MTIgNzkyXSAvQ29udGVudHMgNSAwIFIgL1Jlc291cmNlcyA8PC9Gb250IDw8L0YxIDcgMCBSPj4+Pj4+CmVuZG9iago1IDAgb2JqCjw8L0xlbmd0aCA0NT4+CnN0cmVhbQpCVAovRjEgMjQgVGYKMTAwIDcwMCBUZAooSGVsbG8gV29ybGQhKSBUagpFVAplbmRzdHJlYW0KZW5kb2JqCjcgMCBvYmoKPDwvVHlwZSAvRm9udCAvU3VidHlwZSAvVHlwZTEgL0Jhc2VGb250IC9UaW1lcy1Sb21hbj4+CmVuZG9iagp4cmVmCjAgOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDAwMDkgMDAwMDAgbiAKMDAwMDAwMDA1OCAwMDAwMCBuIAowMDAwMDAwMTE1IDAwMDAwIG4gCjAwMDAwMDAxNzQgMDAwMDAgbiAKMDAwMDAwMDMwMSAwMDAwMCBuIAowMDAwMDAwMzk1IDAwMDAwIG4gCjAwMDAwMDA0NzQgMDAwMDAgbiAKdHJhaWxlcgo8PC9TaXplIDggL1Jvb3QgMSAwIFI+PgpzdGFydHhyZWYKNTY1CiUlRU9G"
    }
  ]
}
```

This creates a simple PDF with "Hello World!" text.

---

## ✅ Expected Response

**Success (200 OK):**
```json
{
  "success": true,
  "message": "Email with 1 attachment(s) sent successfully via AWS SES",
  "data": "ses",
  "timestamp": "2026-01-08T17:30:00"
}
```

**Error (500 Internal Server Error):**
```json
{
  "success": false,
  "message": "Failed to send email with attachments via AWS SES: Invalid credentials",
  "data": null,
  "timestamp": "2026-01-08T17:30:00"
}
```

---

## 📊 All Email Endpoints Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/email/send` | POST | Send simple email (provider optional) |
| `/api/email/send-ses` | POST | Send via AWS SES (no attachment) |
| `/api/email/send-sendgrid` | POST | Send via SendGrid (no attachment) |
| `/api/email/send-with-attachment` | POST | Send with attachments (provider optional) |
| `/api/email/send-ses-with-attachment` | POST | ✨ **NEW** - AWS SES with PDF |
| `/api/email/send-sendgrid-with-attachment` | POST | ✨ **NEW** - SendGrid with PDF |
| `/api/email/health` | GET | Health check |

---

## 🎯 Supported Attachment Types

While optimized for PDF, the implementation supports any file type:

- **PDF**: `application/pdf`
- **Images**: `image/png`, `image/jpeg`, `image/gif`
- **Documents**: `application/msword`, `application/vnd.openxmlformats-officedocument.wordprocessingml.document`
- **Spreadsheets**: `application/vnd.ms-excel`, `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Text**: `text/plain`, `text/csv`
- **ZIP**: `application/zip`

Simply change the `contentType` field to match your file type!

---

## 🔒 Important Notes

1. **File Size Limits:**
   - AWS SES: Maximum 10 MB per email (including all attachments)
   - SendGrid: Maximum 30 MB per email (including all attachments)

2. **Base64 Encoding:**
   - Base64 increases file size by ~33%
   - A 7 MB PDF will be ~9.3 MB when base64 encoded

3. **Email Verification:**
   - AWS SES Sandbox: Both sender and recipient must be verified
   - SendGrid: Sender domain should be verified for production

4. **Multiple Attachments:**
   - Both providers support multiple attachments
   - Add multiple objects to the `attachments` array

---

## 🚀 Quick Test Commands

### Using cURL (Windows CMD):

```cmd
curl -X POST http://localhost:8080/api/email/send-ses-with-attachment ^
  -H "Content-Type: application/json" ^
  -d "{\"to\":\"test@example.com\",\"subject\":\"Test PDF\",\"body\":\"<h1>Test</h1>\",\"attachments\":[{\"fileName\":\"test.pdf\",\"contentType\":\"application/pdf\",\"base64Content\":\"JVBERi0xLjQKJeLjz9MK...\"}]}"
```

### Using PowerShell:

```powershell
$body = @{
    to = "test@example.com"
    subject = "Test PDF"
    body = "<h1>Test Email</h1>"
    attachments = @(
        @{
            fileName = "test.pdf"
            contentType = "application/pdf"
            base64Content = "JVBERi0xLjQKJeLjz9MK..."
        }
    )
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/email/send-ses-with-attachment" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

---

## 🎉 Ready to Use!

Your application now supports sending emails with PDF attachments via both AWS SES and SendGrid!

**Need help?** Check the application logs for detailed error messages.
