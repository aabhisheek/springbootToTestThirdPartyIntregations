# API Quick Start Guide

## Start the Application

```bash
gradlew bootRun
```

Application will start at: `http://localhost:8080`

## Quick Test Sequence

### 1. Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\",\"address\":\"123 Main St\"}"
```

### 2. Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Laptop\",\"description\":\"Gaming Laptop\",\"price\":1299.99,\"stockQuantity\":10,\"category\":\"Electronics\"}"
```

### 3. Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"userId\":1,\"productId\":1,\"quantity\":2,\"notes\":\"Express delivery\"}"
```

### 4. Send Email (SendGrid)

```bash
curl -X POST http://localhost:8080/api/email/send-sendgrid \
  -H "Content-Type: application/json" \
  -d "{\"to\":\"test@example.com\",\"subject\":\"Test Email\",\"body\":\"<h1>Hello!</h1><p>This is a test.</p>\"}"
```

### 5. Send Email (AWS SES)

```bash
curl -X POST http://localhost:8080/api/email/send-ses \
  -H "Content-Type: application/json" \
  -d "{\"to\":\"test@example.com\",\"subject\":\"Test Email\",\"body\":\"<h1>Hello from SES!</h1>\"}"
```

## Get All Records

```bash
# Get all users
curl http://localhost:8080/api/users

# Get all products
curl http://localhost:8080/api/products

# Get all orders
curl http://localhost:8080/api/orders
```

## Update Examples

```bash
# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"John Smith\",\"email\":\"john@example.com\",\"phone\":\"9876543210\",\"address\":\"456 New St\"}"

# Update order status
curl -X PATCH "http://localhost:8080/api/orders/1/status?status=CONFIRMED"
```

## H2 Database Console

Access at: `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (empty)

## Configure Email Providers

Edit `src/main/resources/application.properties`:

```properties
# AWS SES
aws.ses.access-key=YOUR_KEY
aws.ses.secret-key=YOUR_SECRET

# SendGrid
sendgrid.api.key=YOUR_KEY
```

## API Response Format

```json
{
  "success": true,
  "message": "Operation completed",
  "data": {...},
  "timestamp": "2026-01-08T16:14:00"
}
```
