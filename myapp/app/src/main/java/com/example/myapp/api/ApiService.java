package com.example.myapp.api;

import com.example.myapp.Tutor;
import com.example.myapp.models.AnalyticsSummaryResponse;
import com.example.myapp.models.Booking;
import com.example.myapp.models.BookingRequest;
import com.example.myapp.models.BookingResponse;
import com.example.myapp.models.CreateStudySessionRequest;
import com.example.myapp.models.DailyAnalyticsResponse;
import com.example.myapp.models.LoginRequest;
import com.example.myapp.models.LoginResponse;
import com.example.myapp.models.PeriodAnalyticsResponse;
import com.example.myapp.models.ProfileResponse;
import com.example.myapp.models.QuoteResponse;
import com.example.myapp.models.QuotesListResponse;
import com.example.myapp.models.ReviewRequest;
import com.example.myapp.models.ReviewResponse;
import com.example.myapp.models.SignupRequest;
import com.example.myapp.models.SignupResponse;
import com.example.myapp.models.StudyGoal;
import com.example.myapp.models.StudyGoalResponse;
import com.example.myapp.models.StudyGoalsListResponse;
import com.example.myapp.models.StudySessionResponse;
import com.example.myapp.models.StudySessionsListResponse;
import com.example.myapp.models.StudySummaryResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Authentication endpoints
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/signup")
    Call<SignupResponse> signup(@Body SignupRequest signupRequest);

    // Tutor endpoints
    @GET("api/tutors")
    Call<List<Tutor>> getTutors();

    @GET("api/tutors")
    Call<List<Tutor>> searchTutors(
        @Query("name") String name,
        @Query("subject") String subject,
        @Query("date") String date,
        @Query("rating") String rating
    );

    @GET("api/tutors/{id}")
    Call<Tutor> getTutorDetails(@Path("id") int id);

    // Study Session endpoints (require authentication)
    @POST("api/sessions/study")
    Call<StudySessionResponse> createStudySession(@Body CreateStudySessionRequest request);

    @GET("api/sessions/study")
    Call<StudySessionsListResponse> getStudySessions();

    @GET("api/sessions/study/summary")
    Call<StudySummaryResponse> getStudySessionsSummary();

    @GET("api/sessions/study/{id}")
    Call<StudySessionResponse> getStudySession(@Path("id") int id);

    @PUT("api/sessions/study/{id}")
    Call<StudySessionResponse> updateStudySession(@Path("id") int id, @Body CreateStudySessionRequest request);

    @DELETE("api/sessions/study/{id}")
    Call<Void> deleteStudySession(@Path("id") int id);

    // Profile endpoints (require authentication)
    @GET("api/profiles/me")
    Call<ProfileResponse> getMyProfile();

    @PUT("api/profiles/me")
    Call<ProfileResponse> updateProfile(@Body ProfileResponse request);

    // Analytics endpoints (require authentication)
    @GET("api/profiles/analytics/summary")
    Call<AnalyticsSummaryResponse> getAnalyticsSummary();

    @GET("api/profiles/analytics/daily")
    Call<DailyAnalyticsResponse> getDailyAnalytics(@Query("date") String date);

    @GET("api/profiles/analytics/weekly")
    Call<PeriodAnalyticsResponse> getWeeklyAnalytics();

    @GET("api/profiles/analytics/monthly")
    Call<PeriodAnalyticsResponse> getMonthlyAnalytics();

    // Booking endpoints
    @POST("api/bookings")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);

    @PUT("api/bookings/{id}")
    Call<Booking> updateBooking(@Path("id") int id, @Body Booking booking);

    @GET("api/bookings/{id}")
    Call<Booking> getBooking(@Path("id") int id);

    // Session endpoints (Tutor Bookings)
    @GET("api/sessions/upcoming")
    Call<List<Booking>> getUpcomingSessions(@Query("userId") int userId);

    @GET("api/sessions/past")
    Call<List<Booking>> getPastSessions(@Query("userId") int userId);

    // Review endpoints
    @POST("api/reviews")
    Call<ReviewResponse> createReview(@Body ReviewRequest reviewRequest);

    @GET("api/reviews")
    Call<List<ReviewResponse>> getReviews(@Query("tutorId") Integer tutorId);

    // Study Goals endpoints (require authentication)
    @GET("api/profiles/goals")
    Call<StudyGoalsListResponse> getStudyGoals();

    @POST("api/profiles/goals")
    Call<StudyGoalResponse> createStudyGoal(@Body StudyGoal goal);

    @PUT("api/profiles/goals/{id}")
    Call<StudyGoalResponse> updateStudyGoal(@Path("id") int id, @Body StudyGoal goal);

    @DELETE("api/profiles/goals/{id}")
    Call<Void> deleteStudyGoal(@Path("id") int id);

    // Motivational Quotes endpoints (require authentication)
    @GET("api/profiles/quotes/random")
    Call<QuoteResponse> getRandomQuote(@Query("category") String category);

    @GET("api/profiles/quotes")
    Call<QuotesListResponse> getQuotes(@Query("category") String category, @Query("limit") Integer limit);

    // ============================================
    // Textbook & Chapter Endpoints
    // ============================================

    /**
     * Get all textbooks with optional filters
     */
    @GET("api/textbooks")
    Call<com.example.myapp.models.TextbooksResponse> getTextbooks(
        @Query("subject") String subject,
        @Query("grade") String grade,
        @Query("level") String level
    );

    /**
     * Get single textbook with chapters
     */
    @GET("api/textbooks/{id}")
    Call<com.example.myapp.models.TextbookResponse> getTextbook(@Path("id") int id);

    /**
     * Get specific chapter with quizzes
     */
    @GET("api/textbooks/{textbookId}/chapters/{chapterId}")
    Call<com.example.myapp.models.ChapterResponse> getChapter(
        @Path("textbookId") int textbookId,
        @Path("chapterId") int chapterId
    );

    // ============================================
    // Quiz Endpoints
    // ============================================

    /**
     * Get all quizzes with optional filters
     */
    @GET("api/quizzes")
    Call<com.example.myapp.models.QuizzesResponse> getQuizzes(
        @Query("chapterId") Integer chapterId,
        @Query("difficulty") String difficulty
    );

    /**
     * Get specific quiz with questions (without answers)
     */
    @GET("api/quizzes/{id}")
    Call<com.example.myapp.models.QuizResponse> getQuiz(@Path("id") int id);

    /**
     * Submit quiz answers and get results (requires authentication)
     */
    @POST("api/quizzes/{id}/submit")
    Call<com.example.myapp.models.QuizResultResponse> submitQuiz(
        @Path("id") int id,
        @Body com.example.myapp.models.QuizSubmitRequest request
    );

    // ============================================
    // AI Quiz Generation Endpoints (Admin only)
    // ============================================

    /**
     * Generate AI quiz for a chapter (requires admin authentication)
     */
    @POST("api/quizzes/generate")
    Call<com.example.myapp.models.QuizResponse> generateAIQuiz(
        @Body com.example.myapp.models.GenerateQuizRequest request
    );

    /**
     * Regenerate questions for existing quiz (requires admin authentication)
     */
    @POST("api/quizzes/{quizId}/regenerate")
    Call<com.example.myapp.models.QuizResponse> regenerateQuiz(
        @Path("quizId") int quizId,
        @Body com.example.myapp.models.GenerateQuizRequest request
    );
}
