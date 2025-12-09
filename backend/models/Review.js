module.exports = (sequelize, DataTypes) => {
    const Review = sequelize.define('Review', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        tutorId: DataTypes.INTEGER,
        bookingId: DataTypes.INTEGER, // Link review to a specific booking
        studentName: DataTypes.STRING,
        avatarId: DataTypes.INTEGER,
        content: DataTypes.TEXT,
        rating: DataTypes.INTEGER,
        likes: DataTypes.INTEGER
    });
    return Review;
};
