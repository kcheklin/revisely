const express = require('express');
const router = express.Router();
const { Booking, Tutor } = require('../models');
const { Op } = require('sequelize');

// Get upcoming sessions for a user
router.get('/upcoming', async (req, res) => {
    try {
        const { userId } = req.query;
        if (!userId) {
            return res.status(400).json({ error: 'userId is required' });
        }

        const sessions = await Booking.findAll({
            where: {
                userId: userId,
                status: 'Upcoming'
            },
            include: [Tutor]
        });
        res.json(sessions);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Get past sessions for a user
router.get('/past', async (req, res) => {
    try {
        const { userId } = req.query;
        if (!userId) {
            return res.status(400).json({ error: 'userId is required' });
        }

        const sessions = await Booking.findAll({
            where: {
                userId: userId,
                status: { [Op.ne]: 'Upcoming' }
            },
            include: [Tutor]
        });
        res.json(sessions);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
