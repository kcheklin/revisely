module.exports = (sequelize, DataTypes) => {
    const Booking = sequelize.define('Booking', {
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
        userId: {
            type: DataTypes.INTEGER,
            allowNull: true,
            field: 'user_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        studentName: {
            type: DataTypes.STRING,
            field: 'student_name'
        },
        subject: {
            type: DataTypes.STRING,
            allowNull: false
        },
        date: {
            type: DataTypes.DATEONLY, // PostgreSQL DATE type
            allowNull: false
        },
        time: {
            type: DataTypes.TIME, // PostgreSQL TIME type
            allowNull: false
        },
        status: {
            type: DataTypes.STRING,
            defaultValue: 'Upcoming',
            validate: {
                isIn: [['Upcoming', 'Completed', 'Cancelled']]
            }
        }
    }, {
        tableName: 'bookings',
        timestamps: true,
        underscored: true
    });
    return Booking;
};
