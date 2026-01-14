# Quick Setup Guide

## 1. Install PostgreSQL
Download from: https://www.postgresql.org/download/windows/

During installation, remember the password you set for the `postgres` user.

## 2. Create Database
Open Command Prompt and run:
```bash
psql -U postgres
```
Enter your password, then run:
```sql
CREATE DATABASE revisely;
\q
```

## 3. Run Schema
```bash
psql -U postgres -d revisely -f schema.sql
```

## 4. Configure Environment
Copy `.env.example` to `.env`:
```bash
copy .env.example .env
```

Edit `.env` and update:
```
DATABASE_URL=postgresql://postgres:YOUR_PASSWORD@localhost:5432/revisely
JWT_SECRET=generate-random-string-here
JWT_REFRESH_SECRET=generate-different-random-string-here
EMAIL_USER=your-gmail@gmail.com
EMAIL_PASS=your-gmail-app-password
```

## 5. Gmail App Password (for OTP emails)
1. Go to Google Account → Security
2. Enable 2-Step Verification
3. Search "App passwords" → Generate new app password
4. Copy the 16-character password to `.env` as `EMAIL_PASS`

## 6. Install & Run
```bash
npm install
npm run dev
```

Server runs on http://localhost:3000
