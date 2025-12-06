# Revisely Auth Backend

## Setup
1. Install dependencies: `npm install`
2. Configure `.env` (copy from `.env.example`)
3. Create PostgreSQL database and run `schema.sql`
4. Start server: `npm run dev`

## API Routes

### Authentication Routes

#### POST /auth/signup
Register new user

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "pass123",
  "name": "John Doe",
  "role": "student"
}
```

**Response:**
```json
{
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "student"
  }
}
```

---

#### POST /auth/login
Login user and receive JWT tokens

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "pass123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "student"
  }
}
```

---

#### POST /auth/refresh
Refresh access token using refresh token

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

#### POST /auth/password-reset/request
Request password reset OTP via email

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "OTP sent to email"
}
```

---

#### POST /auth/password-reset/verify
Verify OTP and reset password

**Request Body:**
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "newPassword": "newpass123"
}
```

**Response:**
```json
{
  "message": "Password reset successful"
}
```

---

### Protected Routes

#### GET /protected
Test protected route (requires authentication)

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "message": "Protected route",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "role": "student"
  }
}
```

---

#### GET /admin
Admin only route (requires authentication + admin role)

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "message": "Admin only route"
}
```

**Error (if not admin):**
```json
{
  "error": "Access denied"
}
```

---

## Features
- ✅ User registration with email validation
- ✅ JWT-based authentication (access + refresh tokens)
- ✅ Secure password hashing with bcrypt
- ✅ Password reset via email OTP (10-minute expiry)
- ✅ Role-based authorization (student/admin)
- ✅ Token refresh mechanism
- ✅ Authentication middleware
- ✅ PostgreSQL database integration

## Tech Stack
- Node.js + Express
- PostgreSQL
- JWT (jsonwebtoken)
- bcrypt
- nodemailer
