const express = require('express');
const router = express.Router();
const { Review } = require('../models');

// Post a review
router.post('/', async (req, res) => {
    try {
        const { bookingId, tutorId, rating, content } = req.body;

        if (!bookingId) {
            return res.status(400).json({ error: 'bookingId is required to verify the review.' });
        }

        const review = await Review.create(req.body);
        res.status(201).json(review);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Get reviews for a tutor
router.get('/', async (req, res) => {
    try {
        const { tutorId } = req.query;
        const whereClause = tutorId ? { tutorId } : {};

        const reviews = await Review.findAll({
            where: whereClause,
            order: [['createdAt', 'DESC']]
        });
        res.json(reviews);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
