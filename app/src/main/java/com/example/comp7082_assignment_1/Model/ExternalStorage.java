package com.example.comp7082_assignment_1.Model;

import android.content.Context;
import android.os.Environment;

import com.example.comp7082_assignment_1.Gallery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExternalStorage implements Gallery.ExternalStorageAccess {

    public ExternalStorage() {

    }

    @Override
    public File createImageFile(String locationString, Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "_caption_" + locationString + "_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    @Override
    public ArrayList<String> getPhotoList(Date startTimestamp, Date endTimestamp, String keywords, String latitude, String longitude) {
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


}
