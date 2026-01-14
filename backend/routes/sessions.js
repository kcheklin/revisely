const express = require('express');
const router = express.Router();
const { Booking, Tutor } = require('../models');
const { Op } = require('sequelize');
const pool = require('../config/db');
const { authenticate } = require('../middleware/auth');

// Helper function for error responses
const handleError = (res, err, statusCode = 500) => {
  console.error(err);
  res.status(statusCode).json({ error: err.message });
};

// Helper function to validate required fields
const validateFields = (fields, requiredFields) => {
  const missing = requiredFields.filter(field => !fields[field]);
  return missing.length > 0 ? missing : null;
};

// ============================================
// TUTOR BOOKING SESSIONS (SQLite/Sequelize)
// ============================================

// Get sessions by status (upcoming or past)
const getSessionsByStatus = async (req, res, isUpcoming = true) => {
  try {
    const { userId } = req.query;
    if (!userId) {
      return res.status(400).json({ error: 'userId is required' });
    }

    const whereClause = {
      userId: userId,
      status: isUpcoming ? 'Upcoming' : { [Op.ne]: 'Upcoming' }
    };

    const sessions = await Booking.findAll({
      where: whereClause,
      include: [Tutor]
    });
    
    res.json(sessions);
  } catch (err) {
    handleError(res, err);
  }
};

router.get('/upcoming', (req, res) => getSessionsByStatus(req, res, true));
router.get('/past', (req, res) => getSessionsByStatus(req, res, false));

// ============================================
// STUDY SESSIONS (PostgreSQL)
// ============================================

// Apply authentication middleware to study session routes
router.use('/study', authenticate);

// Create a new study session
router.post('/study', async (req, res) => {
  const { subject, durationInMinutes, startTime } = req.body;
  
  const missing = validateFields(req.body, ['subject', 'durationInMinutes', 'startTime']);
  if (missing) {
    return res.status(400).json({ 
      error: `Missing required fields: ${missing.join(', ')}` 
    });
  }

  try {
    const result = await pool.query(
      'INSERT INTO study_sessions (user_id, subject, duration_in_minutes, start_time) VALUES ($1, $2, $3, $4) RETURNING *',
      [req.user.id, subject, durationInMinutes, startTime]
    );
    res.status(201).json({ session: result.rows[0] });
  } catch (error) {
    handleError(res, error);
  }
});

// Get all study sessions for the authenticated user
router.get('/study', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT * FROM study_sessions WHERE user_id = $1 ORDER BY start_time DESC',
      [req.user.id]
    );
    res.json({ sessions: result.rows });
  } catch (error) {
    handleError(res, error);
  }
});

// Get study sessions summary by subject
router.get('/study/summary', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT subject, SUM(duration_in_minutes) as total_minutes, COUNT(*) as session_count FROM study_sessions WHERE user_id = $1 GROUP BY subject',
      [req.user.id]
    );
    res.json({ summary: result.rows });
  } catch (error) {
    handleError(res, error);
  }
});

// Get a specific study session
router.get('/study/:id', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT * FROM study_sessions WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    
    if (!result.rows[0]) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    res.json({ session: result.rows[0] });
  } catch (error) {
    handleError(res, error);
  }
});

// Update a study session
router.put('/study/:id', async (req, res) => {
  const { subject, durationInMinutes, startTime, endTime } = req.body;
  
  try {
    const result = await pool.query(
      'UPDATE study_sessions SET subject = COALESCE($1, subject), duration_in_minutes = COALESCE($2, duration_in_minutes), start_time = COALESCE($3, start_time), end_time = COALESCE($4, end_time) WHERE id = $5 AND user_id = $6 RETURNING *',
      [subject, durationInMinutes, startTime, endTime, req.params.id, req.user.id]
    );
    
    if (!result.rows[0]) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    res.json({ session: result.rows[0] });
  } catch (error) {
    handleError(res, error);
  }
});

// Delete a study session
router.delete('/study/:id', async (req, res) => {
  try {
    const result = await pool.query(
      'DELETE FROM study_sessions WHERE id = $1 AND user_id = $2 RETURNING *',
      [req.params.id, req.user.id]
    );
    
    if (!result.rows[0]) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    res.json({ message: 'Session deleted' });
  } catch (error) {
    handleError(res, error);
  }
});

module.exports = router;