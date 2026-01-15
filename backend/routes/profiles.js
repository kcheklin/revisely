const express = require('express');
const router = express.Router();
const { Profile, StudyGoal, MotivationalQuote, User, StudySession } = require('../models');
const { authenticate } = require('../middleware/auth');
const { Op } = require('sequelize');

// Apply authentication to all profile routes
router.use(authenticate);

// ============================================
// PROFILE CRUD
// ============================================

// Get current user's profile (create if doesn't exist)
router.get('/me', async (req, res) => {
    try {
        let profile = await Profile.findOne({
            where: { userId: req.user.id },
            include: [{
                model: User,
                attributes: ['id', 'name', 'email', 'role']
            }]
        });

        // Create profile if it doesn't exist
        if (!profile) {
            profile = await Profile.create({ userId: req.user.id });
            profile = await Profile.findOne({
                where: { userId: req.user.id },
                include: [{
                    model: User,
                    attributes: ['id', 'name', 'email', 'role']
                }]
            });
        }

        res.json({ profile });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Update profile
router.put('/me', async (req, res) => {
    try {
        const { bio, profilePicture, avatarId, theme, notifications } = req.body;

        let profile = await Profile.findOne({ where: { userId: req.user.id } });

        if (!profile) {
            // Create if doesn't exist
            profile = await Profile.create({
                userId: req.user.id,
                bio,
                profilePicture,
                avatarId,
                theme,
                notifications
            });
        } else {
            // Update existing
            await profile.update({
                bio: bio !== undefined ? bio : profile.bio,
                profilePicture: profilePicture !== undefined ? profilePicture : profile.profilePicture,
                avatarId: avatarId !== undefined ? avatarId : profile.avatarId,
                theme: theme !== undefined ? theme : profile.theme,
                notifications: notifications !== undefined ? notifications : profile.notifications
            });
        }

        res.json({ profile });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// ============================================
// STUDY GOALS
// ============================================

// Get all study goals for current user
router.get('/goals', async (req, res) => {
    try {
        const goals = await StudyGoal.findAll({
            where: { userId: req.user.id },
            order: [['deadline', 'ASC'], ['priority', 'DESC']]
        });
        res.json({ goals });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Create new study goal
router.post('/goals', async (req, res) => {
    try {
        const { subject, targetHours, deadline, priority, description } = req.body;

        if (!subject || !targetHours) {
            return res.status(400).json({ error: 'Subject and targetHours are required' });
        }

        const goal = await StudyGoal.create({
            userId: req.user.id,
            subject,
            targetHours,
            deadline,
            priority,
            description
        });

        res.status(201).json({ goal });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Update study goal
router.put('/goals/:id', async (req, res) => {
    try {
        const goal = await StudyGoal.findOne({
            where: { id: req.params.id, userId: req.user.id }
        });

        if (!goal) {
            return res.status(404).json({ error: 'Goal not found' });
        }

        const { subject, targetHours, currentHours, deadline, completed, priority, description } = req.body;

        await goal.update({
            subject: subject !== undefined ? subject : goal.subject,
            targetHours: targetHours !== undefined ? targetHours : goal.targetHours,
            currentHours: currentHours !== undefined ? currentHours : goal.currentHours,
            deadline: deadline !== undefined ? deadline : goal.deadline,
            completed: completed !== undefined ? completed : goal.completed,
            priority: priority !== undefined ? priority : goal.priority,
            description: description !== undefined ? description : goal.description
        });

        res.json({ goal });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Delete study goal
router.delete('/goals/:id', async (req, res) => {
    try {
        const goal = await StudyGoal.findOne({
            where: { id: req.params.id, userId: req.user.id }
        });

        if (!goal) {
            return res.status(404).json({ error: 'Goal not found' });
        }

        await goal.destroy();
        res.json({ message: 'Goal deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// STUDY ANALYTICS
// ============================================

// Get study analytics summary
router.get('/analytics/summary', async (req, res) => {
    try {
        const { sequelize } = require('../models');

        // Total study time
        const totalTime = await StudySession.sum('durationInMinutes', {
            where: { userId: req.user.id }
        }) || 0;

        // Total sessions
        const totalSessions = await StudySession.count({
            where: { userId: req.user.id }
        });

        // Study by subject
        const bySubject = await StudySession.findAll({
            where: { userId: req.user.id },
            attributes: [
                'subject',
                [sequelize.fn('SUM', sequelize.col('duration_in_minutes')), 'totalMinutes'],
                [sequelize.fn('COUNT', '*'), 'sessionCount']
            ],
            group: ['subject'],
            raw: true
        });

        // Recent sessions (last 7 days)
        const sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);

        const recentSessions = await StudySession.count({
            where: {
                userId: req.user.id,
                startTime: { [Op.gte]: sevenDaysAgo }
            }
        });

        res.json({
            summary: {
                totalMinutes: Math.round(totalTime),
                totalHours: Math.round(totalTime / 60 * 10) / 10,
                totalSessions,
                recentSessions,
                bySubject
            }
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get daily analytics (for pie chart)
router.get('/analytics/daily', async (req, res) => {
    try {
        const { date } = req.query;
        const targetDate = date ? new Date(date) : new Date();
        
        const startOfDay = new Date(targetDate.setHours(0, 0, 0, 0));
        const endOfDay = new Date(targetDate.setHours(23, 59, 59, 999));

        const sessions = await StudySession.findAll({
            where: {
                userId: req.user.id,
                startTime: {
                    [Op.between]: [startOfDay, endOfDay]
                }
            },
            attributes: ['subject', 'durationInMinutes'],
            raw: true
        });

        // Group by subject
        const bySubject = sessions.reduce((acc, session) => {
            if (!acc[session.subject]) {
                acc[session.subject] = 0;
            }
            acc[session.subject] += session.durationInMinutes;
            return acc;
        }, {});

        const chartData = Object.keys(bySubject).map(subject => ({
            subject,
            minutes: bySubject[subject],
            hours: Math.round(bySubject[subject] / 60 * 10) / 10
        }));

        res.json({ date: startOfDay, data: chartData });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get weekly analytics
router.get('/analytics/weekly', async (req, res) => {
    try {
        const today = new Date();
        const weekAgo = new Date(today);
        weekAgo.setDate(weekAgo.getDate() - 7);

        const sessions = await StudySession.findAll({
            where: {
                userId: req.user.id,
                startTime: { [Op.gte]: weekAgo }
            },
            attributes: ['subject', 'durationInMinutes', 'startTime'],
            order: [['startTime', 'ASC']],
            raw: true
        });

        // Group by subject
        const bySubject = sessions.reduce((acc, session) => {
            if (!acc[session.subject]) {
                acc[session.subject] = 0;
            }
            acc[session.subject] += session.durationInMinutes;
            return acc;
        }, {});

        const chartData = Object.keys(bySubject).map(subject => ({
            subject,
            minutes: bySubject[subject],
            hours: Math.round(bySubject[subject] / 60 * 10) / 10
        }));

        res.json({ 
            period: { start: weekAgo, end: today },
            data: chartData 
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get monthly analytics
router.get('/analytics/monthly', async (req, res) => {
    try {
        const today = new Date();
        const monthAgo = new Date(today);
        monthAgo.setMonth(monthAgo.getMonth() - 1);

        const sessions = await StudySession.findAll({
            where: {
                userId: req.user.id,
                startTime: { [Op.gte]: monthAgo }
            },
            attributes: ['subject', 'durationInMinutes', 'startTime'],
            order: [['startTime', 'ASC']],
            raw: true
        });

        // Group by subject
        const bySubject = sessions.reduce((acc, session) => {
            if (!acc[session.subject]) {
                acc[session.subject] = 0;
            }
            acc[session.subject] += session.durationInMinutes;
            return acc;
        }, {});

        const chartData = Object.keys(bySubject).map(subject => ({
            subject,
            minutes: bySubject[subject],
            hours: Math.round(bySubject[subject] / 60 * 10) / 10
        }));

        res.json({ 
            period: { start: monthAgo, end: today },
            data: chartData 
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// MOTIVATIONAL QUOTES
// ============================================

// Get random quote
router.get('/quotes/random', async (req, res) => {
    try {
        const { category } = req.query;

        const where = category ? { category } : {};

        const count = await MotivationalQuote.count({ where });
        
        if (count === 0) {
            return res.status(404).json({ error: 'No quotes found' });
        }

        const randomIndex = Math.floor(Math.random() * count);

        const quote = await MotivationalQuote.findOne({
            where,
            offset: randomIndex,
            limit: 1
        });

        res.json({ quote });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get all quotes (optional, for admin or browsing)
router.get('/quotes', async (req, res) => {
    try {
        const { category, limit = 10 } = req.query;

        const where = category ? { category } : {};

        const quotes = await MotivationalQuote.findAll({
            where,
            limit: parseInt(limit),
            order: [['likes', 'DESC']]
        });

        res.json({ quotes });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;

