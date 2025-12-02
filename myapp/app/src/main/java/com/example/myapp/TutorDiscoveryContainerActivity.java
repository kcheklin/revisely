package com.example.myapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TutorDiscoveryContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_discovery_container);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.discoveryFragmentContainer, new TutorDiscoveryFragment())
                    .commit();
        }
    }
}
