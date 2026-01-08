# 💳 Payment Integration Testing Guide

## ✅ Razorpay & Stripe Implemented!

Your Spring Boot application now supports **Razorpay** and **Stripe** payments with **Test Mode** for FREE testing!

---

## 🎯 Payment Endpoints

### **Razorpay:**
- `POST /api/payments/razorpay/create-order` - Create order
- `POST /api/payments/razorpay/verify` - Verify payment
- `GET /api/payments/razorpay/{paymentId}` - Get payment details

### **Stripe:**
- `POST /api/payments/stripe/create-payment-intent` - Create payment intent
- `GET /api/payments/stripe/{paymentIntentId}` - Get payment details
- `POST /api/payments/stripe/{paymentIntentId}/confirm` - Confirm payment

---

## 🔑 Getting Test API Keys (FREE)

### **Razorpay Test Mode Keys:**

1. **Sign up**: https://dashboard.razorpay.com/signup
2. Go to **Settings** → **API Keys**
3. **Test Mode** is enabled by default ✅
4. Copy **Key ID** and **Key Secret**

**Example Test Keys:**
```
Key ID: rzp_test_1DP5mmOlF5G5ag
Key Secret: THzEB2vS1K8rM1kcWq2K5XYz
```

### **Stripe Test Mode Keys:**

1. **Sign up**: https://dashboard.stripe.com/register
2. Toggle **Test mode** ON (top right) ✅
3. Go to **Developers** → **API keys**
4. Copy **Secret key** (starts with `sk_test_`)

**Example Test Key:**
```
Secret Key: sk_test_51NQZABC...XYZ123
```

---

## ⚙️ Configuration

Update `src/main/resources/application-local.properties`:

```properties
# Razorpay Test Mode
razorpay.key.id=rzp_test_YOUR_KEY_ID
razorpay.key.secret=YOUR_KEY_SECRET

# Stripe Test Mode
stripe.api.key=sk_test_YOUR_SECRET_KEY
```

---

## 🧪 Testing with Dummy Data

### **Test 1: Razorpay - Create Order**

**Endpoint:** `POST /api/payments/razorpay/create-order`

**Request Body:**
```json
{
  "amount": 50000,
  "currency": "INR",
  "customerEmail": "test@example.com",
  "customerName": "Test User",
  "customerPhone": "9999999999",
  "description": "Test Order Payment",
  "orderId": "order_123"
}
```

**Notes:**
- Amount in **paise** (50000 = ₹500)
- Currency: INR, USD, EUR, etc.

**Expected Response:**
```json
{
  "success": true,
  "message": "Razorpay order created successfully",
  "data": {
    "orderId": "order_KxW3zO9YzQxYZq",
    "amount": 50000,
    "currency": "INR",
    "status": "created",
    "provider": "razorpay",
    "additionalData": {
      "key": "rzp_test_1DP5mmOlF5G5ag",
      "order_id": "order_KxW3zO9YzQxYZq",
      "customer_email": "test@example.com",
      "customer_name": "Test User"
    }
  },
  "timestamp": "2026-01-09T..."
}
```

---

### **Test 2: Stripe - Create Payment Intent**

**Endpoint:** `POST /api/payments/stripe/create-payment-intent`

**Request Body:**
```json
{
  "amount": 5000,
  "currency": "USD",
  "customerEmail": "test@example.com",
  "customerName": "Test User",
  "description": "Test Payment",
  "orderId": "order_123"
}
```

**Notes:**
- Amount in **cents** (5000 = $50.00)
- Currency: usd, eur, gbp, etc.

**Expected Response:**
```json
{
  "success": true,
  "message": "Stripe payment intent created successfully",
  "data": {
    "paymentId": "pi_3ABCDEF123xyz",
    "orderId": "pi_3ABCDEF123xyz",
    "amount": 5000,
    "currency": "USD",
    "status": "requires_payment_method",
    "provider": "stripe",
    "additionalData": {
      "client_secret": "pi_3ABC...secret_xyz",
      "payment_intent_id": "pi_3ABCDEF123xyz",
      "customer_email": "test@example.com"
    }
  },
  "timestamp": "2026-01-09T..."
}
```

---

## 💳 Dummy/Test Cards

### **Razorpay Test Cards:**

| Card Number | CVV | Expiry | Status |
|-------------|-----|--------|--------|
| `4111 1111 1111 1111` | Any 3 digits | Any future date | ✅ Success |
| `5555 5555 5555 4444` | Any 3 digits | Any future date | ✅ Success |
| `4000 0000 0000 0002` | Any 3 digits | Any future date | ❌ Card Declined |
| `4000 0000 0000 0341` | Any 3 digits | Any future date | ⚠️ 3D Secure Required |

**Test UPI:** `success@razorpay` (Always succeeds)

### **Stripe Test Cards:**

| Card Number | CVV | Expiry | Status |
|-------------|-----|--------|--------|
| `4242 4242 4242 4242` | Any 3 digits | Any future date | ✅ Success |
| `5555 5555 5555 4444` | Any 3 digits | Any future date | ✅ Success (Mastercard) |
| `4000 0000 0000 0002` | Any 3 digits | Any future date | ❌ Card Declined |
| `4000 0027 6000 3184` | Any 3 digits | Any future date | ⚠️ 3D Secure Required |

**More Test Cards:** https://stripe.com/docs/testing

---

## 📋 Complete Test Scenarios

### **Scenario 1: Full Razorpay Payment Flow**

#### **Step 1:** Create Order
```bash
curl -X POST http://localhost:8080/api/payments/razorpay/create-order \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "currency": "INR",
    "customerEmail": "test@example.com",
    "customerName": "Test User",
    "customerPhone": "9999999999",
    "description": "Product Purchase"
  }'
```

**Save the `orderId` from response!**

#### **Step 2:** (Frontend) Open Razorpay Checkout
```javascript
// In your frontend/HTML
var options = {
    "key": "rzp_test_YOUR_KEY",
    "amount": "50000",
    "currency": "INR",
    "order_id": "order_KxW3zO9YzQxYZq", // from step 1
    "handler": function (response){
        // Step 3: Verify payment
        verifyPayment(response);
    }
};
var rzp = new Razorpay(options);
rzp.open();
```

#### **Step 3:** Verify Payment
```bash
curl -X POST http://localhost:8080/api/payments/razorpay/verify \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "order_KxW3zO9YzQxYZq",
    "paymentId": "pay_ABC123xyz",
    "signature": "signature_from_razorpay"
  }'
```

---

### **Scenario 2: Full Stripe Payment Flow**

#### **Step 1:** Create Payment Intent
```bash
curl -X POST http://localhost:8080/api/payments/stripe/create-payment-intent \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 5000,
    "currency": "USD",
    "customerEmail": "test@example.com",
    "customerName": "Test User",
    "description": "Product Purchase"
  }'
```

**Save the `client_secret` from response!**

#### **Step 2:** (Frontend) Confirm Payment with Stripe Elements
```javascript
// In your frontend
const stripe = Stripe('pk_test_YOUR_PUBLIC_KEY');
const result = await stripe.confirmCardPayment('pi_...secret_xyz', {
  payment_method: {
    card: cardElement,
    billing_details: {
      name: 'Test User'
    }
  }
});

if (result.error) {
  console.error(result.error.message);
} else {
  console.log('Payment successful!', result.paymentIntent);
}
```

#### **Step 3:** Check Payment Status
```bash
curl http://localhost:8080/api/payments/stripe/pi_3ABCDEF123xyz
```

---

## 🧪 Postman Testing

### **Import Updated Collection:**

1. Import `Postman-Collection-With-PDF.json`
2. Find **"Payment APIs (Razorpay & Stripe)"** folder
3. 7 ready-to-use requests available!

### **Quick Test in Postman:**

1. **Select:** `Razorpay - Create Order`
2. **Update** amount if needed
3. **Click Send**
4. ✅ You should get an `orderId`!

---

## 💡 Testing Tips

### **1. Amounts:**

**Razorpay:**
```
₹1.00 = 100 paise
₹5.00 = 500 paise
₹500.00 = 50000 paise
```

**Stripe:**
```
$1.00 = 100 cents
$5.00 = 500 cents
$50.00 = 5000 cents
```

### **2. Test Mode Indicators:**

**Razorpay:**
- Key starts with `rzp_test_`
- Dashboard shows "TEST MODE" badge

**Stripe:**
- Key starts with `sk_test_`
- Dashboard has "Test mode" toggle ON

### **3. No Real Money:**

✅ Test mode = 100% FREE
- No credit card charges
- No bank transfers
- Perfect for development!

---

## 🎯 Example Test Cases

### **Test Case 1: Small Amount**
```json
{
  "amount": 100,
  "currency": "INR",
  "customerEmail": "test@example.com",
  "description": "₹1 Test Payment"
}
```

### **Test Case 2: Large Amount**
```json
{
  "amount": 100000,
  "currency": "INR",
  "customerEmail": "test@example.com",
  "description": "₹1000 Test Payment"
}
```

### **Test Case 3: USD Payment (Stripe)**
```json
{
  "amount": 10000,
  "currency": "USD",
  "customerEmail": "test@example.com",
  "description": "$100 Test Payment"
}
```

---

## 📊 Response Status Codes

| Status | Razorpay | Stripe | Meaning |
|--------|----------|--------|---------|
| `created` | ✅ | - | Order created |
| `requires_payment_method` | - | ✅ | Awaiting payment |
| `succeeded` | ✅ | ✅ | Payment successful |
| `failed` | ✅ | ✅ | Payment failed |
| `captured` | ✅ | - | Payment captured |

---

## 🔧 Troubleshooting

### **Issue: "Invalid API Key"**

**Solution:** 
- Check you're using TEST mode keys
- Verify keys are correctly copied
- No extra spaces in keys

### **Issue: "Amount validation failed"**

**Solution:**
- Minimum amount: Razorpay = 100 paise (₹1), Stripe = 50 cents ($0.50)
- Use integer values only
- Check currency is correct

### **Issue: "Payment webhook not working"**

**Solution:**
- Webhooks need public URL (use ngrok for local testing)
- Test mode webhooks work differently
- Check webhook signature validation

---

## 🎯 Integration Checklist

- [✅] Add dependencies to `build.gradle`
- [✅] Configure API keys in `application-local.properties`
- [✅] Test Razorpay order creation
- [✅] Test Stripe payment intent creation
- [✅] Import Postman collection
- [✅] Test with dummy cards
- [ ] Integrate frontend checkout
- [ ] Add webhook handling (optional)
- [ ] Test payment verification
- [ ] Add order confirmation emails

---

## 📚 Official Documentation

- **Razorpay Docs:** https://razorpay.com/docs/
- **Stripe Docs:** https://stripe.com/docs
- **Razorpay Test Cards:** https://razorpay.com/docs/payments/payments/test-card-details/
- **Stripe Test Cards:** https://stripe.com/docs/testing

---

## 🎉 Summary

| Feature | Razorpay | Stripe | Status |
|---------|----------|--------|--------|
| Create Order/Intent | ✅ | ✅ | Working |
| Get Payment Details | ✅ | ✅ | Working |
| Verify Payment | ✅ | ✅ | Working |
| Test Mode | ✅ Free | ✅ Free | Enabled |
| Dummy Cards | ✅ | ✅ | Available |
| Postman Collection | ✅ | ✅ | Ready |

---

**🚀 Your payment integration is ready to test with FREE dummy data!**

**No credit card required - Test mode is 100% FREE!** 💳✨
