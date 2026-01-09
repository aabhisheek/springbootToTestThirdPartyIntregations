# AWS S3 File Storage - Testing Guide

This guide will help you test the AWS S3 file storage integration in your Spring Boot application.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [AWS S3 Setup](#aws-s3-setup)
3. [Configuration](#configuration)
4. [Testing with Postman](#testing-with-postman)
5. [API Endpoints Reference](#api-endpoints-reference)
6. [Common Issues & Solutions](#common-issues--solutions)
7. [Use Cases](#use-cases)

---

## Prerequisites

✅ **What you need:**
- Active AWS account (Free tier is sufficient)
- AWS Access Key ID and Secret Access Key
- S3 bucket created in your AWS account
- Postman installed
- Application running on `http://localhost:8080`

---

## AWS S3 Setup

### Step 1: Create an S3 Bucket

1. **Login to AWS Console**: https://console.aws.amazon.com/
2. **Navigate to S3**: Search for "S3" in the services search bar
3. **Create Bucket**:
   - Click **"Create bucket"**
   - **Bucket name**: Choose a unique name (e.g., `my-app-storage-2026`)
   - **Region**: Select your preferred region (e.g., `eu-north-1`)
   - **Block Public Access**: Keep all blocked (recommended for security)
   - Click **"Create bucket"**

### Step 2: Get AWS Credentials

You can use the same credentials as AWS SES, or create new ones:

**Option A: Use existing SES credentials**
- Use the same Access Key ID and Secret Access Key you use for AWS SES

**Option B: Create new IAM user for S3**

1. **Navigate to IAM**: https://console.aws.amazon.com/iam/
2. **Create User**:
   - Go to **Users** → **Create user**
   - Username: `s3-storage-user`
   - Click **Next**
3. **Set Permissions**:
   - Select **"Attach policies directly"**
   - Search and select: **AmazonS3FullAccess**
   - Click **Next** → **Create user**
4. **Create Access Key**:
   - Click on the created user
   - Go to **Security credentials** tab
   - Click **Create access key**
   - Select **"Application running on AWS"** or **"Local code"**
   - Copy **Access Key ID** and **Secret Access Key**
   - ⚠️ **Important**: Save these credentials securely!

---

## Configuration

### Step 3: Configure Application Properties

1. **Open**: `src/main/resources/application-local.properties`

2. **Add S3 Configuration**:

```properties
# AWS S3 Configuration
aws.s3.region=eu-north-1
aws.s3.bucket-name=my-app-storage-2026
aws.s3.access-key=YOUR_AWS_ACCESS_KEY_ID
aws.s3.secret-key=YOUR_AWS_SECRET_ACCESS_KEY
```

3. **Replace with your actual values**:
   - `aws.s3.region`: Your S3 bucket region (e.g., `eu-north-1`, `us-east-1`, `ap-south-1`)
   - `aws.s3.bucket-name`: Your S3 bucket name
   - `aws.s3.access-key`: Your AWS Access Key ID
   - `aws.s3.secret-key`: Your AWS Secret Access Key

4. **Save the file** (This file is git-ignored, so your credentials are safe)

### Step 4: Start the Application

```bash
# Build the application
./gradlew clean build -x test

# Run the application
./gradlew bootRun
```

**Or on Windows:**
```powershell
.\gradlew.bat clean build -x test
.\gradlew.bat bootRun
```

Wait for the application to start. You should see:
```
Started EmailDemoApplication in X.XXX seconds
```

---

## Testing with Postman

### Import the Collection

1. **Open Postman**
2. **Import Collection**:
   - Click **Import** button
   - Select **File** tab
   - Choose `Postman-Collection-With-PDF.json`
   - Click **Import**
3. **Navigate to**: `Storage APIs (AWS S3)` folder

---

## API Endpoints Reference

### 1️⃣ Upload Single File

**Endpoint**: `POST /api/storage/upload`

**How to test**:
1. Select **"Upload Single File to S3"** request
2. Go to **Body** tab
3. Click on **"Select File"** next to `file` field
4. Choose any file (PDF, image, document, etc.)
5. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "fileName": "invoice.pdf",
    "fileUrl": "https://my-app-storage-2026.s3.eu-north-1.amazonaws.com/1736413200000_a1b2c3d4.pdf",
    "fileKey": "1736413200000_a1b2c3d4.pdf",
    "fileSize": 102400,
    "contentType": "application/pdf",
    "message": "File uploaded successfully"
  },
  "timestamp": "2026-01-09T12:30:00"
}
```

**Important**: Save the `fileKey` from the response! You'll need it for other operations.

---

### 2️⃣ Upload Multiple Files

**Endpoint**: `POST /api/storage/upload/multiple`

**How to test**:
1. Select **"Upload Multiple Files to S3"** request
2. Go to **Body** tab
3. You'll see multiple `files` fields
4. Select a different file for each field
5. Add more `files` fields if needed:
   - Click **"Bulk Edit"** → switch back to **"Form Data"**
   - Click **"+"** to add more fields
6. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "Uploaded 3/3 files successfully",
  "data": {
    "file1.pdf": {
      "fileName": "file1.pdf",
      "fileUrl": "https://...",
      "fileKey": "1736413200000_a1b2c3d4.pdf",
      ...
    },
    "file2.jpg": {
      "fileName": "file2.jpg",
      "fileUrl": "https://...",
      "fileKey": "1736413205000_b2c3d4e5.jpg",
      ...
    },
    "totalFiles": 3,
    "successCount": 3,
    "failedCount": 0
  },
  "timestamp": "2026-01-09T12:35:00"
}
```

---

### 3️⃣ List All Files

**Endpoint**: `GET /api/storage/files`

**How to test**:
1. Select **"List All Files in S3"** request
2. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "Files retrieved successfully",
  "data": {
    "files": [
      {
        "fileName": "invoice.pdf",
        "fileKey": "1736413200000_a1b2c3d4.pdf",
        "fileUrl": "https://my-app-storage-2026.s3.eu-north-1.amazonaws.com/1736413200000_a1b2c3d4.pdf",
        "size": 102400,
        "lastModified": "2026-01-09T12:30:00.000+00:00",
        "contentType": "application/pdf"
      },
      {
        "fileName": "report.xlsx",
        "fileKey": "1736413300000_b2c3d4e5.xlsx",
        "fileUrl": "https://...",
        "size": 256000,
        "lastModified": "2026-01-09T12:32:00.000+00:00",
        "contentType": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
      }
    ],
    "totalFiles": 2,
    "bucketName": "my-app-storage-2026"
  },
  "timestamp": "2026-01-09T12:40:00"
}
```

---

### 4️⃣ Get File Metadata

**Endpoint**: `GET /api/storage/metadata/{fileKey}`

**How to test**:
1. Select **"Get File Metadata"** request
2. **Replace** `{fileKey}` in the URL with your actual file key
   - Example: `http://localhost:8080/api/storage/metadata/1736413200000_a1b2c3d4.pdf`
3. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "File metadata retrieved successfully",
  "data": {
    "fileName": "invoice.pdf",
    "fileKey": "1736413200000_a1b2c3d4.pdf",
    "fileUrl": "https://my-app-storage-2026.s3.eu-north-1.amazonaws.com/1736413200000_a1b2c3d4.pdf",
    "size": 102400,
    "lastModified": "2026-01-09T12:30:00.000+00:00",
    "contentType": "application/pdf"
  },
  "timestamp": "2026-01-09T12:45:00"
}
```

---

### 5️⃣ Download File

**Endpoint**: `GET /api/storage/download/{fileKey}`

**How to test**:
1. Select **"Download File from S3"** request
2. **Replace** `{fileKey}` with your actual file key
3. Click **Send**
4. The file will be downloaded to your system

**Note**: Postman will show the file content or download it automatically.

---

### 6️⃣ Generate Presigned URL (Temporary Access)

**Endpoint**: `GET /api/storage/presigned-url/{fileKey}?minutes=60`

**What is a Presigned URL?**
A temporary URL that allows anyone to access your file for a limited time without AWS credentials.

**How to test**:
1. Select **"Generate Presigned URL"** request
2. **Replace** `{fileKey}` with your actual file key
3. **Optional**: Change `minutes` parameter (default: 60)
   - Example: `?minutes=30` for 30-minute expiration
4. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "Presigned URL generated successfully",
  "data": {
    "fileKey": "1736413200000_a1b2c3d4.pdf",
    "presignedUrl": "https://my-app-storage-2026.s3.eu-north-1.amazonaws.com/1736413200000_a1b2c3d4.pdf?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=...&X-Amz-Expires=3600...",
    "expiresInMinutes": "60"
  },
  "timestamp": "2026-01-09T12:50:00"
}
```

**Test the presigned URL**:
- Copy the `presignedUrl` from the response
- Open it in your browser
- The file will be accessible for the specified duration
- After expiration, the URL will stop working

---

### 7️⃣ Check if File Exists

**Endpoint**: `GET /api/storage/exists/{fileKey}`

**How to test**:
1. Select **"Check if File Exists"** request
2. **Replace** `{fileKey}` with your actual file key
3. Click **Send**

**Expected Response (File exists)**:
```json
{
  "success": true,
  "message": "File exists",
  "data": {
    "exists": true
  },
  "timestamp": "2026-01-09T12:55:00"
}
```

**Expected Response (File doesn't exist)**:
```json
{
  "success": true,
  "message": "File does not exist",
  "data": {
    "exists": false
  },
  "timestamp": "2026-01-09T12:56:00"
}
```

---

### 8️⃣ Delete File

**Endpoint**: `DELETE /api/storage/delete/{fileKey}`

**How to test**:
1. Select **"Delete File from S3"** request
2. **Replace** `{fileKey}` with your actual file key
3. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "File deleted successfully: 1736413200000_a1b2c3d4.pdf",
  "data": "1736413200000_a1b2c3d4.pdf",
  "timestamp": "2026-01-09T13:00:00"
}
```

**Verify deletion**:
- Use the **"List All Files"** endpoint
- The deleted file should not appear in the list

---

### 9️⃣ Health Check

**Endpoint**: `GET /api/storage/health`

**How to test**:
1. Select **"Storage Health Check"** request
2. Click **Send**

**Expected Response**:
```json
{
  "success": true,
  "message": "Storage service is running",
  "data": "OK",
  "timestamp": "2026-01-09T13:05:00"
}
```

---

## Common Issues & Solutions

### ❌ Error: "The specified bucket does not exist"

**Cause**: Bucket name in configuration doesn't match actual bucket name

**Solution**:
1. Check your bucket name in AWS S3 Console
2. Update `aws.s3.bucket-name` in `application-local.properties`
3. Restart the application

---

### ❌ Error: "Access Denied"

**Cause**: AWS credentials are invalid or don't have S3 permissions

**Solution**:
1. Verify AWS Access Key ID and Secret Access Key
2. Check IAM user has `AmazonS3FullAccess` policy attached
3. Ensure credentials are correctly set in `application-local.properties`
4. Restart the application

---

### ❌ Error: "The bucket is in this region: us-east-1"

**Cause**: Bucket region in configuration doesn't match actual bucket region

**Solution**:
1. Check your bucket region in AWS S3 Console (shown next to bucket name)
2. Update `aws.s3.region` in `application-local.properties`
3. Restart the application

---

### ❌ Error: "Failed to upload file: Connection timeout"

**Cause**: Network issues or AWS service unavailable

**Solution**:
1. Check your internet connection
2. Verify AWS region is correct
3. Try a different AWS region (create new bucket if needed)
4. Check AWS Service Health Dashboard

---

### ❌ Error: "File is empty"

**Cause**: No file selected in Postman or file is actually empty

**Solution**:
1. Ensure you've selected a file in Postman's Body tab
2. Check the file is not empty (has content)
3. Try a different file

---

## Use Cases

### 🎯 Use Case 1: Store User Uploads
**Scenario**: Users upload profile pictures, documents, or files

**Flow**:
1. User uploads file → `POST /api/storage/upload`
2. Store `fileKey` and `fileUrl` in your database
3. Display file using `fileUrl` or generate presigned URL

---

### 🎯 Use Case 2: Generate and Store PDFs
**Scenario**: Generate invoice PDFs after payment and store them

**Flow**:
1. Payment successful → Generate invoice PDF
2. Upload PDF to S3 → `uploadFileFromBytes()` in S3Service
3. Send email with PDF attachment or link
4. Store `fileKey` in order record

**Example Integration**:
```java
// In your service class
byte[] pdfData = generateInvoicePDF(order);
FileUploadResponse response = s3Service.uploadFileFromBytes(
    pdfData, 
    "invoice_" + orderId + ".pdf",
    "application/pdf"
);
```

---

### 🎯 Use Case 3: Temporary File Sharing
**Scenario**: Share files with customers for limited time

**Flow**:
1. Upload file → `POST /api/storage/upload`
2. Generate presigned URL → `GET /api/storage/presigned-url/{fileKey}?minutes=1440`
3. Share URL with customer (expires in 24 hours)

**Example**:
- Customer requests invoice
- Generate presigned URL valid for 24 hours
- Send URL via email
- After 24 hours, URL automatically expires

---

### 🎯 Use Case 4: File Management Dashboard
**Scenario**: Admin panel to manage uploaded files

**Flow**:
1. List all files → `GET /api/storage/files`
2. View file details → `GET /api/storage/metadata/{fileKey}`
3. Delete unwanted files → `DELETE /api/storage/delete/{fileKey}`

---

## Testing Checklist

Complete this checklist to verify S3 integration works correctly:

- [ ] ✅ AWS S3 bucket created
- [ ] ✅ AWS credentials configured in `application-local.properties`
- [ ] ✅ Application starts without errors
- [ ] ✅ Health check returns "OK"
- [ ] ✅ Single file upload successful
- [ ] ✅ Multiple files upload successful
- [ ] ✅ List files shows uploaded files
- [ ] ✅ Get file metadata returns correct info
- [ ] ✅ Download file works
- [ ] ✅ Presigned URL generated and accessible
- [ ] ✅ File exists check returns true for uploaded file
- [ ] ✅ File exists check returns false for non-existent file
- [ ] ✅ Delete file removes file from S3
- [ ] ✅ List files no longer shows deleted file

---

## AWS Free Tier Limits

**AWS S3 Free Tier (First 12 months)**:
- ✅ **5 GB** of standard storage
- ✅ **20,000** GET requests per month
- ✅ **2,000** PUT requests per month
- ✅ **100 GB** data transfer out per month

**After 12 months or exceeding free tier**:
- Storage: ~$0.023 per GB/month
- PUT requests: ~$0.005 per 1,000 requests
- GET requests: ~$0.0004 per 1,000 requests

**💡 Tip**: S3 is very cost-effective. Even with moderate usage, monthly costs are typically under $1.

---

## Security Best Practices

### 🔐 Credentials Security
✅ **DO**:
- Store credentials in `application-local.properties` (git-ignored)
- Use IAM users with minimal required permissions
- Rotate access keys regularly

❌ **DON'T**:
- Commit credentials to Git
- Use root account credentials
- Share credentials publicly

### 🔐 Bucket Security
✅ **DO**:
- Keep "Block all public access" enabled
- Use presigned URLs for temporary access
- Enable S3 bucket versioning for important files
- Enable S3 server-side encryption

❌ **DON'T**:
- Make buckets publicly accessible
- Store sensitive data unencrypted
- Use predictable file names for sensitive files

---

## Next Steps

✅ **S3 storage is ready!**

### What you can do next:

1. **Integrate with Email Service**:
   - Upload invoice to S3
   - Send email with S3 file URL
   - Use presigned URLs for secure access

2. **Integrate with Payments**:
   - Generate invoice PDF after payment
   - Upload to S3
   - Store fileKey in order record

3. **Build a File Gallery**:
   - List all images
   - Display thumbnails
   - Allow download/delete

4. **Add PDF Generation** (iText library):
   - Generate invoices
   - Create reports
   - Upload to S3 automatically

---

## Support

**Need help?**
- Check the logs in the terminal
- Verify AWS console shows the bucket and files
- Review this guide for common issues
- Check AWS S3 documentation: https://docs.aws.amazon.com/s3/

**Happy Testing! 🚀**
