module.exports = (sequelize, DataTypes) => {
    const Booking = sequelize.define('Booking', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        tutorId: DataTypes.INTEGER,
        userId: DataTypes.INTEGER, // Link to the user who booked
        studentName: DataTypes.STRING, // Simplified for now
        subject: DataTypes.STRING,
        date: DataTypes.STRING, // Format: YYYY-MM-DD
        time: DataTypes.STRING, // Format: HH:MM
        status: {
            type: DataTypes.STRING,
            defaultValue: 'Upcoming' // Upcoming, Completed, Cancelled
        }
    });
    return Booking;
};
