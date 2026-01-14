const { sequelize, Tutor, Review, Booking } = require('./models');

const seedData = async () => {
    await sequelize.sync({ force: true });

    const tutor1 = await Tutor.create({
        name: "Dr. Emily Chen",
        faculty: "Engineering",
        subjects: ["Mathematics", "Physics", "Additional Mathematics"],
        avatarId: 2131165300, // R.drawable.avatar_tutor_emily (placeholder ID)
        role: "Mathematics & Physics Expert",
        about: "With over 10 years of teaching experience...",
        noStudents: 127,
        noSessions: 450,
        yearsExperience: 10,
        starRatings: [108, 15, 3, 1, 0],
        subjectMinLevel: ["Beginner", "Intermediate", "Intermediate"],
        subjectMaxLevel: ["Advanced", "Advanced", "Intermediate"],
        education: ["Ph.D. in Mathematics", "B.Sc. in Applied Mathematics"],
        institutions: ["Stanford University", "UC Berkeley"],
        graduationYears: [2015, 2009],
        timeSlots: ["2025-12-01 9:00", "2025-12-01 10:00", "2025-12-01 11:00"],
        timeSlotsAvailability: [true, true, false]
    });

    const booking1 = await Booking.create({
        tutorId: tutor1.id,
        userId: 1, // Dummy User ID
        studentName: "Jessica",
        subject: "Mathematics",
        date: "2025-12-01",
        time: "10:00",
        status: "Completed"
    });

    await Review.create({
        tutorId: tutor1.id,
        bookingId: booking1.id,
        studentName: "Jessica",
        avatarId: 2131165298, // Placeholder
        content: "Excellent tutor! Very patient...",
        rating: 5,
        likes: 12
    });

    const tutor2 = await Tutor.create({
        name: "Prof. Marcus Johnson",
        faculty: "Computer Science",
        subjects: ["Database", "Data Structure"],
        avatarId: 2131165301, // Placeholder
        role: "Computer Science Expert",
        about: "Expert in database systems...",
        noStudents: 90,
        noSessions: 380,
        yearsExperience: 7,
        starRatings: [88, 14, 2, 1, 0],
        subjectMinLevel: ["Intermediate", "Beginner"],
        subjectMaxLevel: ["Advanced", "Advanced"],
        education: ["M.Sc in Computer Science"],
        institutions: ["MIT"],
        graduationYears: [2012],
        timeSlots: ["2025-11-30 10:00"],
        timeSlotsAvailability: [true]
    });

    console.log('Database seeded!');
};

seedData().then(() => {
    process.exit();
});
