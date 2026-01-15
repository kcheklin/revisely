module.exports = (sequelize, DataTypes) => {
    const MotivationalQuote = sequelize.define('MotivationalQuote', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        quote: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        author: {
            type: DataTypes.STRING(100),
            allowNull: true,
            defaultValue: 'Unknown'
        },
        category: {
            type: DataTypes.STRING(50),
            defaultValue: 'motivation',
            validate: {
                isIn: [['motivation', 'success', 'learning', 'perseverance', 'focus']]
            }
        },
        language: {
            type: DataTypes.STRING(10),
            defaultValue: 'en',
            comment: 'Language code (en, es, fr, etc.)'
        },
        likes: {
            type: DataTypes.INTEGER,
            defaultValue: 0
        }
    }, {
        tableName: 'motivational_quotes',
        timestamps: true,
        underscored: true
    });
    return MotivationalQuote;
};

