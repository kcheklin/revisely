const express = require('express');
const router = express.Router();
const { Booking, Tutor, StudySession, User } = require('../models');
const { Op } = require('sequelize');
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
    const session = await StudySession.create({
      userId: req.user.id,
      subject,
      durationInMinutes,
      startTime
    });
    res.status(201).json({ session });
  } catch (error) {
    handleError(res, error);
  }
});

// Get all study sessions for the authenticated user
router.get('/study', async (req, res) => {
  try {
    const sessions = await StudySession.findAll({
      where: { userId: req.user.id },
      order: [['startTime', 'DESC']],
      include: [{
        model: User,
        attributes: ['id', 'name', 'email']
      }]
    });
    res.json({ sessions });
  } catch (error) {
    handleError(res, error);
  }
});

// Get study sessions summary by subject
router.get('/study/summary', async (req, res) => {
  try {
    const { sequelize } = require('../models');
    const summary = await StudySession.findAll({
      where: { userId: req.user.id },
      attributes: [
        'subject',
        [sequelize.fn('SUM', sequelize.col('duration_in_minutes')), 'totalMinutes'],
        [sequelize.fn('COUNT', '*'), 'sessionCount']
      ],
      group: ['subject'],
      raw: true
    });
    res.json({ summary });
  } catch (error) {
    handleError(res, error);
  }
});

// Get a specific study session
router.get('/study/:id', async (req, res) => {
  try {
    const session = await StudySession.findOne({
      where: { 
        id: req.params.id,
        userId: req.user.id
      },
      include: [{
        model: User,
        attributes: ['id', 'name', 'email']
      }]
    });
    
    if (!session) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    res.json({ session });
  } catch (error) {
    handleError(res, error);
  }
});

// Update a study session
router.put('/study/:id', async (req, res) => {
  const { subject, durationInMinutes, startTime, endTime } = req.body;
  
  try {
    const session = await StudySession.findOne({
      where: { 
        id: req.params.id,
        userId: req.user.id
      }
    });
    
    if (!session) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    const updateData = {};
    if (subject !== undefined) updateData.subject = subject;
    if (durationInMinutes !== undefined) updateData.durationInMinutes = durationInMinutes;
    if (startTime !== undefined) updateData.startTime = startTime;
    if (endTime !== undefined) updateData.endTime = endTime;
    
    await session.update(updateData);
    res.json({ session });
  } catch (error) {
    handleError(res, error);
  }
});

// Delete a study session
router.delete('/study/:id', async (req, res) => {
  try {
    const session = await StudySession.findOne({
      where: { 
        id: req.params.id,
        userId: req.user.id
      }
    });
    
    if (!session) {
      return res.status(404).json({ error: 'Session not found' });
    }
    
    await session.destroy();
    res.json({ message: 'Session deleted' });
  } catch (error) {
    handleError(res, error);
  }
});

module.exports = router;