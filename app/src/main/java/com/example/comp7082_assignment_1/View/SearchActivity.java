package com.example.comp7082_assignment_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.Presenter.SearchActivityPresenter;
import com.example.comp7082_assignment_1.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements Gallery.SearchView{

    private SearchActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = new SearchActivityPresenter(this, getApplicationContext(), this);
        presenter.createCalendar();
    }

    public void cancel(final View v) {
        presenter.cancel(v);
    }

    public void go(final View v) {
        presenter.go(v);
    }
}