const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { sequelize } = require('./models');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Routes
const tutorRoutes = require('./routes/tutors');
const bookingRoutes = require('./routes/bookings');
const sessionRoutes = require('./routes/sessions');
const reviewRoutes = require('./routes/reviews');
const messageRoutes = require('./routes/messages');

app.use('/api/tutors', tutorRoutes);
app.use('/api/bookings', bookingRoutes);
app.use('/api/sessions', sessionRoutes);
app.use('/api/reviews', reviewRoutes);
app.use('/api/messages', messageRoutes);

// Root endpoint
app.get('/', (req, res) => {
    res.send('Revisely Backend API is running');
});

// Sync database and start server
sequelize.sync().then(() => {
    console.log('Database synced');
    app.listen(PORT, () => {
        console.log(`Server is running on http://localhost:${PORT}`);
    });
}).catch(err => {
    console.error('Failed to sync database:', err);
});
