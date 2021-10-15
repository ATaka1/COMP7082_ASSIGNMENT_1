package com.example.comp7082_assignment_1.View;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.MainActivity;
import com.example.comp7082_assignment_1.Presenter.ShareActivityPresenter;
import com.example.comp7082_assignment_1.R;

import java.io.OutputStream;

public class ShareActivity extends AppCompatActivity implements Gallery.ShareView{

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
        ImageView image = findViewById(R.id.shareImage);
        image.setImageBitmap(BitmapFactory.decodeFile(filepath));
    }

    // Return to the main activity
    public void returnMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // This function will create and start the share intent. Files are inserted into MediaStore.Images
    // so that other applications will be willing to read the data (the image).
//    public void executeShareIntent() {
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/jpg");
//        Bitmap bm = BitmapFactory.decodeFile(file);
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "title");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        OutputStream outStream;
//        try {
//            outStream = getContentResolver().openOutputStream(uri);
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        // Start sharing activity, createChooser lets user choose application to share to.
//        startActivity(Intent.createChooser(share, "Share image"));
//    }

    // Checks user permissions for writing to external storage, and then executes executeShareIntent
    // as long as permission are granted.
    public void shareImage(View view) {

        // we want
        presenter.shareImage();

//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/jpg");
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
//            } else {
//                executeShareIntent();
//            }
//        } else {
//            executeShareIntent();
//        }
    }

    // When user says yes or no to permission request this will check and then
    // run executeShareIntent.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    executeShareIntent();
//                } else {
//                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

}