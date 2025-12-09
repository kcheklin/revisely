const Sequelize = require('sequelize');
const path = require('path');

const sequelize = new Sequelize({
    dialect: 'sqlite',
    storage: path.join(__dirname, '../database.sqlite'),
    logging: false
});

const db = {};
db.Sequelize = Sequelize;
db.sequelize = sequelize;

db.Tutor = require('./Tutor')(sequelize, Sequelize);
db.Booking = require('./Booking')(sequelize, Sequelize);
db.Review = require('./Review')(sequelize, Sequelize);
db.Message = require('./Message')(sequelize, Sequelize);

// Associations
// Tutor has many Reviews
db.Tutor.hasMany(db.Review, { foreignKey: 'tutorId' });
db.Review.belongsTo(db.Tutor, { foreignKey: 'tutorId' });

// Tutor has many Bookings
db.Tutor.hasMany(db.Booking, { foreignKey: 'tutorId' });
db.Booking.belongsTo(db.Tutor, { foreignKey: 'tutorId' });

module.exports = db;
