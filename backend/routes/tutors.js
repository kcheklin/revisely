const express = require('express');
const router = express.Router();
const { Tutor, Review } = require('../models');
const { Op } = require('sequelize');

// Get all tutors with optional filters
router.get('/', async (req, res) => {
    try {
        const { name, subject, rating } = req.query;
        const whereClause = {};

        if (name) {
            whereClause.name = { [Op.like]: `%${name}%` };
        }

        let tutors = await Tutor.findAll({
            where: whereClause,
            include: [Review]
        });

        // Filter by Subject (JSON array check)
        if (subject) {
            tutors = tutors.filter(tutor => {
                const subjects = tutor.subjects || [];
                return subjects.some(s => s.toLowerCase().includes(subject.toLowerCase()));
            });
        }

        // Filter by Date (Check if timeSlots contains the date)
        if (req.query.date) {
            const searchDate = req.query.date; // Expected format: YYYY-MM-DD
            tutors = tutors.filter(tutor => {
                const slots = tutor.timeSlots || [];
                // Check if any slot string starts with the search date
                return slots.some(slot => slot.startsWith(searchDate));
            });
        }

        // Filter by Rating (Calculate average from starRatings)
        if (rating) {
            const minRating = parseFloat(rating);
            tutors = tutors.filter(tutor => {
                const ratings = tutor.starRatings || [0, 0, 0, 0, 0];
                const totalStars = ratings.reduce((acc, count, index) => acc + count * (5 - index), 0);
                const totalReviews = ratings.reduce((acc, count) => acc + count, 0);
                const avgRating = totalReviews === 0 ? 0 : totalStars / totalReviews;
                return avgRating >= minRating;
            });
        }

        res.json(tutors);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Get tutor details
router.get('/:id', async (req, res) => {
    try {
        const tutor = await Tutor.findByPk(req.params.id, {
            include: [Review]
        });
        if (!tutor) return res.status(404).json({ error: 'Tutor not found' });
        res.json(tutor);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
