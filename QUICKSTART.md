# Quick Start Guide

## Step 1: Configure Email Credentials

Before running the application, update `src/main/resources/application.properties` with your email service credentials:

### For SendGrid:
```properties
sendgrid.api.key=YOUR_ACTUAL_SENDGRID_API_KEY
sendgrid.from.email=your-verified-email@yourdomain.com
sendgrid.from.name=Your App Name
```

### For AWS SES:
```properties
aws.ses.region=us-east-1
aws.ses.access-key=YOUR_AWS_ACCESS_KEY
aws.ses.secret-key=YOUR_AWS_SECRET_KEY
aws.ses.from-email=your-verified-email@yourdomain.com
```

## Step 2: Build the Project

Open terminal in the project root and run:

```bash
gradlew clean build
```

## Step 3: Run the Application

```bash
gradlew bootRun
```

Or run the main class `EmailDemoApplication.java` from your IDE.

The application will start on **http://localhost:8080**

## Step 4: Test the APIs

### Option 1: Using the HTTP file
Open `API-Examples.http` in IntelliJ IDEA or VS Code (with REST Client extension) and run the requests.

### Option 2: Using cURL

**Test Health Endpoint:**
```bash
curl http://localhost:8080/api/email/health
```

**Create a User:**
```bash
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Main St\"}"
```

**Get All Users:**
```bash
curl http://localhost:8080/api/users
```

**Create a Product:**
```bash
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d "{\"name\":\"Laptop\",\"description\":\"Gaming Laptop\",\"price\":1299.99,\"stockQuantity\":10,\"category\":\"Electronics\"}"
```

**Send Email via SendGrid:**
```bash
curl -X POST http://localhost:8080/api/email/send-sendgrid -H "Content-Type: application/json" -d "{\"to\":\"recipient@example.com\",\"subject\":\"Test Email\",\"body\":\"<h1>Hello!</h1><p>Test email from Spring Boot</p>\"}"
```

## Step 5: Access H2 Database Console

Open your browser and go to: **http://localhost:8080/h2-console**

Use these credentials:
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave blank)

## Common Issues

### Issue 1: Port 8080 already in use
Change the port in `application.properties`:
```properties
server.port=8081
```

### Issue 2: Email sending fails
- Verify your API keys are correct
- Check that your email addresses are verified in SendGrid/AWS SES
- Check the logs for detailed error messages

### Issue 3: Gradle build fails
Make sure you have Java 17 or higher installed:
```bash
java -version
```

## IDE Setup

### IntelliJ IDEA:
1. Open IntelliJ IDEA
2. File → Open → Select project folder
3. Wait for Gradle to sync
4. Right-click on `EmailDemoApplication.java` → Run

### VS Code:
1. Open VS Code
2. Install "Extension Pack for Java" and "Spring Boot Extension Pack"
3. File → Open Folder → Select project folder
4. Wait for Gradle to sync
5. Press F5 or use the Run button

## API Testing Order

1. **Create a User** (POST /api/users)
2. **Create a Product** (POST /api/products)
3. **Create an Order** (POST /api/orders) - Use user ID and product ID from steps 1 & 2
4. **Send Confirmation Email** (POST /api/email/send)

## Next Steps

- Review the code in `src/main/java/com/example/emaildemo/`
- Customize the entities and DTOs for your needs
- Add authentication and authorization
- Switch from H2 to MySQL/PostgreSQL for production
- Add more business logic and validations

## Support

Check the main README.md for detailed API documentation and examples.
