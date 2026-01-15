const express = require('express');
const router = express.Router();
const { Textbook, Chapter, Quiz, QuizQuestion } = require('../models');
const { authenticate, authorize } = require('../middleware/auth');

// ============================================
// PUBLIC TEXTBOOK ROUTES
// ============================================

// Get all textbooks
router.get('/', async (req, res) => {
    try {
        const { subject, grade, level, search } = req.query;

        const where = {};
        if (subject) where.subject = subject;
        if (grade) where.grade = grade;
        if (level) where.level = level;
        if (search) {
            const { Op } = require('sequelize');
            where[Op.or] = [
                { title: { [Op.iLike]: `%${search}%` } },
                { description: { [Op.iLike]: `%${search}%` } }
            ];
        }

        const textbooks = await Textbook.findAll({
            where,
            order: [['title', 'ASC']],
            include: [{
                model: Chapter,
                attributes: ['id', 'chapterNumber', 'title']
            }]
        });

        res.json({ textbooks });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get single textbook with chapters
router.get('/:id', async (req, res) => {
    try {
        const textbook = await Textbook.findByPk(req.params.id, {
            include: [{
                model: Chapter,
                include: [{
                    model: Quiz,
                    attributes: ['id', 'title', 'difficulty', 'totalQuestions', 'isActive']
                }]
            }]
        });

        if (!textbook) {
            return res.status(404).json({ error: 'Textbook not found' });
        }

        res.json({ textbook });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get textbook subjects (unique list)
router.get('/meta/subjects', async (req, res) => {
    try {
        const { sequelize } = require('../models');
        const subjects = await Textbook.findAll({
            attributes: [[sequelize.fn('DISTINCT', sequelize.col('subject')), 'subject']],
            raw: true
        });

        res.json({ subjects: subjects.map(s => s.subject) });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// CHAPTER ROUTES
// ============================================

// Get specific chapter
router.get('/:textbookId/chapters/:chapterId', async (req, res) => {
    try {
        const chapter = await Chapter.findOne({
            where: {
                id: req.params.chapterId,
                textbookId: req.params.textbookId
            },
            include: [
                {
                    model: Textbook,
                    attributes: ['id', 'title', 'subject']
                },
                {
                    model: Quiz,
                    where: { isActive: true },
                    required: false,
                    attributes: ['id', 'title', 'description', 'difficulty', 'totalQuestions', 'timeLimit']
                }
            ]
        });

        if (!chapter) {
            return res.status(404).json({ error: 'Chapter not found' });
        }

        res.json({ chapter });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// ADMIN ROUTES (Create/Update/Delete)
// ============================================

// Create textbook (admin only)
router.post('/', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { title, subject, grade, level, coverImage, description, publisher, isbn, yearPublished, language } = req.body;

        if (!title || !subject) {
            return res.status(400).json({ error: 'Title and subject are required' });
        }

        const textbook = await Textbook.create({
            title,
            subject,
            grade,
            level,
            coverImage,
            description,
            publisher,
            isbn,
            yearPublished,
            language
        });

        res.status(201).json({ textbook });
    } catch (error) {
        if (error.name === 'SequelizeUniqueConstraintError') {
            return res.status(400).json({ error: 'ISBN already exists' });
        }
        res.status(400).json({ error: error.message });
    }
});

// Update textbook (admin only)
router.put('/:id', authenticate, authorize('admin'), async (req, res) => {
    try {
        const textbook = await Textbook.findByPk(req.params.id);

        if (!textbook) {
            return res.status(404).json({ error: 'Textbook not found' });
        }

        await textbook.update(req.body);
        res.json({ textbook });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Delete textbook (admin only)
router.delete('/:id', authenticate, authorize('admin'), async (req, res) => {
    try {
        const textbook = await Textbook.findByPk(req.params.id);

        if (!textbook) {
            return res.status(404).json({ error: 'Textbook not found' });
        }

        await textbook.destroy();
        res.json({ message: 'Textbook deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Create chapter (admin only)
router.post('/:textbookId/chapters', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { chapterNumber, title, description, topics, duration } = req.body;

        if (!chapterNumber || !title) {
            return res.status(400).json({ error: 'Chapter number and title are required' });
        }

        const textbook = await Textbook.findByPk(req.params.textbookId);
        if (!textbook) {
            return res.status(404).json({ error: 'Textbook not found' });
        }

        const chapter = await Chapter.create({
            textbookId: req.params.textbookId,
            chapterNumber,
            title,
            description,
            topics,
            duration
        });

        res.status(201).json({ chapter });
    } catch (error) {
        if (error.name === 'SequelizeUniqueConstraintError') {
            return res.status(400).json({ error: 'Chapter number already exists for this textbook' });
        }
        res.status(400).json({ error: error.message });
    }
});

// Update chapter (admin only)
router.put('/:textbookId/chapters/:chapterId', authenticate, authorize('admin'), async (req, res) => {
    try {
        const chapter = await Chapter.findOne({
            where: {
                id: req.params.chapterId,
                textbookId: req.params.textbookId
            }
        });

        if (!chapter) {
            return res.status(404).json({ error: 'Chapter not found' });
        }

        await chapter.update(req.body);
        res.json({ chapter });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Delete chapter (admin only)
router.delete('/:textbookId/chapters/:chapterId', authenticate, authorize('admin'), async (req, res) => {
    try {
        const chapter = await Chapter.findOne({
            where: {
                id: req.params.chapterId,
                textbookId: req.params.textbookId
            }
        });

        if (!chapter) {
            return res.status(404).json({ error: 'Chapter not found' });
        }

        await chapter.destroy();
        res.json({ message: 'Chapter deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;

