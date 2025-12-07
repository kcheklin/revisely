const express = require('express');
const pool = require('../config/db');
const { authenticate } = require('../middleware/auth');
const router = express.Router();

router.use(authenticate);

router.post('/', async (req, res) => {
  const { subject, durationInMinutes, startTime } = req.body;
  if (!subject || !durationInMinutes || !startTime) {
    return res.status(400).json({ error: 'Subject, duration, and start time are required' });
  }
  try {
    const result = await pool.query(
      'INSERT INTO study_sessions (user_id, subject, duration_in_minutes, start_time) VALUES ($1, $2, $3, $4) RETURNING *',
      [req.user.id, subject, durationInMinutes, startTime]
    );
    res.status(201).json({ session: result.rows[0] });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT * FROM study_sessions WHERE user_id = $1 ORDER BY start_time DESC',
      [req.user.id]
    );
    res.json({ sessions: result.rows });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/summary', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT subject, SUM(duration_in_minutes) as total_minutes, COUNT(*) as session_count FROM study_sessions WHERE user_id = $1 GROUP BY subject',
      [req.user.id]
    );
    res.json({ summary: result.rows });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.get('/:id', async (req, res) => {
  try {
    const result = await pool.query(
      'SELECT * FROM study_sessions WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    if (!result.rows[0]) return res.status(404).json({ error: 'Session not found' });
    res.json({ session: result.rows[0] });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.put('/:id', async (req, res) => {
  const { subject, durationInMinutes, startTime, endTime } = req.body;
  try {
    const result = await pool.query(
      'UPDATE study_sessions SET subject = COALESCE($1, subject), duration_in_minutes = COALESCE($2, duration_in_minutes), start_time = COALESCE($3, start_time), end_time = COALESCE($4, end_time) WHERE id = $5 AND user_id = $6 RETURNING *',
      [subject, durationInMinutes, startTime, endTime, req.params.id, req.user.id]
    );
    if (!result.rows[0]) return res.status(404).json({ error: 'Session not found' });
    res.json({ session: result.rows[0] });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.delete('/:id', async (req, res) => {
  try {
    const result = await pool.query(
      'DELETE FROM study_sessions WHERE id = $1 AND user_id = $2 RETURNING *',
      [req.params.id, req.user.id]
    );
    if (!result.rows[0]) return res.status(404).json({ error: 'Session not found' });
    res.json({ message: 'Session deleted' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
