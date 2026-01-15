const bcrypt = require('bcrypt');
const { 
    sequelize, User, Tutor, Review, Booking, StudySession,
    Profile, StudyGoal, MotivationalQuote,
    Textbook, Chapter, Quiz, QuizQuestion, QuizUserResult
} = require('./models');

const seedData = async () => {
    console.log('Starting database seed...');
    
    // Sync all models (force: true will drop existing tables)
    await sequelize.sync({ force: true });
    console.log('✅ Database synced');

    // Create test users
    const hashedPassword = await bcrypt.hash('password123', 10);
    
    const student1 = await User.create({
        email: "jessica@example.com",
        password: hashedPassword,
        name: "Jessica Thompson",
        role: "student"
    });
    console.log('✅ Created student user: jessica@example.com');

    const student2 = await User.create({
        email: "john@example.com",
        password: hashedPassword,
        name: "John Smith",
        role: "student"
    });
    console.log('✅ Created student user: john@example.com');

    const admin = await User.create({
        email: "admin@example.com",
        password: hashedPassword,
        name: "Admin User",
        role: "admin"
    });
    console.log('✅ Created admin user: admin@example.com');

    // Create tutors
    const tutor1 = await Tutor.create({
        name: "Dr. Emily Chen",
        faculty: "Engineering",
        subjects: ["Mathematics", "Physics", "Additional Mathematics"],
        avatarId: 2131165300,
        role: "Mathematics & Physics Expert",
        about: "With over 10 years of teaching experience, I specialize in making complex mathematical concepts accessible and engaging. My approach combines rigorous academic standards with practical applications.",
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
    console.log('✅ Created tutor: Dr. Emily Chen');

    const tutor2 = await Tutor.create({
        name: "Prof. Marcus Johnson",
        faculty: "Computer Science",
        subjects: ["Database", "Data Structure"],
        avatarId: 2131165301,
        role: "Computer Science Expert",
        about: "Expert in database systems and data structures with 7 years of industry and academic experience. I help students bridge the gap between theory and practice.",
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
    console.log('✅ Created tutor: Prof. Marcus Johnson');

    // Create bookings
    const booking1 = await Booking.create({
        tutorId: tutor1.id,
        userId: student1.id,
        studentName: student1.name,
        subject: "Mathematics",
        date: "2025-12-01",
        time: "10:00:00",
        status: "Completed"
    });
    console.log('✅ Created booking for Jessica');

    // Create reviews
    await Review.create({
        tutorId: tutor1.id,
        bookingId: booking1.id,
        studentName: student1.name,
        avatarId: 2131165298,
        content: "Excellent tutor! Very patient and explains concepts clearly. Highly recommend Dr. Chen for anyone struggling with mathematics.",
        rating: 5,
        likes: 12
    });
    console.log('✅ Created review');

    // Create study sessions
    await StudySession.create({
        userId: student1.id,
        subject: "Mathematics",
        durationInMinutes: 45,
        startTime: new Date('2025-11-15T14:00:00'),
        endTime: new Date('2025-11-15T14:45:00')
    });

    await StudySession.create({
        userId: student1.id,
        subject: "Physics",
        durationInMinutes: 30,
        startTime: new Date('2025-11-16T10:00:00'),
        endTime: new Date('2025-11-16T10:30:00')
    });

    await StudySession.create({
        userId: student1.id,
        subject: "Mathematics",
        durationInMinutes: 25,
        startTime: new Date('2025-11-17T09:00:00'),
        endTime: new Date('2025-11-17T09:25:00')
    });
    console.log('✅ Created study sessions');

    // ============================================
    // MODULE 5: Profiles & Study Goals
    // ============================================

    await Profile.create({
        userId: student1.id,
        bio: "Passionate about mathematics and physics. Currently preparing for university entrance exams.",
        avatarId: 2131165298,
        theme: "light",
        notifications: true
    });

    await Profile.create({
        userId: student2.id,
        bio: "Computer science enthusiast interested in algorithms and data structures.",
        avatarId: 2131165299,
        theme: "dark",
        notifications: true
    });
    console.log('✅ Created profiles');

    await StudyGoal.create({
        userId: student1.id,
        subject: "Mathematics",
        targetHours: 50,
        currentHours: 15.5,
        deadline: new Date('2026-03-01'),
        priority: "high",
        description: "Master calculus for final exams"
    });

    await StudyGoal.create({
        userId: student1.id,
        subject: "Physics",
        targetHours: 30,
        currentHours: 8,
        deadline: new Date('2026-02-15'),
        priority: "medium",
        description: "Complete mechanics module"
    });

    await StudyGoal.create({
        userId: student2.id,
        subject: "Database",
        targetHours: 40,
        currentHours: 12,
        deadline: new Date('2026-04-01'),
        priority: "high",
        description: "Prepare for database certification"
    });
    console.log('✅ Created study goals');

    // Motivational Quotes
    const quotes = [
        { quote: "The only way to do great work is to love what you do.", author: "Steve Jobs", category: "motivation" },
        { quote: "Success is not final, failure is not fatal: it is the courage to continue that counts.", author: "Winston Churchill", category: "perseverance" },
        { quote: "Education is the most powerful weapon which you can use to change the world.", author: "Nelson Mandela", category: "learning" },
        { quote: "The beautiful thing about learning is that no one can take it away from you.", author: "B.B. King", category: "learning" },
        { quote: "Concentrate all your thoughts upon the work in hand. The sun's rays do not burn until brought to a focus.", author: "Alexander Graham Bell", category: "focus" },
        { quote: "Success is the sum of small efforts repeated day in and day out.", author: "Robert Collier", category: "success" },
        { quote: "The expert in anything was once a beginner.", author: "Helen Hayes", category: "motivation" },
        { quote: "Don't watch the clock; do what it does. Keep going.", author: "Sam Levenson", category: "perseverance" },
        { quote: "Learning never exhausts the mind.", author: "Leonardo da Vinci", category: "learning" },
        { quote: "The secret of getting ahead is getting started.", author: "Mark Twain", category: "motivation" }
    ];

    for (const quote of quotes) {
        await MotivationalQuote.create(quote);
    }
    console.log('✅ Created motivational quotes');

    // ============================================
    // MODULE 6: Textbooks & Quizzes
    // ============================================

    const mathTextbook = await Textbook.create({
        title: "Advanced Mathematics for Form 4",
        subject: "Mathematics",
        grade: "Form 4",
        level: "Intermediate",
        description: "Comprehensive mathematics textbook covering algebra, calculus, and geometry.",
        publisher: "Educational Press",
        isbn: "978-1234567890",
        yearPublished: 2023,
        language: "English"
    });

    const physicsTextbook = await Textbook.create({
        title: "Physics: Mechanics & Motion",
        subject: "Physics",
        grade: "Form 4",
        level: "Intermediate",
        description: "Introduction to classical mechanics, motion, forces, and energy.",
        publisher: "Science Publishers",
        isbn: "978-0987654321",
        yearPublished: 2024,
        language: "English"
    });

    const csTextbook = await Textbook.create({
        title: "Database Systems: Concepts & Design",
        subject: "Computer Science",
        grade: "Year 2",
        level: "Advanced",
        description: "Database design, SQL, normalization, and transaction management.",
        publisher: "Tech Books Inc",
        isbn: "978-1122334455",
        yearPublished: 2024,
        language: "English"
    });
    console.log('✅ Created textbooks');

    // Math Chapters
    const mathChapter1 = await Chapter.create({
        textbookId: mathTextbook.id,
        chapterNumber: 1,
        title: "Quadratic Equations",
        description: "Learn to solve and graph quadratic equations",
        topics: ["Factoring", "Completing the Square", "Quadratic Formula", "Graphing Parabolas"],
        duration: 120
    });

    const mathChapter2 = await Chapter.create({
        textbookId: mathTextbook.id,
        chapterNumber: 2,
        title: "Functions and Graphs",
        description: "Understanding functions, domain, range, and transformations",
        topics: ["Function Notation", "Domain and Range", "Linear Functions", "Transformations"],
        duration: 90
    });

    // Physics Chapters
    const physicsChapter1 = await Chapter.create({
        textbookId: physicsTextbook.id,
        chapterNumber: 1,
        title: "Motion in a Straight Line",
        description: "Kinematics: displacement, velocity, and acceleration",
        topics: ["Displacement", "Velocity", "Acceleration", "Equations of Motion"],
        duration: 100
    });

    // CS Chapters
    const csChapter1 = await Chapter.create({
        textbookId: csTextbook.id,
        chapterNumber: 1,
        title: "Introduction to Databases",
        description: "Database concepts, models, and SQL basics",
        topics: ["Database Models", "SQL Basics", "Tables and Relationships"],
        duration: 80
    });
    console.log('✅ Created chapters');

    // Create Quizzes
    const mathQuiz1 = await Quiz.create({
        chapterId: mathChapter1.id,
        title: "Quadratic Equations - Practice Quiz",
        description: "Test your understanding of quadratic equations",
        difficulty: "medium",
        timeLimit: 30,
        passingScore: 70,
        isAutoGenerated: false,
        isActive: true,
        totalQuestions: 3
    });

    const physicsQuiz1 = await Quiz.create({
        chapterId: physicsChapter1.id,
        title: "Motion Basics Quiz",
        description: "Quick quiz on basic motion concepts",
        difficulty: "easy",
        timeLimit: 20,
        passingScore: 60,
        isAutoGenerated: false,
        isActive: true,
        totalQuestions: 3
    });
    console.log('✅ Created quizzes');

    // Math Quiz Questions
    await QuizQuestion.create({
        quizId: mathQuiz1.id,
        questionNumber: 1,
        questionText: "Solve for x: x² - 5x + 6 = 0",
        questionType: "multiple-choice",
        options: ["x = 2 or x = 3", "x = 1 or x = 6", "x = -2 or x = -3", "x = 0 or x = 5"],
        correctAnswer: "0",
        explanation: "Factor as (x-2)(x-3) = 0, so x = 2 or x = 3",
        points: 2,
        difficulty: "medium"
    });

    await QuizQuestion.create({
        quizId: mathQuiz1.id,
        questionNumber: 2,
        questionText: "What is the discriminant of 2x² + 3x - 2 = 0?",
        questionType: "multiple-choice",
        options: ["25", "16", "9", "1"],
        correctAnswer: "0",
        explanation: "Discriminant = b² - 4ac = 3² - 4(2)(-2) = 9 + 16 = 25",
        points: 2,
        difficulty: "medium"
    });

    await QuizQuestion.create({
        quizId: mathQuiz1.id,
        questionNumber: 3,
        questionText: "A quadratic equation always has two real solutions.",
        questionType: "true-false",
        options: ["True", "False"],
        correctAnswer: "false",
        explanation: "A quadratic can have two real, one real (repeated), or two complex solutions depending on the discriminant.",
        points: 1,
        difficulty: "easy"
    });

    // Physics Quiz Questions
    await QuizQuestion.create({
        quizId: physicsQuiz1.id,
        questionNumber: 1,
        questionText: "What is the SI unit of velocity?",
        questionType: "multiple-choice",
        options: ["m/s", "m/s²", "m", "s"],
        correctAnswer: "0",
        explanation: "Velocity is measured in meters per second (m/s)",
        points: 1,
        difficulty: "easy"
    });

    await QuizQuestion.create({
        quizId: physicsQuiz1.id,
        questionNumber: 2,
        questionText: "If a car travels 100m in 5 seconds, what is its average velocity?",
        questionType: "multiple-choice",
        options: ["20 m/s", "15 m/s", "25 m/s", "10 m/s"],
        correctAnswer: "0",
        explanation: "Average velocity = displacement/time = 100m/5s = 20 m/s",
        points: 2,
        difficulty: "easy"
    });

    await QuizQuestion.create({
        quizId: physicsQuiz1.id,
        questionNumber: 3,
        questionText: "Acceleration is the rate of change of velocity.",
        questionType: "true-false",
        options: ["True", "False"],
        correctAnswer: "true",
        explanation: "Acceleration is defined as the rate of change of velocity with respect to time.",
        points: 1,
        difficulty: "easy"
    });
    console.log('✅ Created quiz questions');

    // Create a sample quiz result for Jessica
    await QuizUserResult.create({
        quizId: physicsQuiz1.id,
        userId: student1.id,
        score: 75.00,
        totalQuestions: 3,
        correctAnswers: 3,
        answers: [
            { questionId: 1, userAnswer: "0", correctAnswer: "0", isCorrect: true, points: 1, explanation: "Velocity is measured in meters per second (m/s)" },
            { questionId: 2, userAnswer: "0", correctAnswer: "0", isCorrect: true, points: 2, explanation: "Average velocity = displacement/time = 100m/5s = 20 m/s" },
            { questionId: 3, userAnswer: "true", correctAnswer: "true", isCorrect: true, points: 1, explanation: "Acceleration is defined as the rate of change of velocity with respect to time." }
        ],
        timeTaken: 180,
        passed: true,
        completedAt: new Date('2025-11-20T15:30:00')
    });
    console.log('✅ Created quiz results');

    console.log('\n🎉 Database seeded successfully!');
    console.log('\n📝 Test credentials:');
    console.log('   Student: jessica@example.com / password123');
    console.log('   Student: john@example.com / password123');
    console.log('   Admin: admin@example.com / password123');
    console.log('\n📚 Sample data created:');
    console.log('   ✅ 3 Textbooks (Math, Physics, CS)');
    console.log('   ✅ 4 Chapters');
    console.log('   ✅ 2 Quizzes with 6 questions');
    console.log('   ✅ 2 User profiles');
    console.log('   ✅ 3 Study goals');
    console.log('   ✅ 10 Motivational quotes');
};

seedData()
    .then(() => {
        console.log('\n✅ Seed completed');
        process.exit(0);
    })
    .catch(err => {
        console.error('❌ Seed failed:', err);
        process.exit(1);
    });
