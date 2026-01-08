# 📎 File Upload Email API - Complete Guide

## ✅ Better Alternative to Headers and Base64!

Instead of sending PDFs in headers (limited size) or base64 in JSON (33% larger), use **proper file upload with multipart/form-data**!

---

## 🎯 New File Upload Endpoints

### 1. **AWS SES with File Upload**
```
POST http://localhost:8080/api/email/send-ses-with-file
Content-Type: multipart/form-data
```

### 2. **SendGrid with File Upload**
```
POST http://localhost:8080/api/email/send-sendgrid-with-file
Content-Type: multipart/form-data
```

### 3. **Generic with File Upload**
```
POST http://localhost:8080/api/email/send-with-file
Content-Type: multipart/form-data
```

---

## 📮 Postman Setup for File Upload

### **Step-by-Step Guide:**

1. **Create New Request in Postman**
   - Method: `POST`
   - URL: `http://localhost:8080/api/email/send-ses-with-file`

2. **Select Body Tab**
   - Choose `form-data` (NOT raw or JSON)

3. **Add Form Fields:**

| Key | Type | Value |
|-----|------|-------|
| `to` | Text | `recipient@example.com` |
| `subject` | Text | `Invoice Attached` |
| `body` | Text | `<h1>Your Invoice</h1><p>Please see attached.</p>` |
| `files` | File | *Click "Select Files" and choose your PDF* |

4. **For Multiple Files:**
   - Add multiple rows with key `files`
   - Each row can have a different file

5. **Send the Request!**

---

## 🖼️ Visual Postman Example

```
POST http://localhost:8080/api/email/send-ses-with-file

Body (form-data):
┌─────────────┬──────┬────────────────────────────────────────┐
│ KEY         │ TYPE │ VALUE                                  │
├─────────────┼──────┼────────────────────────────────────────┤
│ to          │ Text │ customer@example.com                   │
│ subject     │ Text │ Your Invoice                           │
│ body        │ Text │ <h1>Invoice</h1><p>See attached</p>   │
│ files       │ File │ invoice.pdf                    [Select]│
│ files       │ File │ receipt.pdf                    [Select]│
└─────────────┴──────┴────────────────────────────────────────┘
```

---

## 📋 Complete Examples

### Example 1: Single PDF via AWS SES

**Endpoint:** `POST /api/email/send-ses-with-file`

**Form Data:**
- `to`: `customer@example.com`
- `subject`: `Monthly Invoice`
- `body`: `<h2>Invoice Attached</h2><p>Thank you for your business!</p>`
- `files`: `invoice.pdf` (browse and select)

### Example 2: Multiple PDFs via SendGrid

**Endpoint:** `POST /api/email/send-sendgrid-with-file`

**Form Data:**
- `to`: `manager@example.com`
- `subject`: `Reports - Q1 2026`
- `body`: `<h1>Quarterly Reports</h1><p>All reports attached.</p>`
- `files`: `sales_report.pdf` (browse and select)
- `files`: `financial_report.pdf` (browse and select)
- `files`: `summary.pdf` (browse and select)

### Example 3: Choose Provider Dynamically

**Endpoint:** `POST /api/email/send-with-file`

**Form Data:**
- `to`: `user@example.com`
- `subject`: `Document Request`
- `body`: `<p>Your requested documents are attached.</p>`
- `provider`: `sendgrid` (or `ses`)
- `files`: `document.pdf` (browse and select)

---

## 🧪 Testing with cURL

### Windows PowerShell:
```powershell
$uri = "http://localhost:8080/api/email/send-ses-with-file"
$form = @{
    to = "test@example.com"
    subject = "Test Email"
    body = "<h1>Test</h1>"
    files = Get-Item "C:\path\to\invoice.pdf"
}
Invoke-RestMethod -Uri $uri -Method Post -Form $form
```

### Windows CMD:
```cmd
curl -X POST http://localhost:8080/api/email/send-ses-with-file ^
  -F "to=test@example.com" ^
  -F "subject=Test Email" ^
  -F "body=<h1>Test</h1>" ^
  -F "files=@C:\path\to\invoice.pdf" ^
  -F "files=@C:\path\to\receipt.pdf"
```

### Linux/Mac:
```bash
curl -X POST http://localhost:8080/api/email/send-ses-with-file \
  -F "to=test@example.com" \
  -F "subject=Test Email" \
  -F "body=<h1>Test</h1>" \
  -F "files=@/path/to/invoice.pdf" \
  -F "files=@/path/to/receipt.pdf"
```

---

## 📊 Comparison: Headers vs JSON vs File Upload

| Method | Max Size | Ease of Use | Performance | Best For |
|--------|----------|-------------|-------------|----------|
| **Headers** | ❌ ~8KB | ❌ Complex | ❌ Poor | ❌ Not Recommended |
| **Base64 in JSON** | ⚠️ Limited | ⚠️ Medium | ⚠️ 33% overhead | Small files, APIs only |
| **File Upload (multipart)** | ✅ Large files | ✅ Easy | ✅ Best | ✅ **Recommended!** |

---

## ✅ Why File Upload is Better

### ❌ **Headers Approach (NOT Recommended):**
- Headers limited to ~8KB
- PDFs are typically 100KB - 10MB
- Would fail for most real-world PDFs
- Not designed for file content

### ⚠️ **Base64 in JSON (Current):**
- Works but increases file size by 33%
- 1MB PDF → 1.33MB when base64 encoded
- Requires extra encoding/decoding
- Good for API-to-API communication

### ✅ **File Upload (NEW - Best!):**
- No size increase (binary upload)
- Standard way to upload files
- Works in Postman, web forms, mobile apps
- Better performance
- Easier to test

---

## 🎯 All Email Endpoints Summary

| Endpoint | Input Type | Attachments | Provider |
|----------|------------|-------------|----------|
| `/api/email/send` | JSON | No | Dynamic |
| `/api/email/send-ses` | JSON | No | AWS SES |
| `/api/email/send-sendgrid` | JSON | No | SendGrid |
| `/api/email/send-with-attachment` | JSON | Yes (Base64) | Dynamic |
| `/api/email/send-ses-with-attachment` | JSON | Yes (Base64) | AWS SES |
| `/api/email/send-sendgrid-with-attachment` | JSON | Yes (Base64) | SendGrid |
| `/api/email/send-with-file` | **Multipart** | **Yes (File)** | Dynamic |
| `/api/email/send-ses-with-file` | **Multipart** | **Yes (File)** | AWS SES ⭐ |
| `/api/email/send-sendgrid-with-file` | **Multipart** | **Yes (File)** | SendGrid ⭐ |

---

## 📝 Postman Screenshot Example

```
Method: POST
URL: http://localhost:8080/api/email/send-ses-with-file

Headers:
  (No need to set Content-Type - Postman handles it automatically)

Body: [form-data selected]
┌─────────────┬──────┬────────────────────────────────┐
│ to          │ Text │ anand.abhisheek@gmail.com      │
│ subject     │ Text │ Test PDF Upload                │
│ body        │ Text │ <h1>Test</h1><p>PDF attached</p>│
│ files       │ File │ [Select File] invoice.pdf      │
└─────────────┴──────┴────────────────────────────────┘

[Send]
```

---

## 🔧 Configuration

No additional configuration needed! The endpoints are ready to use.

### Max Upload Size (Optional)

If you need to upload large files, update `application.properties`:

```properties
# Max file size
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

---

## ✅ Expected Response

**Success (200 OK):**
```json
{
  "success": true,
  "message": "Email with 1 file(s) sent successfully via AWS SES",
  "data": "ses",
  "timestamp": "2026-01-09T..."
}
```

**Error (500 Internal Server Error):**
```json
{
  "success": false,
  "message": "Failed to send email with files: File too large",
  "data": null,
  "timestamp": "2026-01-09T..."
}
```

---

## 🎯 When to Use Each Method

### Use **File Upload (Multipart)** when:
- ✅ Testing in Postman
- ✅ Building web forms
- ✅ Mobile app file uploads
- ✅ Large PDFs (>1MB)
- ✅ Multiple files
- ✅ Best performance needed

### Use **Base64 in JSON** when:
- API-to-API communication
- Small files (<100KB)
- No file upload UI available
- JSON-only systems

### Never Use **Headers** for:
- ❌ Files of any size
- ❌ PDF attachments
- ❌ Binary data

---

## 🚀 Quick Start

1. **Start your application:**
   ```bash
   .\gradlew.bat bootRun
   ```

2. **Open Postman**

3. **Create new request:**
   - Method: POST
   - URL: `http://localhost:8080/api/email/send-ses-with-file`

4. **Select Body → form-data**

5. **Add fields:**
   - `to`, `subject`, `body` as Text
   - `files` as File (browse to select PDF)

6. **Click Send!**

---

## 📚 Additional Resources

- **Spring Boot File Upload:** https://spring.io/guides/gs/uploading-files/
- **Postman Multipart Requests:** https://learning.postman.com/docs/sending-requests/requests/

---

## 🎉 Summary

✅ **File Upload (Multipart)** is the **BEST** way to send PDFs  
⚠️ Base64 in JSON works but has 33% overhead  
❌ Headers are **NOT suitable** for file attachments  

**Use the new file upload endpoints for the best experience!** 🚀
