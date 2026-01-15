package com.example.myapp.repositories;

import android.content.Context;
import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;
import com.example.myapp.models.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRepository {
    private ApiService apiService;
    private ApiService authApiService;

    public QuizRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.authApiService = RetrofitClient.getAuthenticatedApiService(context);
    }

    // ============================================
    // Textbook Operations
    // ============================================

    /**
     * Get all textbooks
     */
    public void getTextbooks(String subject, String grade, String level, 
                            final TextbooksCallback callback) {
        Call<TextbooksResponse> call = apiService.getTextbooks(subject, grade, level);
        call.enqueue(new Callback<TextbooksResponse>() {
            @Override
            public void onResponse(Call<TextbooksResponse> call, 
                                 Response<TextbooksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getTextbooks());
                } else {
                    callback.onError("Failed to load textbooks: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TextbooksResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get single textbook with chapters
     */
    public void getTextbook(int textbookId, final TextbookCallback callback) {
        Call<TextbookResponse> call = apiService.getTextbook(textbookId);
        call.enqueue(new Callback<TextbookResponse>() {
            @Override
            public void onResponse(Call<TextbookResponse> call, 
                                 Response<TextbookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getTextbook());
                } else {
                    callback.onError("Failed to load textbook: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TextbookResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get chapter with quizzes
     */
    public void getChapter(int textbookId, int chapterId, 
                          final ChapterCallback callback) {
        Call<ChapterResponse> call = apiService.getChapter(textbookId, chapterId);
        call.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, 
                                 Response<ChapterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getChapter());
                } else {
                    callback.onError("Failed to load chapter: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // ============================================
    // Quiz Operations
    // ============================================

    /**
     * Get all quizzes with optional filters
     */
    public void getQuizzes(Integer chapterId, String difficulty, 
                          final QuizzesCallback callback) {
        Call<QuizzesResponse> call = apiService.getQuizzes(chapterId, difficulty);
        call.enqueue(new Callback<QuizzesResponse>() {
            @Override
            public void onResponse(Call<QuizzesResponse> call, 
                                 Response<QuizzesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getQuizzes());
                } else {
                    callback.onError("Failed to load quizzes: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizzesResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Get specific quiz with questions
     */
    public void getQuiz(int quizId, final QuizCallback callback) {
        Call<QuizResponse> call = apiService.getQuiz(quizId);
        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, 
                                 Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getQuiz());
                } else {
                    callback.onError("Failed to load quiz: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Submit quiz answers and get results
     */
    public void submitQuiz(int quizId, QuizSubmitRequest request, 
                          final QuizResultCallback callback) {
        Call<QuizResultResponse> call = authApiService.submitQuiz(quizId, request);
        call.enqueue(new Callback<QuizResultResponse>() {
            @Override
            public void onResponse(Call<QuizResultResponse> call, 
                                 Response<QuizResultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    callback.onError("Failed to submit quiz: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizResultResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // ============================================
    // AI Quiz Generation (Admin only)
    // ============================================

    /**
     * Generate AI quiz for a chapter
     */
    public void generateAIQuiz(GenerateQuizRequest request, 
                              final QuizCallback callback) {
        Call<QuizResponse> call = authApiService.generateAIQuiz(request);
        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, 
                                 Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getQuiz());
                } else {
                    callback.onError("Failed to generate quiz: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Regenerate questions for existing quiz
     */
    public void regenerateQuiz(int quizId, GenerateQuizRequest request, 
                              final QuizCallback callback) {
        Call<QuizResponse> call = authApiService.regenerateQuiz(quizId, request);
        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, 
                                 Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getQuiz());
                } else {
                    callback.onError("Failed to regenerate quiz: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // ============================================
    // Callback Interfaces
    // ============================================

    public interface TextbooksCallback {
        void onSuccess(java.util.List<Textbook> textbooks);
        void onError(String message);
    }

    public interface TextbookCallback {
        void onSuccess(Textbook textbook);
        void onError(String message);
    }

    public interface ChapterCallback {
        void onSuccess(Chapter chapter);
        void onError(String message);
    }

    public interface QuizzesCallback {
        void onSuccess(java.util.List<Quiz> quizzes);
        void onError(String message);
    }

    public interface QuizCallback {
        void onSuccess(Quiz quiz);
        void onError(String message);
    }

    public interface QuizResultCallback {
        void onSuccess(QuizResultResponse.Result result);
        void onError(String message);
    }
}

