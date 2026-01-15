module.exports = (sequelize, DataTypes) => {
    const Message = sequelize.define('Message', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        senderId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'sender_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        receiverId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            field: 'receiver_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        content: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        timestamp: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        }
    }, {
        tableName: 'messages',
        timestamps: true,
        underscored: true
    });
    return Message;
};
