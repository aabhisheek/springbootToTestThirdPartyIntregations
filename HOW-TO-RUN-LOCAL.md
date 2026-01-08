# 🚀 How to Run the Application Locally

## ✅ Your credentials are now safely configured!

---

## 📁 Configuration Files

### 1. `application.properties` (Committed to Git)
Contains **placeholder values only** - safe to commit:
```properties
aws.ses.access-key=YOUR_AWS_ACCESS_KEY
aws.ses.secret-key=YOUR_AWS_SECRET_KEY
sendgrid.api.key=YOUR_SENDGRID_API_KEY
```

### 2. `application-local.properties` (Git-ignored)
Contains **your actual credentials** - never committed:
```properties
aws.ses.region=eu-north-1
aws.ses.access-key=YOUR_ACTUAL_AWS_ACCESS_KEY
aws.ses.secret-key=YOUR_ACTUAL_AWS_SECRET_KEY
aws.ses.from-email=your-email@gmail.com
email.provider=ses
```

**⚠️ IMPORTANT:** Add your actual AWS and SendGrid credentials to this file!

---

## 🏃 Running the Application

### Method 1: Using Gradle (Recommended)

```bash
# Navigate to project directory
cd C:\Users\anand\Desktop\SpringProjects\BasicApplicationForTestingAnyFeatureInSpringBoot

# Run the application
.\gradlew.bat bootRun
```

Spring Boot will automatically load both:
- `application.properties` (default config)
- `application-local.properties` (your credentials) ✅

### Method 2: Using IntelliJ IDEA / Eclipse

1. Import the project as a Gradle project
2. Right-click on `EmailDemoApplication.java`
3. Click "Run"
4. The application will load your local properties automatically

---

## 🧪 Testing the Application

### 1. Check Application is Running

Open browser: http://localhost:8080/api/email/health

Expected response:
```json
{
  "success": true,
  "message": "Email service is running",
  "data": "OK",
  "timestamp": "2026-01-09T..."
}
```

### 2. Test H2 Database Console

Open browser: http://localhost:8080/h2-console

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (empty)

### 3. Test Email Sending

Using Postman or cURL:

```bash
# Test AWS SES with PDF attachment
curl -X POST http://localhost:8080/api/email/send-ses-with-attachment ^
  -H "Content-Type: application/json" ^
  -d "{\"to\":\"anand.abhisheek@gmail.com\",\"subject\":\"Test PDF\",\"body\":\"<h1>Test</h1>\",\"attachments\":[{\"fileName\":\"test.pdf\",\"contentType\":\"application/pdf\",\"base64Content\":\"JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9UeXBlIC9QYWdlcyAvS2lkcyBbNCAwIFJdIC9Db3VudCAxID4+CmVuZG9iago0IDAgb2JqCjw8L1R5cGUgL1BhZ2UgL1BhcmVudCAzIDAgUiAvTWVkaWFCb3ggWzAgMCA2MTIgNzkyXSAvQ29udGVudHMgNSAwIFIgL1Jlc291cmNlcyA8PC9Gb250IDw8L0YxIDcgMCBSPj4+Pj4+CmVuZG9iago1IDAgb2JqCjw8L0xlbmd0aCA0NT4+CnN0cmVhbQpCVAovRjEgMjQgVGYKMTAwIDcwMCBUZAooSGVsbG8gV29ybGQhKSBUagpFVAplbmRzdHJlYW0KZW5kb2JqCjcgMCBvYmoKPDwvVHlwZSAvRm9udCAvU3VidHlwZSAvVHlwZTEgL0Jhc2VGb250IC9UaW1lcy1Sb21hbj4+CmVuZG9iagp4cmVmCjAgOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDAwMDkgMDAwMDAgbiAKMDAwMDAwMDA1OCAwMDAwMCBuIAowMDAwMDAwMTE1IDAwMDAwIG4gCjAwMDAwMDAxNzQgMDAwMDAgbiAKMDAwMDAwMDMwMSAwMDAwMCBuIAowMDAwMDAwMzk1IDAwMDAwIG4gCjAwMDAwMDA0NzQgMDAwMDAgbiAKdHJhaWxlcgo8PC9TaXplIDggL1Jvb3QgMSAwIFI+PgpzdGFydHhyZWYKNTY1CiUlRU9G\"}]}"
```

---

## 📂 File Structure

```
project/
├── src/main/resources/
│   ├── application.properties          ✅ Git tracked (placeholders only)
│   └── application-local.properties    🔒 Git ignored (your credentials)
├── .gitignore                          ✅ Configured properly
└── ...
```

---

## 🔄 If You Need to Update Credentials

### Update Local Credentials:

1. Edit `src/main/resources/application-local.properties`
2. Change the values
3. Restart the application
4. **Don't commit this file!**

### For Team Members:

Each team member should create their own `application-local.properties` file with their credentials.

---

## 🎯 Quick Command Reference

```bash
# Build the project
.\gradlew.bat build

# Run the application
.\gradlew.bat bootRun

# Run tests
.\gradlew.bat test

# Clean build
.\gradlew.bat clean build

# Check if build works
.\gradlew.bat build -x test
```

---

## 🐛 Troubleshooting

### Issue: Application can't find credentials

**Solution:** Make sure `application-local.properties` exists in `src/main/resources/`

### Issue: Emails not sending

**Solution:**
1. Check AWS credentials are correct
2. Verify email addresses are verified in AWS SES (if in sandbox mode)
3. Check application logs for detailed error messages

### Issue: Port 8080 already in use

**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

---

## 📊 Application URLs

| Service | URL | Description |
|---------|-----|-------------|
| Application | http://localhost:8080 | Main application |
| H2 Console | http://localhost:8080/h2-console | Database console |
| Email Health | http://localhost:8080/api/email/health | Health check |
| User API | http://localhost:8080/api/users | User CRUD |
| Product API | http://localhost:8080/api/products | Product CRUD |
| Order API | http://localhost:8080/api/orders | Order CRUD |
| Email API | http://localhost:8080/api/email/* | Email operations |

---

## 🎉 You're All Set!

Your application is configured and ready to run. Your credentials are safe and won't be committed to Git.

**Remember:** Check `SECURITY-NOTICE.md` for important security actions you need to take!
