require('dotenv').config({ path: '.env.local' });
const Sequelize = require('sequelize');

// Connect to PostgreSQL using DATABASE_URL from .env
const sequelize = new Sequelize(process.env.DATABASE_URL, {
    dialect: 'postgres',
    logging: false,
    dialectOptions: {
        ssl: process.env.NODE_ENV === 'production' ? {
            require: true,
            rejectUnauthorized: false
        } : false
    }
});

const db = {};
db.Sequelize = Sequelize;
db.sequelize = sequelize;

// Import models - Authentication & Core
db.User = require('./User')(sequelize, Sequelize);
db.RefreshToken = require('./RefreshToken')(sequelize, Sequelize);
db.PasswordReset = require('./PasswordReset')(sequelize, Sequelize);
db.StudySession = require('./StudySession')(sequelize, Sequelize);

// Tutor System
db.Tutor = require('./Tutor')(sequelize, Sequelize);
db.Booking = require('./Booking')(sequelize, Sequelize);
db.Review = require('./Review')(sequelize, Sequelize);
db.Message = require('./Message')(sequelize, Sequelize);

// Profile & Analytics (Module 5)
db.Profile = require('./Profile')(sequelize, Sequelize);
db.StudyGoal = require('./StudyGoal')(sequelize, Sequelize);
db.MotivationalQuote = require('./MotivationalQuote')(sequelize, Sequelize);

// Quizzes (Module 6)
db.Textbook = require('./Textbook')(sequelize, Sequelize);
db.Chapter = require('./Chapter')(sequelize, Sequelize);
db.Quiz = require('./Quiz')(sequelize, Sequelize);
db.QuizQuestion = require('./QuizQuestion')(sequelize, Sequelize);
db.QuizUserResult = require('./QuizUserResult')(sequelize, Sequelize);

// ============================================
// ASSOCIATIONS
// ============================================

// User relationships
db.User.hasMany(db.RefreshToken, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.RefreshToken.belongsTo(db.User, { foreignKey: 'userId' });

db.User.hasMany(db.PasswordReset, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.PasswordReset.belongsTo(db.User, { foreignKey: 'userId' });

db.User.hasMany(db.StudySession, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.StudySession.belongsTo(db.User, { foreignKey: 'userId' });

db.User.hasMany(db.Booking, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.Booking.belongsTo(db.User, { foreignKey: 'userId' });

db.User.hasMany(db.Message, { foreignKey: 'senderId', as: 'sentMessages' });
db.User.hasMany(db.Message, { foreignKey: 'receiverId', as: 'receivedMessages' });
db.Message.belongsTo(db.User, { foreignKey: 'senderId', as: 'sender' });
db.Message.belongsTo(db.User, { foreignKey: 'receiverId', as: 'receiver' });

// Tutor relationships
db.Tutor.hasMany(db.Review, { foreignKey: 'tutorId', onDelete: 'CASCADE' });
db.Review.belongsTo(db.Tutor, { foreignKey: 'tutorId' });

db.Tutor.hasMany(db.Booking, { foreignKey: 'tutorId', onDelete: 'CASCADE' });
db.Booking.belongsTo(db.Tutor, { foreignKey: 'tutorId' });

// Booking relationships
db.Booking.hasMany(db.Review, { foreignKey: 'bookingId', onDelete: 'SET NULL' });
db.Review.belongsTo(db.Booking, { foreignKey: 'bookingId' });

// ============================================
// MODULE 5: Profile & Study Analytics
// ============================================

// Profile relationships (One-to-One with User)
db.User.hasOne(db.Profile, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.Profile.belongsTo(db.User, { foreignKey: 'userId' });

// Study Goals relationships
db.User.hasMany(db.StudyGoal, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.StudyGoal.belongsTo(db.User, { foreignKey: 'userId' });

// Motivational Quotes (standalone, no relationships needed)

// ============================================
// MODULE 6: Textbook-Based Quizzes
// ============================================

// Textbook -> Chapters (One-to-Many)
db.Textbook.hasMany(db.Chapter, { foreignKey: 'textbookId', onDelete: 'CASCADE' });
db.Chapter.belongsTo(db.Textbook, { foreignKey: 'textbookId' });

// Chapter -> Quizzes (One-to-Many)
db.Chapter.hasMany(db.Quiz, { foreignKey: 'chapterId', onDelete: 'CASCADE' });
db.Quiz.belongsTo(db.Chapter, { foreignKey: 'chapterId' });

// Quiz -> Questions (One-to-Many)
db.Quiz.hasMany(db.QuizQuestion, { foreignKey: 'quizId', onDelete: 'CASCADE' });
db.QuizQuestion.belongsTo(db.Quiz, { foreignKey: 'quizId' });

// Quiz -> User Results (One-to-Many)
db.Quiz.hasMany(db.QuizUserResult, { foreignKey: 'quizId', onDelete: 'CASCADE' });
db.QuizUserResult.belongsTo(db.Quiz, { foreignKey: 'quizId' });

// User -> Quiz Results (One-to-Many)
db.User.hasMany(db.QuizUserResult, { foreignKey: 'userId', onDelete: 'CASCADE' });
db.QuizUserResult.belongsTo(db.User, { foreignKey: 'userId' });

module.exports = db;
