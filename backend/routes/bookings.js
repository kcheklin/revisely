const express = require('express');
const router = express.Router();
const { Booking } = require('../models');
const { Op } = require('sequelize');

// Create a booking
router.post('/', async (req, res) => {
    try {
        const { tutorId, date, time, userId, subject } = req.body; // Added userId and subject

        // Check for double booking
        const existingBooking = await Booking.findOne({
            where: {
                tutorId: tutorId,
                date: date,
                time: time,
                status: { [Op.ne]: 'Cancelled' } // Ignore cancelled bookings
            }
        });

        if (existingBooking) {
            return res.status(409).json({ error: 'This time slot is already booked.' });
        }

        const booking = await Booking.create({
            tutorId,
            userId, // Ensure userId is saved
            date,
            time,
            subject,
            status: 'Upcoming'
        });
        res.status(201).json(booking);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Update booking (e.g., cancel)
router.put('/:id', async (req, res) => {
    try {
        const booking = await Booking.findByPk(req.params.id);
        if (!booking) return res.status(404).json({ error: 'Booking not found' });

        await booking.update(req.body);
        res.json(booking);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

module.exports = router;
