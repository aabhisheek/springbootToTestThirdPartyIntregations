# 📮 Postman Collection - File Upload Guide

## ✅ Updated Postman Collection

Your `Postman-Collection-With-PDF.json` has been updated with **File Upload endpoints**!

---

## 📥 Import into Postman

### **Step 1: Import Collection**

1. Open Postman
2. Click **Import** button (top left)
3. Select **Postman-Collection-With-PDF.json**
4. Click **Import**

---

## 📂 Collection Structure

After importing, you'll see:

```
Spring Boot Email API with PDF Attachments
├── Email APIs (Base64 JSON - Old Method)
│   ├── Send Email via AWS SES (No Attachment)
│   ├── Send Email via SendGrid (No Attachment)
│   ├── Send Email with PDF - AWS SES (Base64)
│   ├── Send Email with PDF - SendGrid (Base64)
│   ├── Send Email with Multiple PDFs (Base64)
│   └── Email Health Check
│
└── File Upload APIs (Recommended) ⭐ NEW!
    ├── Send Email with File Upload - AWS SES
    ├── Send Email with File Upload - SendGrid
    ├── Send Email with File Upload - Generic
    └── Send Email with Multiple Files - AWS SES
```

---

## 🎯 How to Use File Upload Endpoints

### **Example: Send Email with File Upload - AWS SES**

#### **Step 1:** Select the Request
- Click on `File Upload APIs (Recommended)`
- Select `Send Email with File Upload - AWS SES`

#### **Step 2:** Review Pre-filled Fields

You'll see the Body tab with `form-data` selected:

| Key | Type | Value (Pre-filled) |
|-----|------|--------------------|
| `to` | Text | `recipient@example.com` |
| `subject` | Text | `Invoice - Order #12345` |
| `body` | Text | `<html>...</html>` |
| `files` | File | *Empty - You need to select* |

#### **Step 3:** Update Email Details

- **Change `to`** to your email address
- **Update `subject`** if needed
- **Modify `body`** HTML content as desired

#### **Step 4:** Select Your PDF File

1. Find the **`files`** row
2. Click **Select Files** button (right side)
3. Browse and select your PDF file
4. The file name will appear

#### **Step 5:** Send the Request

- Click **Send** button
- Wait for response

#### **Expected Response:**

```json
{
  "success": true,
  "message": "Email with 1 file(s) sent successfully via AWS SES",
  "data": "ses",
  "timestamp": "2026-01-09T..."
}
```

---

## 📝 Visual Example

### **Postman Interface:**

```
┌─────────────────────────────────────────────────────────────┐
│  POST  http://localhost:8080/api/email/send-ses-with-file  │
└─────────────────────────────────────────────────────────────┘

┌─ Body ─┬─ form-data ─────────────────────────────────────┐
│        │                                                   │
│ Key    │ Value                                             │
├────────┼───────────────────────────────────────────────────┤
│ to     │ customer@example.com                              │
│        │ [Text]                                            │
├────────┼───────────────────────────────────────────────────┤
│ subject│ Your Invoice                                      │
│        │ [Text]                                            │
├────────┼───────────────────────────────────────────────────┤
│ body   │ <h1>Invoice Attached</h1><p>Thank you!</p>       │
│        │ [Text]                                            │
├────────┼───────────────────────────────────────────────────┤
│ files  │ invoice.pdf                    [Select Files]     │
│        │ [File]                                            │
└────────┴───────────────────────────────────────────────────┘

                       [Send] ← Click here!
```

---

## 🎯 All Available File Upload Requests

### 1. **Send Email with File Upload - AWS SES**
```
POST /api/email/send-ses-with-file
```
- **Use Case:** Send email via AWS SES with PDF
- **Fields:** to, subject, body, files
- **Files:** Single or multiple

### 2. **Send Email with File Upload - SendGrid**
```
POST /api/email/send-sendgrid-with-file
```
- **Use Case:** Send email via SendGrid with PDF
- **Fields:** to, subject, body, files
- **Files:** Single or multiple

### 3. **Send Email with File Upload - Generic**
```
POST /api/email/send-with-file
```
- **Use Case:** Choose provider dynamically
- **Fields:** to, subject, body, provider, files
- **Provider:** "ses" or "sendgrid"
- **Files:** Single or multiple

### 4. **Send Email with Multiple Files - AWS SES**
```
POST /api/email/send-ses-with-file
```
- **Use Case:** Send multiple PDFs at once
- **Fields:** to, subject, body, files (×3)
- **Files:** Multiple files field

---

## 📎 How to Add Multiple Files

### **Method 1: Multiple Files in One Field**

In Postman, you can select multiple files in a single `files` field:

1. Click `files` field
2. Click **Select Files**
3. Hold `Ctrl` (Windows) or `Cmd` (Mac)
4. Select multiple files
5. Click **Open**

### **Method 2: Multiple Files Fields**

Add multiple `files` rows:

1. In form-data, hover over `files` row
2. Click the duplicate icon or add new row
3. Name it `files` (same name)
4. Select different file for each row

```
┌────────┬───────────────────────────────┐
│ files  │ invoice.pdf     [Select Files]│
├────────┼───────────────────────────────┤
│ files  │ receipt.pdf     [Select Files]│
├────────┼───────────────────────────────┤
│ files  │ summary.pdf     [Select Files]│
└────────┴───────────────────────────────┘
```

---

## 🎨 Postman Tips

### **Tip 1: Save Your Email**

Save your email address as a Postman variable:

1. Click **Environment** (top right)
2. Create new environment
3. Add variable: `my_email` = `your@email.com`
4. Use in request: `{{my_email}}`

### **Tip 2: Test Different Files**

Create multiple requests with different test files:
- Small PDF (< 100KB)
- Medium PDF (1-2 MB)
- Multiple PDFs

### **Tip 3: Check Response Times**

Compare performance:
- Base64 JSON upload vs File upload
- Small files vs Large files

### **Tip 4: Use Pre-request Scripts**

Add validation before sending:

```javascript
// Ensure recipient email is set
if (!pm.request.body.formdata.get('to')) {
    console.error('Recipient email is required!');
}
```

---

## 📊 Comparison: Base64 vs File Upload

| Feature | Base64 JSON | File Upload |
|---------|-------------|-------------|
| **Ease in Postman** | ⚠️ Manual encoding | ✅ Click & select |
| **File Size** | ⚠️ +33% overhead | ✅ No overhead |
| **Multiple Files** | ⚠️ Complex JSON | ✅ Easy |
| **Testing Speed** | ⚠️ Slow | ✅ Fast |
| **Recommendation** | API-only | ✅ **Preferred** |

---

## ✅ Testing Checklist

Use this checklist to test all scenarios:

- [ ] Send single PDF via AWS SES
- [ ] Send single PDF via SendGrid
- [ ] Send multiple PDFs (2-3 files)
- [ ] Send large PDF (5+ MB)
- [ ] Send to multiple recipients
- [ ] Test with different PDF files
- [ ] Verify email received
- [ ] Check PDF opens correctly
- [ ] Test error handling (invalid email, missing file)

---

## 🎯 Common Issues & Solutions

### **Issue 1: "files field is empty"**

**Solution:** Make sure you've clicked "Select Files" and chosen a PDF

### **Issue 2: "File too large"**

**Solution:** 
- Check file size limits in `application.properties`
- AWS SES: Max 10MB total
- SendGrid: Max 30MB total

### **Issue 3: "Email not received"**

**Solution:**
- Check AWS SES sandbox mode (emails must be verified)
- Check SendGrid sender verification
- Look at application logs for errors

### **Issue 4: "Cannot find form-data"**

**Solution:** Make sure you selected **form-data** in the Body tab, not raw or JSON

---

## 🚀 Quick Start Steps

1. ✅ Import `Postman-Collection-With-PDF.json`
2. ✅ Start your Spring Boot app: `.\gradlew.bat bootRun`
3. ✅ Open Postman
4. ✅ Select: `File Upload APIs` → `Send Email with File Upload - AWS SES`
5. ✅ Update `to` email address
6. ✅ Click `Select Files` for `files` field
7. ✅ Choose your PDF
8. ✅ Click **Send**!
9. ✅ Check your email!

---

## 📚 Related Documentation

- **FILE-UPLOAD-GUIDE.md** - Complete file upload documentation
- **HEADERS-vs-FILE-UPLOAD.md** - Comparison of all methods
- **EMAIL-WITH-PDF-GUIDE.md** - Base64 method guide

---

## 🎉 Summary

**Before (Base64 JSON):**
```json
{
  "attachments": [{
    "base64Content": "JVBERi0xLjQKJeLjz9MK..." // Manual encoding needed
  }]
}
```

**After (File Upload):** ⭐
```
Just click "Select Files" and choose your PDF!
✅ No encoding needed
✅ Easy to test
✅ Better performance
```

---

**Your Postman collection is ready! Import and start testing! 🚀**
