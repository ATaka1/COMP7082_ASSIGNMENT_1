package com.example.comp7082_assignment_1.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.Model.LocationClient;
import com.example.comp7082_assignment_1.Presenter.MainActivityPresenter;
import com.example.comp7082_assignment_1.R;
import com.example.comp7082_assignment_1.View.SearchActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Gallery.MainActivityView {
    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    private ArrayList<String> photos = null;
    private int index = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private FusedLocationProviderClient fusedLocationClient;
    private String locationStr;
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainActivityPresenter(this, getApplicationContext(), this);
        photos = presenter.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        setContentView(R.layout.activity_main);
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        fusedLocationClient = LocationClient.getInstance(this).getLocationClient();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Notice:")
                            .setMessage("Without location access, PhotoGallery will be unable to record location data for photos,"
                                    + "and location search will not work correctly.")
                            .setPositiveButton(android.R.string.yes, null)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

    @Override
    public void startSearchActivity(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void startShareActivity(View v) {
        Intent intent = new Intent(this, ShareActivity.class);
        // Check if there are any photos.
        if (photos != null) {
            intent.putExtra("filepath", photos.get(index));
            startActivity(intent);
            // If there are no photos then let the user know.
        } else {
            Toast.makeText(getApplicationContext(), "No photos yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void takePhoto(View v) {
        presenter.takePhoto(v);
    }

    public void scrollPhotos(View v) {
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
        Log.i("MAINACTIVITYLOL", photos.get(index));
        switch (v.getId()) {
            case R.id.btnPrev:
                if (index > 0) {
                    index = index - 1;
                    System.out.println("The Index for Prev is " + index);
                }
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index = index + 1;
                    System.out.println("The Index for Next is " + index);
                }
                break;
            default:
                break;
        }
        displayPhoto(photos.get(index));
    }

    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        TextView longtv = (TextView) findViewById(R.id.tvLong);
        TextView lattv = (TextView) findViewById(R.id.tvLat);
        if (path == null || path == "") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
            longtv.setText("");
            lattv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            String[] coords = attr[4].split(",");
            et.setText(attr[3]);
            String tempTv = attr[4] + " / " + attr[5];
            String lat = "";
            String longitude = "";
            if(attr.length > 6) {
                tempTv = attr[5] + " / " + attr[6];
            }
            if(coords.length > 1) {
                lat = coords[0];
                longitude = coords[1];
            }
            tv.setText(tempTv);
            lattv.setText(lat);
            longtv.setText(longitude);
        }
    }


    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("/");
        String a = attr[9];
        String[] attr2 = a.split("_");
        String filePath = attr[0] + "/" + attr[1] + "/" + attr[2] + "/" + attr[3] + "/" + attr[4] + "/" + attr[5] + "/" + attr[6] + "/" + attr[7] + "/" + attr[8] + "/";
        if (attr2.length >= 3) {
            String tmpPath = filePath + attr2[0] + "_" + caption + "_" + attr2[2] + "_" + attr2[3];
            if (attr2.length > 4) {
                tmpPath += "_" + attr2[4];
            }
            File to = new File(tmpPath);
            File from = new File(path);
            from.renameTo(to);
            photos = presenter.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                Date startTimestamp , endTimestamp;
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);
                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                }
                String keywords = (String) data.getStringExtra("KEYWORDS");
                String latitude = (String) data.getStringExtra("LATITUDE");
                String longitude = (String) data.getStringExtra("LONGITUDE");
                index = 0;
                photos = presenter.findPhotos(startTimestamp, endTimestamp, keywords, latitude, longitude);
                if (photos.size() == 0) {
                    displayPhoto(null);
                } else {
                    displayPhoto(photos.get(index));
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = presenter.findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "","");
        }
    }

}