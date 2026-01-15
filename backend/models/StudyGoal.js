module.exports = (sequelize, DataTypes) => {
    const StudyGoal = sequelize.define('StudyGoal', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
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
        subject: {
            type: DataTypes.STRING(100),
            allowNull: false
        },
        targetHours: {
            type: DataTypes.DECIMAL(5, 2),
            allowNull: false,
            field: 'target_hours',
            comment: 'Target study hours for this goal'
        },
        currentHours: {
            type: DataTypes.DECIMAL(5, 2),
            defaultValue: 0,
            field: 'current_hours',
            comment: 'Current progress in hours'
        },
        deadline: {
            type: DataTypes.DATEONLY,
            allowNull: true,
            comment: 'Goal deadline date'
        },
        completed: {
            type: DataTypes.BOOLEAN,
            defaultValue: false
        },
        priority: {
            type: DataTypes.STRING(20),
            defaultValue: 'medium',
            validate: {
                isIn: [['low', 'medium', 'high']]
            }
        },
        description: {
            type: DataTypes.TEXT,
            allowNull: true
        }
    }, {
        tableName: 'study_goals',
        timestamps: true,
        underscored: true
    });
    return StudyGoal;
};

