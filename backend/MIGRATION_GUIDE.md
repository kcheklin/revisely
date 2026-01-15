# PostgreSQL Migration Guide

## 📋 What Changed?

Your backend has been successfully migrated from a **hybrid SQLite + PostgreSQL** setup to a **unified PostgreSQL** database!

### Before (Hybrid Setup):
- ❌ SQLite for tutors, bookings, reviews, messages
- ❌ PostgreSQL for users, auth, study sessions
- ❌ No referential integrity between databases
- ❌ Complex setup and maintenance

### After (Unified PostgreSQL):
- ✅ Single PostgreSQL database for everything
- ✅ Proper foreign key relationships
- ✅ Better data integrity and consistency
- ✅ Production-ready architecture
- ✅ Simplified deployment

---

## 🔄 Files Modified

### **New Model Files Created:**
1. `models/User.js` - User authentication model
2. `models/RefreshToken.js` - JWT refresh tokens
3. `models/PasswordReset.js` - Password reset OTPs
4. `models/StudySession.js` - Study session tracking

### **Updated Model Files:**
1. `models/index.js` - Changed from SQLite to PostgreSQL with Sequelize
2. `models/Tutor.js` - Added PostgreSQL types (JSONB), snake_case fields
3. `models/Booking.js` - Added proper foreign keys and constraints
4. `models/Review.js` - Added validation and relationships
5. `models/Message.js` - Added user relationships

### **Updated Route Files:**
1. `routes/auth.js` - Now uses Sequelize models instead of raw SQL
2. `routes/sessions.js` - Study sessions now use Sequelize

### **Updated Configuration:**
1. `seed.js` - Now creates users, tutors, bookings, and study sessions
2. `SETUP.md` - Simplified setup instructions
3. `.env.example` - Created with all required variables

### **Files You Can Delete:**
1. `database.sqlite` - No longer needed (SQLite database)
2. `config/db.js` - Replaced by Sequelize connection in models/index.js

---

## 🗄️ Database Schema

### Tables Created by Sequelize:

#### **Authentication Tables:**
- `users` - User accounts (students, tutors, admins)
- `refresh_tokens` - JWT refresh tokens
- `password_resets` - OTP codes for password reset

#### **Tutor Booking Tables:**
- `tutors` - Tutor profiles and availability
- `bookings` - Tutor session bookings
- `reviews` - Tutor reviews and ratings
- `messages` - User-to-user messaging

#### **Study Session Tables:**
- `study_sessions` - Personal study timer sessions

### Relationships:
```
users (1) ──→ (N) bookings
users (1) ──→ (N) study_sessions
users (1) ──→ (N) refresh_tokens
users (1) ──→ (N) password_resets
users (1) ──→ (N) messages (as sender)
users (1) ──→ (N) messages (as receiver)

tutors (1) ──→ (N) bookings
tutors (1) ──→ (N) reviews

bookings (1) ──→ (N) reviews
```

---

## 🚀 How to Set Up (Fresh Install)

### 1. Install PostgreSQL
Download from: https://www.postgresql.org/download/windows/

### 2. Create Database
```bash
psql -U postgres
CREATE DATABASE revisely;
\q
```

### 3. Configure Environment
```bash
copy .env.example .env
```

Edit `.env` with your settings:
```env
DATABASE_URL=postgresql://postgres:YOUR_PASSWORD@localhost:5432/revisely
JWT_SECRET=your-secret-key
JWT_REFRESH_SECRET=your-refresh-secret-key
EMAIL_USER=your-email@gmail.com
EMAIL_PASS=your-app-password
```

### 4. Install & Seed
```bash
npm install
npm run seed
```

### 5. Run Server
```bash
npm start
```

---

## 🧪 Testing

### Test Credentials (created by seed):
- Student: `jessica@example.com` / `password123`
- Student: `john@example.com` / `password123`
- Admin: `admin@example.com` / `password123`

### Run Tests:
```bash
node test_backend.js
node verify_all.js
```

---

## 📊 Benefits of This Migration

### 1. **Data Integrity**
- Foreign keys enforce relationships
- Cascading deletes maintain consistency
- Constraints prevent invalid data

### 2. **Simplified Code**
- Single ORM (Sequelize) for all operations
- No more mixing raw SQL and ORM
- Consistent error handling

### 3. **Production Ready**
- PostgreSQL handles concurrent connections
- Better performance under load
- SSL support for remote databases

### 4. **Easier Deployment**
- Single DATABASE_URL environment variable
- Works with Heroku, Railway, Render, etc.
- No file persistence issues

### 5. **Developer Experience**
- Type safety with Sequelize
- Automatic timestamps (created_at, updated_at)
- Better query builder and associations

---

## 🔧 Troubleshooting

### Issue: "DATABASE_URL is not defined"
**Solution:** Make sure you have a `.env` file with `DATABASE_URL` set

### Issue: "relation 'users' does not exist"
**Solution:** Run `npm run seed` to create tables

### Issue: "password authentication failed"
**Solution:** Check your PostgreSQL password in `DATABASE_URL`

### Issue: "Connection refused"
**Solution:** Make sure PostgreSQL service is running

### Issue: Port already in use
**Solution:** Change `PORT` in `.env` or stop the other process

---

## 📝 Notes

- **Sequelize Auto-Migration:** The app uses `sequelize.sync()` which auto-creates tables
- **Timestamps:** All tables now have `created_at` and `updated_at` columns
- **JSONB Type:** PostgreSQL's JSONB is used for array/object fields (better than JSON)
- **Snake Case:** Database columns use snake_case (e.g., `user_id`, `created_at`)
- **Camel Case:** JavaScript code uses camelCase (Sequelize handles conversion)

---

## 🎉 Migration Complete!

Your backend is now running on a single, unified PostgreSQL database with proper relationships and production-ready architecture!

For questions or issues, check the main README.md or SETUP.md.

