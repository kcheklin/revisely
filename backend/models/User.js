module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define('User', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        email: {
            type: DataTypes.STRING(255),
            unique: true,
            allowNull: false,
            validate: {
                isEmail: true
            }
        },
        password: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        name: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        role: {
            type: DataTypes.STRING(50),
            defaultValue: 'student',
            validate: {
                isIn: [['student', 'admin', 'tutor']]
            }
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
        tableName: 'users',
        timestamps: true,
        underscored: true
    });
    return User;
};

