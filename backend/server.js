require('dotenv').config({ path: '.env.local' });
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { sequelize } = require('./models');
const { apiLimiter, authLimiter } = require('./middleware/rateLimiter');
const { authenticate, authorize } = require('./middleware/auth');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(bodyParser.json());
app.use(apiLimiter);

// Auth Routes (with auth limiter)
const authRoutes = require('./routes/auth');
app.use('/auth', authLimiter, authRoutes);

// API Routes
const routes = {
  // Tutor Booking System
  tutors: require('./routes/tutors'),
  bookings: require('./routes/bookings'),
  sessions: require('./routes/sessions'),
  reviews: require('./routes/reviews'),
  messages: require('./routes/messages'),
  
  // Module 5: Profile & Analytics
  profiles: require('./routes/profiles'),
  
  // Module 6: Textbook Quizzes
  textbooks: require('./routes/textbooks'),
  quizzes: require('./routes/quizzes')
};

Object.entries(routes).forEach(([name, router]) => {
  app.use(`/api/${name}`, router);
});

// Protected route examples
app.get('/protected', authenticate, (req, res) => {
  res.json({ message: 'Protected route', user: req.user });
});

app.get('/admin', authenticate, authorize('admin'), (req, res) => {
  res.json({ message: 'Admin only route' });
});

// Root endpoint
app.get('/', (req, res) => {
  res.send('Revisely Backend API is running');
});

// Sync database and start server
sequelize.sync()
  .then(() => {
    console.log('Database synced');
    app.listen(PORT, '0.0.0.0', () => {
      console.log(`Server is running on http://0.0.0.0:${PORT}`);
      console.log(`Local Access: http://localhost:${PORT}`);
      console.log(`Network Access: http://10.131.156.144:${PORT}`); 
    });
  })
  .catch(err => {
    console.error('Failed to sync database:', err);
    process.exit(1);
  });

module.exports = app;