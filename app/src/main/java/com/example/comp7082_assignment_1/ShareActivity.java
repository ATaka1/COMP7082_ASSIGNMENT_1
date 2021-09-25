package com.example.comp7082_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Get the incoming file path from main activity.
        Intent incomingIntent = getIntent();
        String filePathToShare = incomingIntent.getStringExtra("filepath");

        // Display on share activity
        TextView display = findViewById(R.id.display);
        display.setText(filePathToShare);
    }
}