module.exports = (sequelize, DataTypes) => {
    const Review = sequelize.define('Review', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        tutorId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'tutor_id',
            references: {
                model: 'tutors',
                key: 'id'
            }
        },
        bookingId: {
            type: DataTypes.INTEGER,
            allowNull: true,
            field: 'booking_id',
            references: {
                model: 'bookings',
                key: 'id'
            }
        },
        studentName: {
            type: DataTypes.STRING,
            field: 'student_name'
        },
        avatarId: {
            type: DataTypes.INTEGER,
            field: 'avatar_id'
        },
        content: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        rating: {
            type: DataTypes.INTEGER,
            allowNull: false,
            validate: {
                min: 1,
                max: 5
            }
        },
        likes: {
            type: DataTypes.INTEGER,
            defaultValue: 0
        }
    }, {
        tableName: 'reviews',
        timestamps: true,
        underscored: true
    });
    return Review;
};
