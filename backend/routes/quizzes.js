const express = require('express');
const router = express.Router();
const { Quiz, QuizQuestion, QuizUserResult, Chapter, Textbook, User } = require('../models');
const { authenticate, authorize } = require('../middleware/auth');
const geminiService = require('../services/geminiService');

// ============================================
// PUBLIC QUIZ LISTING
// ============================================

// Get all quizzes (with filters)
router.get('/', async (req, res) => {
    try {
        const { chapterId, difficulty, isActive = true } = req.query;

        const where = { isActive };
        if (chapterId) where.chapterId = chapterId;
        if (difficulty) where.difficulty = difficulty;

        const quizzes = await Quiz.findAll({
            where,
            include: [{
                model: Chapter,
                attributes: ['id', 'title', 'chapterNumber'],
                include: [{
                    model: Textbook,
                    attributes: ['id', 'title', 'subject']
                }]
            }],
            order: [['createdAt', 'DESC']]
        });

        res.json({ quizzes });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get specific quiz details (without answers)
router.get('/:id', async (req, res) => {
    try {
        const quiz = await Quiz.findByPk(req.params.id, {
            include: [
                {
                    model: Chapter,
                    include: [{
                        model: Textbook,
                        attributes: ['id', 'title', 'subject']
                    }]
                },
                {
                    model: QuizQuestion,
                    attributes: ['id', 'questionNumber', 'questionText', 'questionType', 'options', 'points', 'difficulty'],
                    order: [['questionNumber', 'ASC']]
                }
            ]
        });

        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        res.json({ quiz });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// QUIZ TAKING & SUBMISSION (Authenticated)
// ============================================

// Submit quiz answers and get results
router.post('/:id/submit', authenticate, async (req, res) => {
    try {
        const { answers, timeTaken } = req.body;

        if (!answers || !Array.isArray(answers)) {
            return res.status(400).json({ error: 'Answers array is required' });
        }

        const quiz = await Quiz.findByPk(req.params.id, {
            include: [{
                model: QuizQuestion,
                order: [['questionNumber', 'ASC']]
            }]
        });

        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        // Score the quiz
        let totalPoints = 0;
        let earnedPoints = 0;
        const gradedAnswers = [];

        for (const question of quiz.QuizQuestions) {
            totalPoints += question.points;

            const userAnswer = answers.find(a => a.questionId === question.id);
            
            if (!userAnswer) {
                gradedAnswers.push({
                    questionId: question.id,
                    userAnswer: null,
                    correctAnswer: question.correctAnswer,
                    isCorrect: false,
                    points: 0,
                    explanation: question.explanation
                });
                continue;
            }

            // Check if answer is correct
            let isCorrect = false;
            if (question.questionType === 'multiple-choice') {
                isCorrect = userAnswer.answer === question.correctAnswer;
            } else if (question.questionType === 'true-false') {
                isCorrect = userAnswer.answer.toString().toLowerCase() === question.correctAnswer.toLowerCase();
            } else {
                // For short-answer and essay, simple string comparison (can be enhanced)
                isCorrect = userAnswer.answer.trim().toLowerCase() === question.correctAnswer.trim().toLowerCase();
            }

            const pointsEarned = isCorrect ? question.points : 0;
            earnedPoints += pointsEarned;

            gradedAnswers.push({
                questionId: question.id,
                userAnswer: userAnswer.answer,
                correctAnswer: question.correctAnswer,
                isCorrect,
                points: pointsEarned,
                explanation: question.explanation
            });
        }

        const score = totalPoints > 0 ? (earnedPoints / totalPoints) * 100 : 0;
        const passed = score >= quiz.passingScore;

        // Save result
        const result = await QuizUserResult.create({
            quizId: quiz.id,
            userId: req.user.id,
            score: score.toFixed(2),
            totalQuestions: quiz.QuizQuestions.length,
            correctAnswers: gradedAnswers.filter(a => a.isCorrect).length,
            answers: gradedAnswers,
            timeTaken,
            passed,
            completedAt: new Date()
        });

        res.json({
            result: {
                id: result.id,
                score: result.score,
                totalQuestions: result.totalQuestions,
                correctAnswers: result.correctAnswers,
                passed: result.passed,
                timeTaken: result.timeTaken,
                answers: gradedAnswers
            }
        });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Get user's quiz results
router.get('/:id/results', authenticate, async (req, res) => {
    try {
        const results = await QuizUserResult.findAll({
            where: {
                quizId: req.params.id,
                userId: req.user.id
            },
            order: [['completedAt', 'DESC']],
            include: [{
                model: Quiz,
                attributes: ['id', 'title', 'difficulty', 'passingScore']
            }]
        });

        res.json({ results });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get specific result details
router.get('/:quizId/results/:resultId', authenticate, async (req, res) => {
    try {
        const result = await QuizUserResult.findOne({
            where: {
                id: req.params.resultId,
                quizId: req.params.quizId,
                userId: req.user.id
            },
            include: [{
                model: Quiz,
                include: [{
                    model: Chapter,
                    include: [{
                        model: Textbook,
                        attributes: ['title', 'subject']
                    }]
                }]
            }]
        });

        if (!result) {
            return res.status(404).json({ error: 'Result not found' });
        }

        res.json({ result });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get user's all quiz results (personal dashboard)
router.get('/results/me', authenticate, async (req, res) => {
    try {
        const results = await QuizUserResult.findAll({
            where: { userId: req.user.id },
            include: [{
                model: Quiz,
                include: [{
                    model: Chapter,
                    include: [{
                        model: Textbook,
                        attributes: ['title', 'subject']
                    }]
                }]
            }],
            order: [['completedAt', 'DESC']]
        });

        res.json({ results });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// AI-POWERED QUIZ GENERATION
// ============================================

// Generate AI quiz for a chapter (admin only)
router.post('/generate', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { 
            chapterId, 
            difficulty = 'medium', 
            numQuestions = 5, 
            questionTypes = ['multiple-choice', 'true-false'],
            timeLimit = 30,
            passingScore = 70
        } = req.body;

        if (!chapterId) {
            return res.status(400).json({ error: 'Chapter ID is required' });
        }

        // Fetch chapter with textbook information
        const chapter = await Chapter.findByPk(chapterId, {
            include: [{
                model: Textbook,
                attributes: ['id', 'title', 'subject']
            }]
        });

        if (!chapter) {
            return res.status(404).json({ error: 'Chapter not found' });
        }

        // Prepare chapter data for AI
        const chapterData = {
            textbookTitle: chapter.Textbook.title,
            textbookSubject: chapter.Textbook.subject,
            chapterTitle: chapter.title,
            chapterDescription: chapter.description,
            topics: chapter.topics
        };

        // Generate questions using Gemini AI
        console.log('Generating AI quiz questions for chapter:', chapter.title);
        const generatedQuestions = await geminiService.generateQuizQuestions(
            chapterData,
            { difficulty, numQuestions, questionTypes }
        );

        // Create quiz
        const quiz = await Quiz.create({
            chapterId: chapter.id,
            title: `${chapter.title} - AI Generated Quiz`,
            description: `Auto-generated quiz covering ${chapter.title}`,
            difficulty,
            timeLimit,
            passingScore,
            isAutoGenerated: true,
            isActive: true,
            totalQuestions: generatedQuestions.length
        });

        // Create quiz questions
        const questions = [];
        for (let i = 0; i < generatedQuestions.length; i++) {
            const q = generatedQuestions[i];
            const question = await QuizQuestion.create({
                quizId: quiz.id,
                questionNumber: i + 1,
                questionText: q.questionText,
                questionType: q.questionType,
                options: q.options,
                correctAnswer: q.correctAnswer,
                explanation: q.explanation,
                points: q.points,
                difficulty: q.difficulty
            });
            questions.push(question);
        }

        // Fetch complete quiz with questions
        const completeQuiz = await Quiz.findByPk(quiz.id, {
            include: [
                {
                    model: Chapter,
                    include: [{
                        model: Textbook,
                        attributes: ['title', 'subject']
                    }]
                },
                {
                    model: QuizQuestion,
                    order: [['questionNumber', 'ASC']]
                }
            ]
        });

        res.status(201).json({ 
            message: 'AI quiz generated successfully',
            quiz: completeQuiz 
        });
    } catch (error) {
        console.error('Error generating AI quiz:', error);
        res.status(500).json({ error: error.message });
    }
});

// Test Gemini API connection (admin only)
router.get('/test-ai', authenticate, authorize('admin'), async (req, res) => {
    try {
        const result = await geminiService.testConnection();
        res.json(result);
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// Regenerate questions for existing quiz (admin only)
router.post('/:id/regenerate', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { 
            numQuestions = 5, 
            questionTypes = ['multiple-choice', 'true-false']
        } = req.body;

        const quiz = await Quiz.findByPk(req.params.id, {
            include: [{
                model: Chapter,
                include: [{
                    model: Textbook,
                    attributes: ['title', 'subject']
                }]
            }]
        });

        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        // Prepare chapter data
        const chapterData = {
            textbookTitle: quiz.Chapter.Textbook.title,
            textbookSubject: quiz.Chapter.Textbook.subject,
            chapterTitle: quiz.Chapter.title,
            chapterDescription: quiz.Chapter.description,
            topics: quiz.Chapter.topics
        };

        // Generate new questions
        const generatedQuestions = await geminiService.generateQuizQuestions(
            chapterData,
            { difficulty: quiz.difficulty, numQuestions, questionTypes }
        );

        // Delete old questions
        await QuizQuestion.destroy({ where: { quizId: quiz.id } });

        // Create new questions
        for (let i = 0; i < generatedQuestions.length; i++) {
            const q = generatedQuestions[i];
            await QuizQuestion.create({
                quizId: quiz.id,
                questionNumber: i + 1,
                questionText: q.questionText,
                questionType: q.questionType,
                options: q.options,
                correctAnswer: q.correctAnswer,
                explanation: q.explanation,
                points: q.points,
                difficulty: q.difficulty
            });
        }

        // Update quiz
        await quiz.update({ 
            totalQuestions: generatedQuestions.length,
            isAutoGenerated: true 
        });

        // Fetch updated quiz
        const updatedQuiz = await Quiz.findByPk(quiz.id, {
            include: [{
                model: QuizQuestion,
                order: [['questionNumber', 'ASC']]
            }]
        });

        res.json({ 
            message: 'Questions regenerated successfully',
            quiz: updatedQuiz 
        });
    } catch (error) {
        console.error('Error regenerating questions:', error);
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// ADMIN ROUTES - Quiz Management
// ============================================

// Create quiz (admin only)
router.post('/', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { chapterId, title, description, difficulty, timeLimit, passingScore, isAutoGenerated } = req.body;

        if (!chapterId || !title) {
            return res.status(400).json({ error: 'Chapter ID and title are required' });
        }

        const chapter = await Chapter.findByPk(chapterId);
        if (!chapter) {
            return res.status(404).json({ error: 'Chapter not found' });
        }

        const quiz = await Quiz.create({
            chapterId,
            title,
            description,
            difficulty,
            timeLimit,
            passingScore: passingScore || 70,
            isAutoGenerated: isAutoGenerated || false,
            isActive: true
        });

        res.status(201).json({ quiz });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Update quiz (admin only)
router.put('/:id', authenticate, authorize('admin'), async (req, res) => {
    try {
        const quiz = await Quiz.findByPk(req.params.id);

        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        await quiz.update(req.body);
        res.json({ quiz });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Delete quiz (admin only)
router.delete('/:id', authenticate, authorize('admin'), async (req, res) => {
    try {
        const quiz = await Quiz.findByPk(req.params.id);

        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        await quiz.destroy();
        res.json({ message: 'Quiz deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// ============================================
// ADMIN ROUTES - Question Management
// ============================================

// Add question to quiz (admin only)
router.post('/:quizId/questions', authenticate, authorize('admin'), async (req, res) => {
    try {
        const { questionNumber, questionText, questionType, options, correctAnswer, explanation, points, difficulty } = req.body;

        if (!questionText || !questionType || !correctAnswer) {
            return res.status(400).json({ error: 'Question text, type, and correct answer are required' });
        }

        const quiz = await Quiz.findByPk(req.params.quizId);
        if (!quiz) {
            return res.status(404).json({ error: 'Quiz not found' });
        }

        const question = await QuizQuestion.create({
            quizId: req.params.quizId,
            questionNumber: questionNumber || 1,
            questionText,
            questionType,
            options,
            correctAnswer,
            explanation,
            points: points || 1,
            difficulty
        });

        // Update quiz total questions count
        const totalQuestions = await QuizQuestion.count({ where: { quizId: req.params.quizId } });
        await quiz.update({ totalQuestions });

        res.status(201).json({ question });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Update question (admin only)
router.put('/:quizId/questions/:questionId', authenticate, authorize('admin'), async (req, res) => {
    try {
        const question = await QuizQuestion.findOne({
            where: {
                id: req.params.questionId,
                quizId: req.params.quizId
            }
        });

        if (!question) {
            return res.status(404).json({ error: 'Question not found' });
        }

        await question.update(req.body);
        res.json({ question });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Delete question (admin only)
router.delete('/:quizId/questions/:questionId', authenticate, authorize('admin'), async (req, res) => {
    try {
        const question = await QuizQuestion.findOne({
            where: {
                id: req.params.questionId,
                quizId: req.params.quizId
            }
        });

        if (!question) {
            return res.status(404).json({ error: 'Question not found' });
        }

        await question.destroy();

        // Update quiz total questions count
        const quiz = await Quiz.findByPk(req.params.quizId);
        const totalQuestions = await QuizQuestion.count({ where: { quizId: req.params.quizId } });
        await quiz.update({ totalQuestions });

        res.json({ message: 'Question deleted successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;

