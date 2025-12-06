require('dotenv').config();
const express = require('express');
const authRoutes = require('./routes/auth');
const { authenticate, authorize } = require('./middleware/auth');

const app = express();
app.use(express.json());

app.use('/auth', authRoutes);

app.get('/protected', authenticate, (req, res) => {
  res.json({ message: 'Protected route', user: req.user });
});

app.get('/admin', authenticate, authorize('admin'), (req, res) => {
  res.json({ message: 'Admin only route' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
