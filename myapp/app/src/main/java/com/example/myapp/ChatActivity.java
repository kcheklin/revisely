package com.example.myapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend, btnBackChat;
    private ChatAdapter adapter;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBackChat = findViewById(R.id.btnBackChat);

        // Back button
        btnBackChat.setOnClickListener(v -> finish());

        // Sample messages
        messages = new ArrayList<>();
        messages.add(new Message("How was your session?", false, "10:30 AM"));
        messages.add(new Message("That's awesome!", true, "10:32 AM"));
        messages.add(new Message("Thank you", true, "10:32 AM"));

        adapter = new ChatAdapter(messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        // Send button
        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                messages.add(new Message(text, true, "Just now"));
                adapter.notifyItemInserted(messages.size() - 1);
                rvMessages.scrollToPosition(messages.size() - 1);
                etMessage.setText("");
            }
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navSearch = findViewById(R.id.navSearch);
        LinearLayout navUpcoming = findViewById(R.id.navUpcoming);
        LinearLayout navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, StudentHomepageActivity.class)));
        navSearch.setOnClickListener(v -> startActivity(new Intent(this, TutorDiscoveryContainerActivity.class)));
        navUpcoming.setOnClickListener(v -> startActivity(new Intent(this, UpcomingSessionsActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
