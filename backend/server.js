require('dotenv').config();
const express = require('express');
const authRoutes = require('./routes/auth');
const sessionsRoutes = require('./routes/sessions');
const { authenticate, authorize } = require('./middleware/auth');
const { apiLimiter, authLimiter } = require('./middleware/rateLimiter');

const app = express();
app.use(express.json());
app.use(apiLimiter);

app.use('/auth', authLimiter, authRoutes);
app.use('/sessions', sessionsRoutes);

app.get('/protected', authenticate, (req, res) => {
  res.json({ message: 'Protected route', user: req.user });
});

app.get('/admin', authenticate, authorize('admin'), (req, res) => {
  res.json({ message: 'Admin only route' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
