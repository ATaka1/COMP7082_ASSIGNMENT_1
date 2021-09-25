package com.example.comp7082_assignment_1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Console;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ISearch {
    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    private ArrayList<String> photos = null;
    private int index = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private FusedLocationProviderClient fusedLocationClient;
    private String locationStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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


    public void startSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

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

    public void oldtakePhoto(View v) {
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
    }

    public void takePhoto(View v) {
        locationStr = "";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
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
                                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.example.comp7082_assignment_1.fileprovider", photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            }
                        });

            } else {
                oldtakePhoto(v);
            }
        }
    }

    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String latitude, String longitude) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.comp7082_assignment_1/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                String[] attr = f.getPath().split("_");
                String f_keyword = attr[3];
                String f_lat = "";
                String f_long = "";
                if (attr.length > 6) {
                    String[] location = attr[4].split(",");
                    if (location.length > 1) {
                        f_lat = location[0];
                        f_long = location[1];
                    }
                }
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime()))
                        && (keywords == "" || f_keyword.contains(keywords))
                        && (latitude == "" || f_lat.startsWith(latitude))
                        && (longitude == "" || f_long.startsWith(longitude))) {
                    photos.add(f.getPath());
                }
            }
        }
        return photos;
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
        if (path == null || path == "") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            et.setText(attr[3]);
            String tempTv = attr[4] + " / " + attr[5];
            if (attr.length > 6) {
                tempTv = attr[5] + " / " + attr[6] + "\n Location: " + attr[4];
            }
            tv.setText(tempTv);
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
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "", "");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "_caption_" + locationStr + "_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
                photos = findPhotos(startTimestamp, endTimestamp, keywords, latitude, longitude);
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
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", "","");
        }
    }



}