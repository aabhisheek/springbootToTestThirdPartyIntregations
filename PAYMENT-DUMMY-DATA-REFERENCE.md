# 💳 Payment Gateway - Dummy Data Quick Reference

## 🎯 Quick Copy-Paste Test Data

---

## 📮 Postman Test Bodies

### **Razorpay - Create Order (₹500)**

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

### **Stripe - Create Payment Intent ($50)**

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

---

## 💳 Test Credit Cards

### **Razorpay Test Cards:**

**SUCCESS:**
```
Card: 4111 1111 1111 1111
CVV: 123
Expiry: 12/2028
```

```
Card: 5555 5555 5555 4444
CVV: 123
Expiry: 12/2028
```

**DECLINED:**
```
Card: 4000 0000 0000 0002
CVV: 123
Expiry: 12/2028
```

**Test UPI:** `success@razorpay`

### **Stripe Test Cards:**

**SUCCESS (Visa):**
```
Card: 4242 4242 4242 4242
CVV: 123
Expiry: 12/28
ZIP: 12345
```

**SUCCESS (Mastercard):**
```
Card: 5555 5555 5555 4444
CVV: 123
Expiry: 12/28
ZIP: 12345
```

**DECLINED:**
```
Card: 4000 0000 0000 0002
CVV: 123
Expiry: 12/28
```

**3D Secure Required:**
```
Card: 4000 0027 6000 3184
CVV: 123
Expiry: 12/28
```

---

## 🧪 Amount Examples

### **Razorpay (INR - Paise)**
```
₹1.00   = 100
₹10.00  = 1000
₹50.00  = 5000
₹100.00 = 10000
₹500.00 = 50000
₹1000.00= 100000
```

### **Stripe (USD - Cents)**
```
$1.00   = 100
$10.00  = 1000
$50.00  = 5000
$100.00 = 10000
$500.00 = 50000
```

---

## 🔑 Test API Keys Format

### **Razorpay:**
```properties
razorpay.key.id=rzp_test_1DP5mmOlF5G5ag
razorpay.key.secret=THzEB2vS1K8rM1kcWq2K5XYz
```

### **Stripe:**
```properties
stripe.api.key=sk_test_51NQZA...xyz123
```

---

## 🎯 All API Endpoints

```
# Razorpay
POST   /api/payments/razorpay/create-order
POST   /api/payments/razorpay/verify
GET    /api/payments/razorpay/{paymentId}

# Stripe
POST   /api/payments/stripe/create-payment-intent
GET    /api/payments/stripe/{paymentIntentId}
POST   /api/payments/stripe/{paymentIntentId}/confirm

# Health
GET    /api/payments/health
```

---

## 📋 Quick Test Checklist

```
□ Sign up for Razorpay Test account
□ Sign up for Stripe Test account
□ Copy test API keys
□ Update application-local.properties
□ Start Spring Boot app
□ Import Postman collection
□ Test Razorpay create order
□ Test Stripe create payment intent
□ Use test cards (4242 4242 4242 4242)
□ Verify response success
```

---

## 🚀 One-Line Tests

**Razorpay (Windows CMD):**
```cmd
curl -X POST http://localhost:8080/api/payments/razorpay/create-order -H "Content-Type: application/json" -d "{\"amount\":50000,\"currency\":\"INR\",\"customerEmail\":\"test@example.com\",\"customerName\":\"Test User\"}"
```

**Stripe (Windows CMD):**
```cmd
curl -X POST http://localhost:8080/api/payments/stripe/create-payment-intent -H "Content-Type: application/json" -d "{\"amount\":5000,\"currency\":\"USD\",\"customerEmail\":\"test@example.com\",\"customerName\":\"Test User\"}"
```

---

## 💡 Pro Tips

1. **Always use Test Mode keys** (start with `rzp_test_` or `sk_test_`)
2. **No real money is charged** in test mode
3. **Test cards never decline** (except specific test cards)
4. **Amount must be integer** (no decimals)
5. **Minimum amounts:** Razorpay = ₹1 (100 paise), Stripe = $0.50 (50 cents)

---

## 🎉 Ready to Test!

**All data is FREE and NO credit card required!** 🚀

**Start testing:** Import Postman collection and use the test data above!
