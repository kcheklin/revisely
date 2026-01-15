module.exports = (sequelize, DataTypes) => {
    const Tutor = sequelize.define('Tutor', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false
        },
        faculty: DataTypes.STRING,
        subjects: {
            type: DataTypes.JSONB, // Use JSONB for PostgreSQL (better performance)
            defaultValue: []
        },
        avatarId: {
            type: DataTypes.INTEGER,
            field: 'avatar_id'
        },
        role: DataTypes.STRING,
        about: DataTypes.TEXT,
        noStudents: {
            type: DataTypes.INTEGER,
            defaultValue: 0,
            field: 'no_students'
        },
        noSessions: {
            type: DataTypes.INTEGER,
            defaultValue: 0,
            field: 'no_sessions'
        },
        yearsExperience: {
            type: DataTypes.INTEGER,
            defaultValue: 0,
            field: 'years_experience'
        },
        starRatings: {
            type: DataTypes.JSONB,
            defaultValue: [0, 0, 0, 0, 0],
            field: 'star_ratings'
        },
        subjectMinLevel: {
            type: DataTypes.JSONB,
            defaultValue: [],
            field: 'subject_min_level'
        },
        subjectMaxLevel: {
            type: DataTypes.JSONB,
            defaultValue: [],
            field: 'subject_max_level'
        },
        education: {
            type: DataTypes.JSONB,
            defaultValue: []
        },
        institutions: {
            type: DataTypes.JSONB,
            defaultValue: []
        },
        graduationYears: {
            type: DataTypes.JSONB,
            defaultValue: [],
            field: 'graduation_years'
        },
        timeSlots: {
            type: DataTypes.JSONB,
            defaultValue: [],
            field: 'time_slots'
        },
        timeSlotsAvailability: {
            type: DataTypes.JSONB,
            defaultValue: [],
            field: 'time_slots_availability'
        }
    }, {
        tableName: 'tutors',
        timestamps: true,
        underscored: true
    });
    return Tutor;
};
