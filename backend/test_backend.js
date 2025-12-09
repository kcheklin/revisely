const axios = require('axios');

const BASE_URL = 'http://localhost:3000/api';

async function testSystem() {
    try {
        console.log('--- 1. Testing Get Tutors ---');
        const tutors = await axios.get(`${BASE_URL}/tutors`);
        console.log(`✅ Success: Found ${tutors.data.length} tutors.`);
        const tutorId = tutors.data[0].id;

        console.log('\n--- 2. Testing Create Booking ---');
        const bookingData = {
            tutorId: tutorId,
            studentName: "Test Student",
            subject: "Mathematics",
            date: "2025-12-05",
            time: "10:00",
            status: "Upcoming"
        };
        const booking = await axios.post(`${BASE_URL}/bookings`, bookingData);
        console.log('✅ Success: Booking created:', booking.data.id);

        console.log('\n--- 3. Testing Double Booking Prevention ---');
        try {
            await axios.post(`${BASE_URL}/bookings`, bookingData);
            console.log('❌ Failed: Should have prevented double booking!');
        } catch (error) {
            if (error.response && error.response.status === 409) {
                console.log('✅ Success: Double booking prevented correctly.');
            } else {
                console.log('❌ Failed: Unexpected error:', error.message);
            }
        }

        console.log('\n--- 4. Testing Get Upcoming Sessions ---');
        const sessions = await axios.get(`${BASE_URL}/sessions/upcoming`);
        const mySession = sessions.data.find(s => s.id === booking.data.id);
        if (mySession) {
            console.log('✅ Success: Found the new booking in upcoming sessions.');
        } else {
            console.log('❌ Failed: Booking not found in upcoming sessions.');
        }

    } catch (error) {
        console.error('❌ System Test Failed:', error.message);
    }
}

testSystem();
