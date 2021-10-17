package com.example.comp7082_assignment_1.Presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.Model.ExternalStorage;
import com.example.comp7082_assignment_1.Model.LocationClient;
import com.example.comp7082_assignment_1.View.MainActivity;
import com.example.comp7082_assignment_1.View.SearchActivity;
import com.example.comp7082_assignment_1.View.ShareActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivityPresenter extends AppCompatActivity implements Gallery.MainActivityPresenter{

    private Gallery.MainActivityView view;
    private Context context;
    private Activity activity;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String locationStr;
    private FusedLocationProviderClient fusedLocationClient;
    private String mCurrentPhotoPath;
    private ExternalStorage storage;

    public MainActivityPresenter(Gallery.MainActivityView view, Context context, Activity activity) {
        this.view = view;
        this.context = context;
        this.activity = activity;
        this.fusedLocationClient = LocationClient.getInstance(activity).getLocationClient();
        this.storage = new ExternalStorage();
    };

    @Override
    public void takePhoto(View view) {
        locationStr = "";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                locationStr = Location.convert(location.getLatitude(), Location.FORMAT_DEGREES)
                                        + "," + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
                            }
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(context, "com.example.comp7082_assignment_1.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        });
            } else {
                oldTakePhoto(view);
            }
        }
    }

    @Override
    public void oldTakePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.comp7082_assignment_1.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    public File createImageFile() throws IOException{
        // Create an image file name
        File image = storage.createImageFile(locationStr, context);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String latitude, String longitude) {
        ArrayList<String> photos = storage.getPhotoList(startTimestamp, endTimestamp, keywords, latitude, longitude);
        return photos;
    };


};
