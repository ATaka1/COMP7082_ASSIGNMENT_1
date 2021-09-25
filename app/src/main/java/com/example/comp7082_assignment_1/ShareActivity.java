package com.example.comp7082_assignment_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class ShareActivity extends AppCompatActivity {

    public String file = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Get the incoming file path from main activity.
        Intent incomingIntent = getIntent();
        file = incomingIntent.getStringExtra("filepath");

        // Display photo
        displayPhoto(file);
    }

    // Display the photo user wants to share on the screen.
    public void displayPhoto(String filepath) {
        ImageView image = findViewById(R.id.shareImage);
        image.setImageBitmap(BitmapFactory.decodeFile(filepath));
    }

    // Return to the main activity
    public void returnMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Creates Action Send intent to share photo with other applications.
    public void shareImage(View view){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        // Easiest way I found to get a proper URI for the image. File --> getUriForFile() using FileProvider
        File myFile = new File(file);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.comp7082_assignment_1.fileprovider", myFile);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Start sharing activity, createChooser lets user choose application to share to.
        startActivity(Intent.createChooser(share, "Share image"));
    }

}