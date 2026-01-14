const express = require('express');
const router = express.Router();
const { Message } = require('../models');
const { Op } = require('sequelize');

// Get messages for a user
router.get('/', async (req, res) => {
    try {
        const { userId, otherUserId } = req.query;

        if (!userId) {
            return res.status(400).json({ error: 'userId is required' });
        }

        const whereClause = {
            [Op.or]: [
                { senderId: userId },
                { receiverId: userId }
            ]
        };

        // If chatting with a specific person
        if (otherUserId) {
            whereClause[Op.and] = [
                {
                    [Op.or]: [
                        { senderId: userId, receiverId: otherUserId },
                        { senderId: otherUserId, receiverId: userId }
                    ]
                }
            ];
        }

        const messages = await Message.findAll({
            where: whereClause,
            order: [['timestamp', 'ASC']]
        });
        res.json(messages);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Send message
router.post('/', async (req, res) => {
    try {
        const message = await Message.create({
            ...req.body,
            timestamp: new Date()
        });
        res.status(201).json(message);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

module.exports = router;
