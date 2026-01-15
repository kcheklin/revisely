module.exports = (sequelize, DataTypes) => {
    const QuizUserResult = sequelize.define('QuizUserResult', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        quizId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'quiz_id',
            references: {
                model: 'quizzes',
                key: 'id'
            }
        },
        userId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'user_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        score: {
            type: DataTypes.DECIMAL(5, 2),
            allowNull: false,
            comment: 'Score as percentage (0-100)'
        },
        totalQuestions: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'total_questions'
        },
        correctAnswers: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'correct_answers'
        },
        answers: {
            type: DataTypes.JSONB,
            allowNull: false,
            comment: 'User answers: [{questionId, userAnswer, isCorrect, points}]'
        },
        timeTaken: {
            type: DataTypes.INTEGER,
            allowNull: true,
            field: 'time_taken',
            comment: 'Time taken in seconds'
        },
        passed: {
            type: DataTypes.BOOLEAN,
            allowNull: false,
            comment: 'Whether user passed the quiz'
        },
        completedAt: {
            type: DataTypes.DATE,
            allowNull: false,
            field: 'completed_at',
            defaultValue: DataTypes.NOW
        }
    }, {
        tableName: 'quiz_user_results',
        timestamps: true,
        underscored: true,
        indexes: [
            {
                fields: ['user_id', 'quiz_id']
            }
        ]
    });
    return QuizUserResult;
};

