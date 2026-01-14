const axios = require('axios');

const API_URL = 'http://localhost:3000/api';
const TEST_USER_ID = 999;
const TEST_TUTOR_ID = 1; // Assuming Tutor 1 exists from seed

async function runTests() {
    console.log('🚀 Starting Verification of Modules 3 & 4...\n');

    try {
        // --- Module 3: Tutor Discovery ---
        console.log('1️⃣  Testing Tutor Discovery...');

        // 1. Search by Subject
        const searchRes = await axios.get(`${API_URL}/tutors?subject=Math`);
        if (searchRes.data.length > 0) console.log('   ✅ Search by Subject: Passed');
        else console.error('   ❌ Search by Subject: Failed (No tutors found)');

        // 1.1 Search by Name
        const nameRes = await axios.get(`${API_URL}/tutors?name=Emily`);
        if (nameRes.data.length > 0 && nameRes.data[0].name.includes('Emily')) console.log('   ✅ Search by Name: Passed');
        else console.error('   ❌ Search by Name: Failed');

        // 1.2 Search by Rating
        const ratingRes = await axios.get(`${API_URL}/tutors?rating=4`);
        if (ratingRes.data.length > 0) console.log('   ✅ Search by Rating: Passed');
        else console.error('   ❌ Search by Rating: Failed');

        // 2. Search by Date
        const dateRes = await axios.get(`${API_URL}/tutors?date=2025-12-01`);
        if (dateRes.data.length > 0) console.log('   ✅ Search by Date: Passed');
        else console.error('   ❌ Search by Date: Failed');

        // 3. Get Details
        const detailRes = await axios.get(`${API_URL}/tutors/${TEST_TUTOR_ID}`);
        if (detailRes.data.name) console.log('   ✅ Get Tutor Details: Passed');
        else console.error('   ❌ Get Tutor Details: Failed');


        // --- Module 3: Booking System ---
        console.log('\n2️⃣  Testing Booking System...');

        const bookingData = {
            tutorId: TEST_TUTOR_ID,
            userId: TEST_USER_ID,
            date: "2025-12-25",
            time: "14:00",
            subject: "Physics"
        };

        // 4. Create Booking
        let bookingId;
        try {
            const bookRes = await axios.post(`${API_URL}/bookings`, bookingData);
            bookingId = bookRes.data.id;
            console.log('   ✅ Create Booking: Passed');
        } catch (e) {
            console.error('   ❌ Create Booking: Failed', e.response?.data || e.message);
        }

        // 5. Prevent Double Booking
        try {
            await axios.post(`${API_URL}/bookings`, bookingData);
            console.error('   ❌ Prevent Double Booking: Failed (Should have errored)');
        } catch (e) {
            if (e.response && e.response.status === 409) {
                console.log('   ✅ Prevent Double Booking: Passed');
            } else {
                console.error('   ❌ Prevent Double Booking: Failed with unexpected error', e.message);
            }
        }


        // --- Module 4: My Sessions ---
        console.log('\n3️⃣  Testing My Sessions...');

        // 6. Get Upcoming Sessions
        const upcomingRes = await axios.get(`${API_URL}/sessions/upcoming?userId=${TEST_USER_ID}`);
        const myBooking = upcomingRes.data.find(b => b.id === bookingId);
        if (myBooking) console.log('   ✅ Get Upcoming Sessions: Passed');
        else console.error('   ❌ Get Upcoming Sessions: Failed (Booking not found)');

        // 6.1 Get Past Sessions
        // Create a temporary booking and set it to completed/past
        let pastBookingId;
        try {
            const pastBookRes = await axios.post(`${API_URL}/bookings`, {
                ...bookingData,
                date: "2024-01-01",
                time: "10:00"
            });
            pastBookingId = pastBookRes.data.id;

            // Update to Completed
            await axios.put(`${API_URL}/bookings/${pastBookingId}`, { status: 'Completed' });

            const pastRes = await axios.get(`${API_URL}/sessions/past?userId=${TEST_USER_ID}`);
            const myPastBooking = pastRes.data.find(b => b.id === pastBookingId);
            if (myPastBooking) console.log('   ✅ Get Past Sessions: Passed');
            else console.error('   ❌ Get Past Sessions: Failed');

        } catch (e) {
            console.error('   ❌ Get Past Sessions: Failed', e.message);
        } // logic to ensure we don't crash if double booking or something

        // --- Module 4: Reviews ---
        console.log('\n4️⃣  Testing Reviews...');

        // 7. Post Review (Linked to Booking)
        try {
            await axios.post(`${API_URL}/reviews`, {
                tutorId: TEST_TUTOR_ID,
                bookingId: bookingId,
                studentName: "Test User",
                rating: 5,
                content: "Great session!"
            });
            console.log('   ✅ Post Review: Passed');
        } catch (e) {
            console.error('   ❌ Post Review: Failed', e.response?.data || e.message);
        }


        // --- Module 4: Chat System ---
        console.log('\n5️⃣  Testing Chat System...');

        // 8. Send Message
        await axios.post(`${API_URL}/messages`, {
            senderId: TEST_USER_ID,
            receiverId: TEST_TUTOR_ID,
            content: "Hello Tutor!"
        });
        console.log('   ✅ Send Message: Passed');

        // 9. Get Messages
        const msgRes = await axios.get(`${API_URL}/messages?userId=${TEST_USER_ID}`);
        if (msgRes.data.length > 0) console.log('   ✅ Get Messages: Passed');
        else console.error('   ❌ Get Messages: Failed');


        // --- Cleanup (Cancel Booking) ---
        console.log('\n🧹 Cleaning up...');
        if (bookingId) await axios.put(`${API_URL}/bookings/${bookingId}`, { status: 'Cancelled' });
        if (pastBookingId) await axios.put(`${API_URL}/bookings/${pastBookingId}`, { status: 'Cancelled' });
        console.log('   ✅ Bookings Cancelled/Cleaned');

        console.log('\n🎉 ALL TESTS COMPLETED!');

    } catch (err) {
        console.error('\n❌ FATAL ERROR:', err.message);
    }
}

runTests();
