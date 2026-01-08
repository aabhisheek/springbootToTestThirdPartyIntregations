# Setup Instructions

## Project Created Successfully! ✅

Your Spring Boot application has been created with all the required features:

- ✅ 4 REST API Controllers (User, Product, Order, Email)
- ✅ 3 Database Tables (Users, Products, Orders)
- ✅ AWS SES Email Integration
- ✅ SendGrid Email Integration
- ✅ Gradle Build Configuration

## Initial Setup (Required)

### Option 1: Using Gradle Wrapper (Recommended)

If you have Gradle installed on your system:

```bash
# Initialize Gradle wrapper
gradle wrapper --gradle-version 8.5

# This will download the Gradle wrapper jar file
```

### Option 2: Import into IDE

The easiest way to get started:

1. **IntelliJ IDEA**:
   - Open IntelliJ IDEA
   - File → Open → Select the project folder
   - IntelliJ will automatically detect the Gradle build and download dependencies
   - Wait for indexing to complete

2. **Eclipse**:
   - Open Eclipse
   - File → Import → Gradle → Existing Gradle Project
   - Select the project folder
   - Finish

3. **VS Code**:
   - Open VS Code
   - Install "Extension Pack for Java" if not already installed
   - Open the project folder
   - VS Code will detect the Gradle project

### Option 3: Manual Gradle Installation

If you don't have Gradle:

1. Download from: https://gradle.org/releases/
2. Extract and add to PATH
3. Run: `gradle wrapper --gradle-version 8.5`

## Configuration

### 1. Update Email Credentials

Edit `src/main/resources/application.properties`:

```properties
# AWS SES Configuration
aws.ses.region=us-east-1
aws.ses.access-key=YOUR_AWS_ACCESS_KEY_HERE
aws.ses.secret-key=YOUR_AWS_SECRET_KEY_HERE
aws.ses.from-email=noreply@yourdomain.com

# SendGrid Configuration
sendgrid.api.key=YOUR_SENDGRID_API_KEY_HERE
sendgrid.from.email=noreply@yourdomain.com
sendgrid.from.name=Email Demo App

# Default provider (ses or sendgrid)
email.provider=sendgrid
```

### 2. AWS SES Setup (Optional)

If using AWS SES:
1. Create AWS account
2. Go to Amazon SES console
3. Verify email addresses or domain
4. Create IAM credentials with SES permissions
5. Update credentials in application.properties

### 3. SendGrid Setup (Optional)

If using SendGrid:
1. Create SendGrid account at https://sendgrid.com
2. Navigate to Settings → API Keys
3. Create new API key with Mail Send permissions
4. Update API key in application.properties

## Running the Application

### Using IDE

Most IDEs will allow you to run the main class directly:
- Navigate to `EmailDemoApplication.java`
- Right-click → Run 'EmailDemoApplication'

### Using Gradle (after wrapper setup)

```bash
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### Using Maven (Alternative)

If you prefer Maven, you can convert the project:
1. Create a `pom.xml` based on the dependencies in `build.gradle`
2. Run: `mvn spring-boot:run`

## Accessing the Application

Once running:

- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (empty)

## Testing the APIs

### Quick Test

```bash
# Create a user
curl -X POST http://localhost:8080/api/users ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Main St\"}"

# Get all users
curl http://localhost:8080/api/users
```

### Full API Documentation

See `API-QUICK-START.md` for complete API examples.

## Project Structure

```
src/
├── main/
│   ├── java/com/example/emaildemo/
│   │   ├── controller/      # 4 REST Controllers
│   │   ├── service/         # Business logic + Email services
│   │   ├── repository/      # Data access
│   │   ├── entity/          # 3 Database tables
│   │   ├── dto/             # Data transfer objects
│   │   ├── config/          # Configuration
│   │   └── EmailDemoApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/emaildemo/
```

## Troubleshooting

### Issue: Gradle wrapper not found

**Solution**: Import project into IDE (IntelliJ/Eclipse/VS Code) or install Gradle manually

### Issue: Cannot compile

**Solution**: 
1. Ensure Java 17+ is installed: `java -version`
2. Refresh Gradle dependencies in your IDE
3. Clean build: `gradle clean build`

### Issue: Application won't start

**Solution**: Check if port 8080 is available or change it in `application.properties`:
```properties
server.port=8081
```

### Issue: Email not sending

**Solution**: 
1. Verify email credentials in `application.properties`
2. For AWS SES: Ensure emails are verified in sandbox mode
3. For SendGrid: Check API key permissions
4. Check application logs for detailed error messages

## Next Steps

1. ✅ Import project into your IDE
2. ✅ Configure email credentials (if testing email features)
3. ✅ Run the application
4. ✅ Test APIs using the examples in `API-QUICK-START.md`
5. ✅ Access H2 console to view database
6. ✅ Customize and extend as needed

## Documentation Files

- **README.md** - Comprehensive project documentation
- **API-QUICK-START.md** - API testing examples
- **PROJECT-OVERVIEW.md** - Complete feature list
- **SETUP-INSTRUCTIONS.md** - This file

## Support

The application is complete and ready to use. Simply:
1. Import into your IDE
2. Let the IDE download dependencies
3. Run and test!

Enjoy coding! 🚀
