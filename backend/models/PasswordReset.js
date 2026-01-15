module.exports = (sequelize, DataTypes) => {
    const PasswordReset = sequelize.define('PasswordReset', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        userId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'user_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        otp: {
            type: DataTypes.STRING(6),
            allowNull: false
        },
        expiresAt: {
            type: DataTypes.DATE,
            allowNull: false,
            field: 'expires_at'
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
        tableName: 'password_resets',
        timestamps: true,
        underscored: true
    });
    return PasswordReset;
};

