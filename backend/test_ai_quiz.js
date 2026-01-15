/**
 * Test script for AI Quiz Generation
 * 
 * This script helps you test the Gemini AI integration without needing to make HTTP requests manually.
 * 
 * Usage:
 *   node test_ai_quiz.js
 */

require('dotenv').config({ path: '.env.local' });
const { Chapter, Textbook, Quiz, QuizQuestion, sequelize } = require('./models');
const geminiService = require('./services/geminiService');

const colors = {
    reset: '\x1b[0m',
    green: '\x1b[32m',
    red: '\x1b[31m',
    yellow: '\x1b[33m',
    blue: '\x1b[36m',
    gray: '\x1b[90m'
};

const log = {
    success: (msg) => console.log(`${colors.green}✓${colors.reset} ${msg}`),
    error: (msg) => console.log(`${colors.red}✗${colors.reset} ${msg}`),
    info: (msg) => console.log(`${colors.blue}ℹ${colors.reset} ${msg}`),
    warning: (msg) => console.log(`${colors.yellow}⚠${colors.reset} ${msg}`),
    section: (msg) => console.log(`\n${colors.blue}═══${colors.reset} ${msg} ${colors.blue}═══${colors.reset}\n`)
};

async function testAIQuizGeneration() {
    console.log('\n' + '='.repeat(60));
    console.log('  AI QUIZ GENERATION TEST');
    console.log('='.repeat(60) + '\n');

    try {
        // Test 1: Check environment variables
        log.section('Test 1: Environment Configuration');
        
        if (!process.env.GEMINI_API_KEY) {
            log.error('GEMINI_API_KEY not found in environment variables');
            log.info('Please add GEMINI_API_KEY to your .env.local file');
            log.info('Get your API key from: https://makersuite.google.com/app/apikey');
            return;
        }
        
        const keyPreview = process.env.GEMINI_API_KEY.substring(0, 10) + '...';
        log.success(`API key found: ${keyPreview}`);

        // Test 2: Database connection
        log.section('Test 2: Database Connection');
        
        await sequelize.authenticate();
        log.success('Database connection established');

        // Test 3: Gemini API connection
        log.section('Test 3: Gemini API Connection');
        
        const connectionTest = await geminiService.testConnection();
        if (connectionTest.success) {
            log.success('Gemini API connection successful');
            log.info(`Response: ${connectionTest.message}`);
        } else {
            log.error('Gemini API connection failed');
            log.error(`Error: ${connectionTest.error}`);
            return;
        }

        // Test 4: Fetch a chapter
        log.section('Test 4: Fetching Sample Chapter');
        
        const chapter = await Chapter.findOne({
            include: [{
                model: Textbook,
                attributes: ['title', 'subject']
            }]
        });

        if (!chapter) {
            log.warning('No chapters found in database');
            log.info('Run "npm run seed" to create sample data first');
            return;
        }

        log.success(`Found chapter: ${chapter.title}`);
        log.info(`Textbook: ${chapter.Textbook.title}`);
        log.info(`Subject: ${chapter.Textbook.subject}`);
        log.info(`Topics: ${chapter.topics ? chapter.topics.join(', ') : 'None'}`);

        // Test 5: Generate quiz questions
        log.section('Test 5: Generating AI Quiz Questions');
        
        const chapterData = {
            textbookTitle: chapter.Textbook.title,
            textbookSubject: chapter.Textbook.subject,
            chapterTitle: chapter.title,
            chapterDescription: chapter.description,
            topics: chapter.topics
        };

        const quizOptions = {
            difficulty: 'medium',
            numQuestions: 3, // Using 3 for faster testing
            questionTypes: ['multiple-choice', 'true-false']
        };

        log.info('Generating questions (this may take 5-10 seconds)...');
        
        const startTime = Date.now();
        const questions = await geminiService.generateQuizQuestions(chapterData, quizOptions);
        const endTime = Date.now();
        const duration = ((endTime - startTime) / 1000).toFixed(2);

        log.success(`Generated ${questions.length} questions in ${duration}s`);

        // Test 6: Display generated questions
        log.section('Test 6: Generated Questions Preview');
        
        questions.forEach((q, index) => {
            console.log(`\n${colors.yellow}Question ${index + 1}:${colors.reset}`);
            console.log(`  ${colors.gray}Type:${colors.reset} ${q.questionType}`);
            console.log(`  ${colors.gray}Difficulty:${colors.reset} ${q.difficulty}`);
            console.log(`  ${colors.gray}Points:${colors.reset} ${q.points}`);
            console.log(`\n  ${q.questionText}`);
            
            if (q.options && q.options.length > 0) {
                console.log(`\n  ${colors.gray}Options:${colors.reset}`);
                q.options.forEach((opt, i) => {
                    const isCorrect = q.correctAnswer === i.toString() || 
                                    (q.questionType === 'true-false' && 
                                     opt.toLowerCase() === q.correctAnswer.toLowerCase());
                    const marker = isCorrect ? `${colors.green}✓${colors.reset}` : ' ';
                    console.log(`    ${marker} ${i}. ${opt}`);
                });
            }
            
            console.log(`\n  ${colors.gray}Correct Answer:${colors.reset} ${q.correctAnswer}`);
            console.log(`  ${colors.gray}Explanation:${colors.reset} ${q.explanation}`);
        });

        // Test 7: Save to database (optional)
        log.section('Test 7: Saving Quiz to Database');
        
        const quiz = await Quiz.create({
            chapterId: chapter.id,
            title: `${chapter.title} - AI Test Quiz`,
            description: `Test quiz generated on ${new Date().toLocaleString()}`,
            difficulty: quizOptions.difficulty,
            timeLimit: 30,
            passingScore: 70,
            isAutoGenerated: true,
            isActive: true,
            totalQuestions: questions.length
        });

        log.success(`Quiz created with ID: ${quiz.id}`);

        for (let i = 0; i < questions.length; i++) {
            const q = questions[i];
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

        log.success(`Saved ${questions.length} questions to database`);

        // Summary
        log.section('Test Summary');
        log.success('All tests passed successfully!');
        log.info(`Quiz ID: ${quiz.id}`);
        log.info(`Chapter: ${chapter.title}`);
        log.info(`Questions: ${questions.length}`);
        log.info(`Generation Time: ${duration}s`);
        log.info('');
        log.info('You can now view this quiz at:');
        log.info(`  GET /api/quizzes/${quiz.id}`);
        
    } catch (error) {
        log.error('Test failed with error:');
        console.error(error);
        
        if (error.message.includes('API key')) {
            log.info('');
            log.info('💡 Make sure your GEMINI_API_KEY is valid');
            log.info('   Get a key from: https://makersuite.google.com/app/apikey');
        }
    } finally {
        await sequelize.close();
        log.info('\nDatabase connection closed');
    }
}

// Run the test
testAIQuizGeneration()
    .then(() => {
        console.log('\n' + '='.repeat(60));
        console.log('  Test completed');
        console.log('='.repeat(60) + '\n');
        process.exit(0);
    })
    .catch(err => {
        console.error('\n❌ Fatal error:', err);
        process.exit(1);
    });

