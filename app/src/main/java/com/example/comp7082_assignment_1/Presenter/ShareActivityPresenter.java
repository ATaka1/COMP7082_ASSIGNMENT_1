package com.example.comp7082_assignment_1.Presenter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import com.example.comp7082_assignment_1.R;
import com.example.comp7082_assignment_1.View.ShareActivity;

import java.io.OutputStream;

public class ShareActivityPresenter extends AppCompatActivity implements Gallery.SharePresenter {

    private static final int REQUEST = 112;
    Gallery.ShareView view;
    private String file = "";
    private Context context;
    private Activity activity;

    public ShareActivityPresenter(Gallery.ShareView view, String file, Context context, Activity activity) {
        this.view = view;
        this.file = file;
        this.context = context;
        this.activity = activity;
    }

    // This function will create and start the share intent. Files are inserted into MediaStore.Images
    // so that other applications will be willing to read the data (the image).
    public void executeShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        Bitmap bm = BitmapFactory.decodeFile(file);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outStream;
        try {
            outStream = activity.getContentResolver().openOutputStream(uri);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, uri);
        // Start sharing activity, createChooser lets user choose application to share to.
        activity.startActivity(Intent.createChooser(share, "Share image"));
    }

    @Override
    public void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST);
            } else {
                executeShareIntent();
            }
        } else {
            executeShareIntent();
        }
    }

    // When user says yes or no to permission request this will check and then
    // run executeShareIntent.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    executeShareIntent();
                } else {
                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
