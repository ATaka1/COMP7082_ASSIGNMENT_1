package com.example.comp7082_assignment_1;

import android.view.View;

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

    interface SearchView {

    };

}
