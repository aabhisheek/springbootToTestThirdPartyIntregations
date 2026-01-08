# Spring Boot Email Demo Application

A comprehensive Spring Boot application demonstrating REST APIs, database operations, and email integration with AWS SES and SendGrid.

## Features

- **4 REST API Controllers** (User, Product, Order, Email)
- **3 Database Tables** (Users, Products, Orders)
- **AWS SES Email Integration**
- **SendGrid Email Integration**
- **H2 In-Memory Database**
- **Gradle Build Tool**
- **Validation & Error Handling**
- **Lombok for cleaner code**

## Technologies Used

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- AWS SES SDK
- SendGrid API
- Gradle
- Lombok

## Project Structure

```
src/main/java/com/example/emaildemo/
├── controller/          # REST API Controllers
│   ├── UserController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   └── EmailController.java
├── service/            # Business Logic
│   ├── UserServiceImpl.java
│   ├── ProductServiceImpl.java
│   ├── OrderServiceImpl.java
│   ├── EmailService.java
│   ├── AwsSesEmailService.java
│   ├── SendGridEmailService.java
│   └── EmailServiceFactory.java
├── repository/         # Data Access Layer
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── entity/            # Database Entities
│   ├── User.java
│   ├── Product.java
│   └── Order.java
├── dto/               # Data Transfer Objects
│   ├── UserDTO.java
│   ├── ProductDTO.java
│   ├── OrderDTO.java
│   ├── EmailRequest.java
│   └── ApiResponse.java
├── config/            # Configuration
│   └── WebConfig.java
└── EmailDemoApplication.java
```

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Gradle 7.x or higher

### Configuration

1. **Clone or create the project**

2. **Configure Email Services** in `src/main/resources/application.properties`:

```properties
# AWS SES Configuration
aws.ses.region=us-east-1
aws.ses.access-key=YOUR_AWS_ACCESS_KEY
aws.ses.secret-key=YOUR_AWS_SECRET_KEY
aws.ses.from-email=noreply@yourdomain.com

# SendGrid Configuration
sendgrid.api.key=YOUR_SENDGRID_API_KEY
sendgrid.from.email=noreply@yourdomain.com
sendgrid.from.name=Email Demo App

# Default Email Provider (ses or sendgrid)
email.provider=sendgrid
```

3. **Build the project**:

```bash
gradlew build
```

4. **Run the application**:

```bash
gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### User APIs

- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

**Example Request:**
```json
POST /api/users
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "address": "123 Main St, City, Country"
}
```

### Product APIs

- `POST /api/products` - Create a new product
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{category}` - Get products by category
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

**Example Request:**
```json
POST /api/products
{
  "name": "Laptop",
  "description": "High performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics"
}
```

### Order APIs

- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get orders by user ID
- `PATCH /api/orders/{id}/status?status=CONFIRMED` - Update order status
- `DELETE /api/orders/{id}` - Cancel order

**Example Request:**
```json
POST /api/orders
{
  "userId": 1,
  "productId": 1,
  "quantity": 2,
  "notes": "Please deliver by evening"
}
```

**Order Status Values:**
- PENDING
- CONFIRMED
- PROCESSING
- SHIPPED
- DELIVERED
- CANCELLED

### Email APIs

- `POST /api/email/send` - Send email using default provider
- `POST /api/email/send-ses` - Send email via AWS SES
- `POST /api/email/send-sendgrid` - Send email via SendGrid
- `GET /api/email/health` - Health check

**Example Request:**
```json
POST /api/email/send
{
  "to": "recipient@example.com",
  "subject": "Welcome to Email Demo",
  "body": "<h1>Welcome!</h1><p>This is a test email.</p>",
  "provider": "sendgrid"
}
```

## Database Access

The application uses H2 in-memory database. You can access the H2 console at:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## Email Provider Configuration

### AWS SES Setup

1. Create an AWS account
2. Navigate to Amazon SES (Simple Email Service)
3. Verify your email addresses or domain
4. Create IAM credentials with SES permissions
5. Update `application.properties` with your credentials

### SendGrid Setup

1. Create a SendGrid account
2. Navigate to Settings > API Keys
3. Create a new API key with Mail Send permissions
4. Update `application.properties` with your API key

## Testing the APIs

### Using cURL

**Create a User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'
```

**Send Email:**
```bash
curl -X POST http://localhost:8080/api/email/send-sendgrid \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email",
    "body": "Hello from Spring Boot!"
  }'
```

### Using Postman

Import the API endpoints into Postman and test all the CRUD operations.

## Error Handling

All APIs return a standardized response format:

**Success Response:**
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {...},
  "timestamp": "2026-01-08T16:14:00"
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error message",
  "data": null,
  "timestamp": "2026-01-08T16:14:00"
}
```

## Development

### Building the Project

```bash
gradlew clean build
```

### Running Tests

```bash
gradlew test
```

### Running in Development Mode

```bash
gradlew bootRun --args='--spring.profiles.active=dev'
```

## Notes

- The application uses an in-memory H2 database, so data is lost when the application stops
- To use MySQL or PostgreSQL, update the `application.properties` and add the appropriate driver dependency
- Make sure to configure valid AWS SES or SendGrid credentials before testing email functionality
- The default email provider is set to SendGrid in `application.properties`

## License

This is a demo application for testing Spring Boot features.

## Author

Created as a basic Spring Boot application demonstrating REST APIs and email integration.
