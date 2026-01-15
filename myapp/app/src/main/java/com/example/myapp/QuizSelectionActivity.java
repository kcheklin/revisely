package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.models.Chapter;
import com.example.myapp.models.Quiz;
import com.example.myapp.models.Textbook;
import com.example.myapp.repositories.QuizRepository;

import java.util.ArrayList;
import java.util.List;

public class QuizSelectionActivity extends AppCompatActivity {

    private Spinner subjectSpinner, gradeSpinner, chapterSpinner;
    private Button startQuizButton;
    private LinearLayout btnNavHome, btnNavSearch, btnNavUpcoming, btnNavProfile;
    
    private QuizRepository quizRepository;
    private List<Textbook> textbooksList;
    private List<Chapter> chaptersList;
    private List<Quiz> quizzesList;
    private Textbook selectedTextbook;
    private Chapter selectedChapter;
    private Quiz selectedQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        gradeSpinner = findViewById(R.id.gradeSpinner);
        chapterSpinner = findViewById(R.id.chapterSpinner);
        startQuizButton = findViewById(R.id.startQuizButton);

        btnNavHome = findViewById(R.id.navHome);
        btnNavSearch = findViewById(R.id.navSearch);
        btnNavUpcoming = findViewById(R.id.navUpcoming);
        btnNavProfile = findViewById(R.id.navProfile);

        quizRepository = new QuizRepository(this);
        textbooksList = new ArrayList<>();
        chaptersList = new ArrayList<>();
        quizzesList = new ArrayList<>();

        setupSpinners();
        setupBottomNavigation();
        loadTextbooks();

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedQuiz != null) {
                    Intent intent = new Intent(QuizSelectionActivity.this, QuestionActivity.class);
                    intent.putExtra("quizId", selectedQuiz.getId());
                    intent.putExtra("quizTitle", selectedQuiz.getTitle());
                    startActivity(intent);
                } else {
                    Toast.makeText(QuizSelectionActivity.this, 
                        "Please select a quiz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupSpinners() {
        // Subject spinner - will be populated from backend data
        List<String> subjects = new ArrayList<>();
        subjects.add("Select a subject");
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedSubject = parent.getItemAtPosition(position).toString();
                    filterTextbooksBySubject(selectedSubject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Grade/Textbook spinner
        List<String> grades = new ArrayList<>();
        grades.add("Select a textbook");
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, grades);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && textbooksList != null && position <= textbooksList.size()) {
                    selectedTextbook = textbooksList.get(position - 1);
                    loadChapters(selectedTextbook.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Chapter spinner
        List<String> chapters = new ArrayList<>();
        chapters.add("Select a chapter");
        ArrayAdapter<String> chapterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chapters);
        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapterAdapter);

        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && chaptersList != null && position <= chaptersList.size()) {
                    selectedChapter = chaptersList.get(position - 1);
                    loadQuizzes(selectedChapter.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadTextbooks() {
        quizRepository.getTextbooks(null, null, null, new QuizRepository.TextbooksCallback() {
            @Override
            public void onSuccess(List<Textbook> textbooks) {
                textbooksList = textbooks;
                updateSubjectSpinner(textbooks);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(QuizSelectionActivity.this, 
                    "Error loading textbooks: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSubjectSpinner(List<Textbook> textbooks) {
        List<String> subjects = new ArrayList<>();
        subjects.add("Select a subject");
        
        // Get unique subjects
        List<String> uniqueSubjects = new ArrayList<>();
        for (Textbook textbook : textbooks) {
            if (!uniqueSubjects.contains(textbook.getSubject())) {
                uniqueSubjects.add(textbook.getSubject());
            }
        }
        subjects.addAll(uniqueSubjects);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
    }

    private void filterTextbooksBySubject(String subject) {
        List<String> textbookTitles = new ArrayList<>();
        textbookTitles.add("Select a textbook");

        List<Textbook> filteredTextbooks = new ArrayList<>();
        for (Textbook textbook : textbooksList) {
            if (textbook.getSubject().equals(subject)) {
                filteredTextbooks.add(textbook);
                textbookTitles.add(textbook.getTitle() + " - " + textbook.getGrade());
            }
        }
        
        textbooksList = filteredTextbooks;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, textbookTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);
    }

    private void loadChapters(int textbookId) {
        quizRepository.getTextbook(textbookId, new QuizRepository.TextbookCallback() {
            @Override
            public void onSuccess(Textbook textbook) {
                chaptersList = textbook.getChapters();
                updateChapterSpinner(textbook.getChapters());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(QuizSelectionActivity.this, 
                    "Error loading chapters: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateChapterSpinner(List<Chapter> chapters) {
        List<String> chapterTitles = new ArrayList<>();
        chapterTitles.add("Select a chapter");

        for (Chapter chapter : chapters) {
            chapterTitles.add("Chapter " + chapter.getChapterNumber() + ": " + chapter.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chapterTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(adapter);
    }

    private void loadQuizzes(int chapterId) {
        quizRepository.getQuizzes(chapterId, null, new QuizRepository.QuizzesCallback() {
            @Override
            public void onSuccess(List<Quiz> quizzes) {
                quizzesList = quizzes;
                if (quizzes != null && !quizzes.isEmpty()) {
                    // Select the first available quiz
                    selectedQuiz = quizzes.get(0);
                    Toast.makeText(QuizSelectionActivity.this, 
                        "Quiz loaded: " + selectedQuiz.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    selectedQuiz = null;
                    Toast.makeText(QuizSelectionActivity.this, 
                        "No quizzes available for this chapter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(QuizSelectionActivity.this, 
                    "Error loading quizzes: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        btnNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, StudentHomepageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Navigate to Search and Book
        btnNavSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this,  TutorDiscoveryContainerActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Upcoming Session
        btnNavUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, UpcomingSessionsActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Profile
        btnNavProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizSelectionActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}