# Module 5 & 6 API Documentation

## 🎉 Successfully Implemented!

Both Module 5 (Profile & Study Analytics) and Module 6 (Textbook-Based Quizzes) have been fully implemented with complete CRUD operations, relationships, and sample data.

---

## 📊 Module 5: Profile Page & Study Analytics

### Database Tables Created:
- `profiles` - User profile information
- `study_goals` - Study goals and targets
- `motivational_quotes` - Motivational quotes for users

### API Endpoints

#### Profile Management

**GET `/api/profiles/me`** - Get current user's profile (creates if doesn't exist)
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "profile": {
    "id": 1,
    "userId": 1,
    "bio": "Student bio here",
    "profilePicture": "url_or_path",
    "avatarId": 123,
    "theme": "light",
    "notifications": true,
    "User": {...}
  }
}
```

**PUT `/api/profiles/me`** - Update profile
```json
Headers: { "Authorization": "Bearer <token>" }

Request Body:
{
  "bio": "Updated bio",
  "profilePicture": "new_url",
  "avatarId": 456,
  "theme": "dark",
  "notifications": false
}
```

---

#### Study Goals

**GET `/api/profiles/goals`** - Get all study goals for current user
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "goals": [
    {
      "id": 1,
      "userId": 1,
      "subject": "Mathematics",
      "targetHours": 50,
      "currentHours": 15.5,
      "deadline": "2026-03-01",
      "completed": false,
      "priority": "high",
      "description": "Master calculus for final exams"
    }
  ]
}
```

**POST `/api/profiles/goals`** - Create new study goal
```json
Headers: { "Authorization": "Bearer <token>" }

Request Body:
{
  "subject": "Physics",
  "targetHours": 30,
  "deadline": "2026-02-15",
  "priority": "medium",
  "description": "Complete mechanics module"
}
```

**PUT `/api/profiles/goals/:id`** - Update study goal
```json
Headers: { "Authorization": "Bearer <token>" }

Request Body:
{
  "currentHours": 20,
  "completed": false
}
```

**DELETE `/api/profiles/goals/:id`** - Delete study goal
```json
Headers: { "Authorization": "Bearer <token>" }
```

---

#### Study Analytics

**GET `/api/profiles/analytics/summary`** - Get overall study summary
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "summary": {
    "totalMinutes": 500,
    "totalHours": 8.3,
    "totalSessions": 15,
    "recentSessions": 5,
    "bySubject": [
      {
        "subject": "Mathematics",
        "totalMinutes": "300",
        "sessionCount": "10"
      },
      {
        "subject": "Physics",
        "totalMinutes": "200",
        "sessionCount": "5"
      }
    ]
  }
}
```

**GET `/api/profiles/analytics/daily?date=2026-01-15`** - Get daily analytics (for pie chart)
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "date": "2026-01-15T00:00:00.000Z",
  "data": [
    {
      "subject": "Mathematics",
      "minutes": 45,
      "hours": 0.8
    },
    {
      "subject": "Physics",
      "minutes": 30,
      "hours": 0.5
    }
  ]
}
```

**GET `/api/profiles/analytics/weekly`** - Get weekly analytics (last 7 days)
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "period": {
    "start": "2026-01-08T...",
    "end": "2026-01-15T..."
  },
  "data": [
    {
      "subject": "Mathematics",
      "minutes": 180,
      "hours": 3
    }
  ]
}
```

**GET `/api/profiles/analytics/monthly`** - Get monthly analytics (last 30 days)
```json
Headers: { "Authorization": "Bearer <token>" }

Response: (same structure as weekly)
```

---

#### Motivational Quotes

**GET `/api/profiles/quotes/random?category=motivation`** - Get random quote
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "quote": {
    "id": 1,
    "quote": "The only way to do great work is to love what you do.",
    "author": "Steve Jobs",
    "category": "motivation",
    "language": "en",
    "likes": 0
  }
}
```

**GET `/api/profiles/quotes?category=learning&limit=5`** - Get quotes list
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "quotes": [...]
}
```

---

## 📚 Module 6: Textbook-Based Quizzes

### Database Tables Created:
- `textbooks` - Textbook information
- `chapters` - Textbook chapters
- `quizzes` - Quizzes for chapters
- `quiz_questions` - Quiz questions
- `quiz_user_results` - User quiz results

### API Endpoints

#### Textbooks (Public)

**GET `/api/textbooks`** - Get all textbooks
```json
Query Params: ?subject=Mathematics&grade=Form 4&level=Intermediate&search=algebra

Response:
{
  "textbooks": [
    {
      "id": 1,
      "title": "Advanced Mathematics for Form 4",
      "subject": "Mathematics",
      "grade": "Form 4",
      "level": "Intermediate",
      "coverImage": "url",
      "description": "...",
      "publisher": "Educational Press",
      "isbn": "978-1234567890",
      "yearPublished": 2023,
      "language": "English",
      "Chapters": [...]
    }
  ]
}
```

**GET `/api/textbooks/:id`** - Get single textbook with chapters and quizzes
```json
Response:
{
  "textbook": {
    "id": 1,
    "title": "...",
    "Chapters": [
      {
        "id": 1,
        "chapterNumber": 1,
        "title": "Quadratic Equations",
        "description": "...",
        "topics": ["Factoring", "..."],
        "duration": 120,
        "Quizzes": [...]
      }
    ]
  }
}
```

**GET `/api/textbooks/meta/subjects`** - Get unique subjects list
```json
Response:
{
  "subjects": ["Mathematics", "Physics", "Computer Science"]
}
```

---

#### Chapters

**GET `/api/textbooks/:textbookId/chapters/:chapterId`** - Get specific chapter
```json
Response:
{
  "chapter": {
    "id": 1,
    "textbookId": 1,
    "chapterNumber": 1,
    "title": "Quadratic Equations",
    "description": "...",
    "topics": [...],
    "duration": 120,
    "Textbook": {...},
    "Quizzes": [...]
  }
}
```

---

#### Quizzes (Public Listing)

**GET `/api/quizzes?chapterId=1&difficulty=medium`** - Get all quizzes
```json
Response:
{
  "quizzes": [
    {
      "id": 1,
      "chapterId": 1,
      "title": "Quadratic Equations - Practice Quiz",
      "description": "...",
      "difficulty": "medium",
      "timeLimit": 30,
      "passingScore": 70,
      "totalQuestions": 3,
      "isActive": true,
      "Chapter": {
        "Textbook": {...}
      }
    }
  ]
}
```

**GET `/api/quizzes/:id`** - Get quiz details with questions (NO ANSWERS)
```json
Response:
{
  "quiz": {
    "id": 1,
    "title": "...",
    "QuizQuestions": [
      {
        "id": 1,
        "questionNumber": 1,
        "questionText": "Solve for x: x² - 5x + 6 = 0",
        "questionType": "multiple-choice",
        "options": ["x = 2 or x = 3", "x = 1 or x = 6", "..."],
        "points": 2,
        "difficulty": "medium"
        // NOTE: correctAnswer and explanation NOT included
      }
    ]
  }
}
```

---

#### Quiz Taking & Submission (Authenticated)

**POST `/api/quizzes/:id/submit`** - Submit quiz answers
```json
Headers: { "Authorization": "Bearer <token>" }

Request Body:
{
  "answers": [
    {
      "questionId": 1,
      "answer": "0"  // Index for multiple choice, or text for others
    },
    {
      "questionId": 2,
      "answer": "0"
    },
    {
      "questionId": 3,
      "answer": "false"
    }
  ],
  "timeTaken": 300  // seconds
}

Response:
{
  "result": {
    "id": 1,
    "score": 75.00,
    "totalQuestions": 3,
    "correctAnswers": 2,
    "passed": true,
    "timeTaken": 300,
    "answers": [
      {
        "questionId": 1,
        "userAnswer": "0",
        "correctAnswer": "0",
        "isCorrect": true,
        "points": 2,
        "explanation": "Factor as (x-2)(x-3) = 0, so x = 2 or x = 3"
      },
      // ... more answers with explanations
    ]
  }
}
```

**GET `/api/quizzes/:id/results`** - Get user's results for a specific quiz
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "results": [
    {
      "id": 1,
      "score": 75.00,
      "passed": true,
      "completedAt": "2025-11-20T15:30:00Z",
      "Quiz": {...}
    }
  ]
}
```

**GET `/api/quizzes/:quizId/results/:resultId`** - Get specific result details
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "result": {
    "id": 1,
    "score": 75.00,
    "totalQuestions": 3,
    "correctAnswers": 2,
    "answers": [...],
    "passed": true,
    "completedAt": "...",
    "Quiz": {...}
  }
}
```

**GET `/api/quizzes/results/me`** - Get all user's quiz results
```json
Headers: { "Authorization": "Bearer <token>" }

Response:
{
  "results": [...]
}
```

---

#### Admin Routes - Quiz Management

**POST `/api/textbooks`** - Create textbook (admin only)
```json
Headers: { "Authorization": "Bearer <admin-token>" }

Request Body:
{
  "title": "New Textbook",
  "subject": "Physics",
  "grade": "Form 5",
  "level": "Advanced",
  "description": "...",
  "publisher": "...",
  "isbn": "...",
  "yearPublished": 2024
}
```

**PUT `/api/textbooks/:id`** - Update textbook (admin only)
**DELETE `/api/textbooks/:id`** - Delete textbook (admin only)

**POST `/api/textbooks/:textbookId/chapters`** - Create chapter (admin only)
```json
Headers: { "Authorization": "Bearer <admin-token>" }

Request Body:
{
  "chapterNumber": 1,
  "title": "Chapter Title",
  "description": "...",
  "topics": ["Topic 1", "Topic 2"],
  "duration": 90
}
```

**PUT `/api/textbooks/:textbookId/chapters/:chapterId`** - Update chapter (admin only)
**DELETE `/api/textbooks/:textbookId/chapters/:chapterId`** - Delete chapter (admin only)

**POST `/api/quizzes`** - Create quiz (admin only)
```json
Headers: { "Authorization": "Bearer <admin-token>" }

Request Body:
{
  "chapterId": 1,
  "title": "Quiz Title",
  "description": "...",
  "difficulty": "medium",
  "timeLimit": 30,
  "passingScore": 70
}
```

**PUT `/api/quizzes/:id`** - Update quiz (admin only)
**DELETE `/api/quizzes/:id`** - Delete quiz (admin only)

**POST `/api/quizzes/:quizId/questions`** - Add question to quiz (admin only)
```json
Headers: { "Authorization": "Bearer <admin-token>" }

Request Body:
{
  "questionNumber": 1,
  "questionText": "What is 2+2?",
  "questionType": "multiple-choice",
  "options": ["3", "4", "5", "6"],
  "correctAnswer": "1",  // Index or text
  "explanation": "2+2 equals 4",
  "points": 1,
  "difficulty": "easy"
}
```

**PUT `/api/quizzes/:quizId/questions/:questionId`** - Update question (admin only)
**DELETE `/api/quizzes/:quizId/questions/:questionId`** - Delete question (admin only)

---

## 🗄️ Database Schema

### Module 5 Tables:

```sql
profiles (
  id, user_id (FK->users), bio, profile_picture, avatar_id, 
  theme, notifications, created_at, updated_at
)

study_goals (
  id, user_id (FK->users), subject, target_hours, current_hours,
  deadline, completed, priority, description, created_at, updated_at
)

motivational_quotes (
  id, quote, author, category, language, likes, created_at, updated_at
)
```

### Module 6 Tables:

```sql
textbooks (
  id, title, subject, grade, level, cover_image, description,
  publisher, isbn, year_published, language, created_at, updated_at
)

chapters (
  id, textbook_id (FK->textbooks), chapter_number, title,
  description, topics (JSONB), duration, created_at, updated_at
)

quizzes (
  id, chapter_id (FK->chapters), title, description, difficulty,
  is_auto_generated, time_limit, passing_score, total_questions,
  is_active, created_at, updated_at
)

quiz_questions (
  id, quiz_id (FK->quizzes), question_number, question_text,
  question_type, options (JSONB), correct_answer, explanation,
  points, difficulty, created_at, updated_at
)

quiz_user_results (
  id, quiz_id (FK->quizzes), user_id (FK->users), score,
  total_questions, correct_answers, answers (JSONB), time_taken,
  passed, completed_at, created_at, updated_at
)
```

---

## 🧪 Sample Data Seeded

After running `npm run seed`, you'll have:

- ✅ **3 Textbooks** (Math, Physics, CS)
- ✅ **4 Chapters** across textbooks
- ✅ **2 Quizzes** with 6 total questions
- ✅ **1 Quiz Result** for testing
- ✅ **2 User Profiles**
- ✅ **3 Study Goals**
- ✅ **10 Motivational Quotes**

---

## 🚀 Testing the APIs

### 1. Start the server:
```bash
npm start
```

### 2. Test authentication:
```bash
POST http://localhost:3000/auth/login
Body: { "email": "jessica@example.com", "password": "password123" }
```

### 3. Test profile endpoints:
```bash
GET http://localhost:3000/api/profiles/me
Headers: { "Authorization": "Bearer <token>" }
```

### 4. Test quizzes:
```bash
GET http://localhost:3000/api/textbooks
GET http://localhost:3000/api/quizzes
GET http://localhost:3000/api/quizzes/1
POST http://localhost:3000/api/quizzes/1/submit
Headers: { "Authorization": "Bearer <token>" }
Body: { "answers": [...], "timeTaken": 300 }
```

---

## 📊 Features Implemented

### Module 5:
- ✅ Profile CRUD operations
- ✅ Profile picture & avatar support
- ✅ Study goals management
- ✅ Daily/Weekly/Monthly analytics
- ✅ Pie chart data endpoints
- ✅ Study summary by subject
- ✅ Random motivational quotes
- ✅ Quote categories

### Module 6:
- ✅ Textbook listing & search
- ✅ Chapter management
- ✅ Quiz CRUD operations
- ✅ Question management
- ✅ Multiple question types (MC, T/F, Short Answer, Essay)
- ✅ Automatic scoring
- ✅ Answer explanations
- ✅ Quiz results tracking
- ✅ User performance history
- ✅ Passing score validation
- ✅ Time tracking

---

## 🎯 Next Steps

1. **Test all endpoints** with Postman or your frontend
2. **Add more sample data** if needed
3. **Implement AI quiz generation** (optional enhancement)
4. **Add image upload** for profile pictures
5. **Add quiz analytics** (performance over time)

---

## ✅ All Done!

Both Module 5 and Module 6 are fully implemented and ready to use! 🎉

