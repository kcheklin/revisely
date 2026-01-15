module.exports = (sequelize, DataTypes) => {
    const StudySession = sequelize.define('StudySession', {
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
            type: DataTypes.STRING(255),
            allowNull: false
        },
        durationInMinutes: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'duration_in_minutes'
        },
        startTime: {
            type: DataTypes.DATE,
            allowNull: false,
            field: 'start_time'
        },
        endTime: {
            type: DataTypes.DATE,
            allowNull: true,
            field: 'end_time'
        },
        createdAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW,
            field: 'created_at'
        },
        updatedAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW,
            field: 'updated_at'
        }
    }, {
        tableName: 'study_sessions',
        timestamps: true,
        underscored: true
    });
    return StudySession;
};

