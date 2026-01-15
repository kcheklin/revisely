module.exports = (sequelize, DataTypes) => {
    const Chapter = sequelize.define('Chapter', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        textbookId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'textbook_id',
            references: {
                model: 'textbooks',
                key: 'id'
            }
        },
        chapterNumber: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'chapter_number'
        },
        title: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        description: {
            type: DataTypes.TEXT,
            allowNull: true
        },
        topics: {
            type: DataTypes.JSONB,
            defaultValue: [],
            comment: 'Array of topics covered in this chapter'
        },
        duration: {
            type: DataTypes.INTEGER,
            allowNull: true,
            comment: 'Estimated study duration in minutes'
        }
    }, {
        tableName: 'chapters',
        timestamps: true,
        underscored: true,
        indexes: [
            {
                unique: true,
                fields: ['textbook_id', 'chapter_number']
            }
        ]
    });
    return Chapter;
};

