# Quick Setup Guide

## 1. Install PostgreSQL
Download from: https://www.postgresql.org/download/windows/

During installation, remember the password you set for the `postgres` user.

## 2. Create Database
Open Command Prompt or PowerShell and run:
```bash
psql -U postgres
```
Enter your password, then run:
```sql
CREATE DATABASE revisely;
\q
```

## 3. Configure Environment
Copy `.env.example` to `.env`:
```bash
copy .env.example .env
```

Edit `.env` and update:
```env
DATABASE_URL=postgresql://postgres:YOUR_PASSWORD@localhost:5432/revisely
JWT_SECRET=generate-random-string-here
JWT_REFRESH_SECRET=generate-different-random-string-here
EMAIL_USER=your-gmail@gmail.com
EMAIL_PASS=your-gmail-app-password
```

**Generate random secrets:**
- Use a password generator or run: `node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"`

## 4. Gmail App Password (for OTP emails)
1. Go to [Google Account → Security](https://myaccount.google.com/security)
2. Enable 2-Step Verification
3. Search "App passwords" → Generate new app password
4. Copy the 16-character password to `.env` as `EMAIL_PASS`

## 5. Install Dependencies
```bash
npm install
```

## 6. Seed Database
This will create tables and populate with test data:
```bash
npm run seed
```

**Test credentials created:**
- Student: `jessica@example.com` / `password123`
- Student: `john@example.com` / `password123`
- Admin: `admin@example.com` / `password123`

## 7. Run Server
```bash
npm start
```

Server runs on **http://localhost:3000**

## 🎉 Done!

Test the API:
```bash
node test_backend.js
```
