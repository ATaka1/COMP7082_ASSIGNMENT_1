package com.example.comp7082_assignment_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.View.MainActivity;
import com.example.comp7082_assignment_1.Presenter.ShareActivityPresenter;
import com.example.comp7082_assignment_1.R;

public class ShareActivity extends AppCompatActivity implements Gallery.ShareView {

    public String file = "";
    final public int REQUEST = 112;
    ShareActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Get the incoming file path from main activity.
        Intent incomingIntent = getIntent();
        file = incomingIntent.getStringExtra("filepath");

        presenter = new ShareActivityPresenter(this, file, getApplicationContext(), this);

        // Display photo
        displayPhoto(file);
    }

    // Display the photo user wants to share on the screen.
    public void displayPhoto(String filepath) {
        presenter.displayPhoto(filepath);
    }

    // Return to the main activity
    public void returnMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Call Share Image
    public void shareImage(View view) {
        presenter.shareImage();
    }
}