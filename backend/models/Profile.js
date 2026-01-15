module.exports = (sequelize, DataTypes) => {
    const Profile = sequelize.define('Profile', {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        userId: {
            type: DataTypes.INTEGER,
            allowNull: false,
            unique: true,
            field: 'user_id',
            references: {
                model: 'users',
                key: 'id'
            }
        },
        bio: {
            type: DataTypes.TEXT,
            allowNull: true,
            defaultValue: ''
        },
        profilePicture: {
            type: DataTypes.STRING,
            allowNull: true,
            field: 'profile_picture',
            comment: 'URL or path to profile picture'
        },
        avatarId: {
            type: DataTypes.INTEGER,
            allowNull: true,
            field: 'avatar_id',
            comment: 'Avatar ID for default avatars'
        },
        theme: {
            type: DataTypes.STRING(50),
            defaultValue: 'light',
            validate: {
                isIn: [['light', 'dark', 'auto']]
            }
        },
        notifications: {
            type: DataTypes.BOOLEAN,
            defaultValue: true
        }
    }, {
        tableName: 'profiles',
        timestamps: true,
        underscored: true
    });
    return Profile;
};

