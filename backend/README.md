# Revisely Auth Backend

## Setup
1. Install dependencies: `npm install`
2. Configure `.env` (copy from `.env.example`)
3. Create PostgreSQL database and run `schema.sql`
4. Start server: `npm run dev`

## API Routes

### Study Session Routes

#### POST /sessions
Create new study session (requires authentication)

**Headers:**
```
Authorization: Bearer <access-token>
```

**Request Body:**
```json
{
  "subject": "Mathematics",
  "durationInMinutes": 25,
  "startTime": "2024-12-06T10:00:00Z"
}
```

**Response:**
```json
{
  "session": {
    "id": 1,
    "user_id": 1,
    "subject": "Mathematics",
    "duration_in_minutes": 25,
    "start_time": "2024-12-06T10:00:00Z",
    "end_time": null,
    "created_at": "2024-12-06T10:00:00Z"
  }
}
```

---

#### GET /sessions
Get all study sessions for authenticated user

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "sessions": [
    {
      "id": 1,
      "user_id": 1,
      "subject": "Mathematics",
      "duration_in_minutes": 25,
      "start_time": "2024-12-06T10:00:00Z",
      "end_time": "2024-12-06T10:25:00Z",
      "created_at": "2024-12-06T10:00:00Z"
    }
  ]
}
```

---

#### GET /sessions/summary
Get study session summary grouped by subject

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "summary": [
    {
      "subject": "Mathematics",
      "total_minutes": 150,
      "session_count": 6
    },
    {
      "subject": "Physics",
      "total_minutes": 75,
      "session_count": 3
    }
  ]
}
```

---

#### GET /sessions/:id
Get specific study session by ID

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "session": {
    "id": 1,
    "user_id": 1,
    "subject": "Mathematics",
    "duration_in_minutes": 25,
    "start_time": "2024-12-06T10:00:00Z",
    "end_time": "2024-12-06T10:25:00Z",
    "created_at": "2024-12-06T10:00:00Z"
  }
}
```

---

#### PUT /sessions/:id
Update study session

**Headers:**
```
Authorization: Bearer <access-token>
```

**Request Body:**
```json
{
  "subject": "Advanced Mathematics",
  "durationInMinutes": 30,
  "endTime": "2024-12-06T10:30:00Z"
}
```

**Response:**
```json
{
  "session": {
    "id": 1,
    "user_id": 1,
    "subject": "Advanced Mathematics",
    "duration_in_minutes": 30,
    "start_time": "2024-12-06T10:00:00Z",
    "end_time": "2024-12-06T10:30:00Z",
    "created_at": "2024-12-06T10:00:00Z"
  }
}
```

---

#### DELETE /sessions/:id
Delete study session

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response:**
```json
{
  "message": "Session deleted"
}
```

---

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

### Module 1: Authentication & Authorization
- ✅ User registration with email validation
- ✅ JWT-based authentication (access + refresh tokens)
- ✅ Secure password hashing with bcrypt
- ✅ Password reset via email OTP (10-minute expiry)
- ✅ Role-based authorization (student/admin)
- ✅ Token refresh mechanism
- ✅ Authentication middleware

### Module 2: Countdown Focus Timer
- ✅ CRUD operations for study sessions
- ✅ Session summary endpoint (grouped by subject)
- ✅ Timer logs with timestamps
- ✅ Rate limiting (100 requests per 15 min)
- ✅ Auth rate limiting (5 login attempts per 15 min)

### Shared
- ✅ PostgreSQL database integration
- ✅ API security with rate limiting

## Tech Stack
- Node.js + Express
- PostgreSQL
- JWT (jsonwebtoken)
- bcrypt
- nodemailer
