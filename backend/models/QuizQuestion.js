module.exports = (sequelize, DataTypes) => {
    const QuizQuestion = sequelize.define('QuizQuestion', {
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
        questionNumber: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'question_number'
        },
        questionText: {
            type: DataTypes.TEXT,
            allowNull: false,
            field: 'question_text'
        },
        questionType: {
            type: DataTypes.STRING(50),
            allowNull: false,
            defaultValue: 'multiple-choice',
            field: 'question_type',
            validate: {
                isIn: [['multiple-choice', 'true-false', 'short-answer', 'essay']]
            }
        },
        options: {
            type: DataTypes.JSONB,
            allowNull: true,
            comment: 'Array of answer options for multiple choice questions'
        },
        correctAnswer: {
            type: DataTypes.TEXT,
            allowNull: false,
            field: 'correct_answer',
            comment: 'Correct answer (index for MC, text for others)'
        },
        explanation: {
            type: DataTypes.TEXT,
            allowNull: true,
            comment: 'Explanation for the correct answer'
        },
        points: {
            type: DataTypes.INTEGER,
            defaultValue: 1,
            comment: 'Points awarded for correct answer'
        },
        difficulty: {
            type: DataTypes.STRING(20),
            defaultValue: 'medium',
            validate: {
                isIn: [['easy', 'medium', 'hard']]
            }
        }
    }, {
        tableName: 'quiz_questions',
        timestamps: true,
        underscored: true,
        indexes: [
            {
                fields: ['quiz_id', 'question_number']
            }
        ]
    });
    return QuizQuestion;
};

