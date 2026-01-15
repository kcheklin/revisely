module.exports = (sequelize, DataTypes) => {
    const Textbook = sequelize.define('Textbook', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        title: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        subject: {
            type: DataTypes.STRING(100),
            allowNull: false
        },
        grade: {
            type: DataTypes.STRING(50),
            allowNull: true,
            comment: 'e.g., Grade 10, Form 4, Year 11'
        },
        level: {
            type: DataTypes.STRING(50),
            allowNull: true,
            comment: 'e.g., Beginner, Intermediate, Advanced'
        },
        coverImage: {
            type: DataTypes.STRING,
            allowNull: true,
            field: 'cover_image'
        },
        description: {
            type: DataTypes.TEXT,
            allowNull: true
        },
        publisher: {
            type: DataTypes.STRING(200),
            allowNull: true
        },
        isbn: {
            type: DataTypes.STRING(20),
            allowNull: true,
            unique: true
        },
        yearPublished: {
            type: DataTypes.INTEGER,
            allowNull: true,
            field: 'year_published'
        },
        language: {
            type: DataTypes.STRING(50),
            defaultValue: 'English'
        }
    }, {
        tableName: 'textbooks',
        timestamps: true,
        underscored: true
    });
    return Textbook;
};

