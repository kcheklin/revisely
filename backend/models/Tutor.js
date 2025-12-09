module.exports = (sequelize, DataTypes) => {
    const Tutor = sequelize.define('Tutor', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        name: DataTypes.STRING,
        faculty: DataTypes.STRING,
        subjects: DataTypes.JSON, // Store as JSON array string
        avatarId: DataTypes.INTEGER,
        role: DataTypes.STRING,
        about: DataTypes.TEXT,
        noStudents: DataTypes.INTEGER,
        noSessions: DataTypes.INTEGER,
        yearsExperience: DataTypes.INTEGER,
        starRatings: DataTypes.JSON, // Store as JSON array string
        subjectMinLevel: DataTypes.JSON,
        subjectMaxLevel: DataTypes.JSON,
        education: DataTypes.JSON,
        institutions: DataTypes.JSON,
        graduationYears: DataTypes.JSON,
        timeSlots: DataTypes.JSON,
        timeSlotsAvailability: DataTypes.JSON
    });
    return Tutor;
};
