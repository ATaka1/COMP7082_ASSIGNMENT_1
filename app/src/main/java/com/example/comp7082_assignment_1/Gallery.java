package com.example.comp7082_assignment_1;

import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public interface Gallery {

    interface ShareView {

    }

    interface SharePresenter {
        void shareImage();
        void displayPhoto(String filepath);
    }

    interface SearchPresenter {
        void cancel(final View v);
        void go(final View v);
        void createCalendar();
    }

    interface SearchView {};

    interface MainActivityView {
        void startShareActivity(View view);
        void startSearchActivity(View view);
    };

    interface MainActivityPresenter {
        void takePhoto(View view);
        void oldTakePhoto(View view);
        File createImageFile() throws IOException;
        ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, String latitude, String longitude);
    };

}
