module.exports = (sequelize, DataTypes) => {
    const Message = sequelize.define('Message', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        senderId: DataTypes.INTEGER,
        receiverId: DataTypes.INTEGER,
        content: DataTypes.TEXT,
        timestamp: DataTypes.DATE
    });
    return Message;
};
