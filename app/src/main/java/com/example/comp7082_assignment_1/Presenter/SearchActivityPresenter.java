package com.example.comp7082_assignment_1.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.comp7082_assignment_1.Gallery;
import com.example.comp7082_assignment_1.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivityPresenter extends AppCompatActivity implements Gallery.SearchPresenter {

    private Gallery.SearchView view;
    private Context context;
    private Activity activity;

    public SearchActivityPresenter(Gallery.SearchView v, Context c, Activity a) {
        this.view = v;
        this.context = c;
        this.activity = a;
    }

    @Override
    public void cancel(View v) {
        activity.finish();
    }

    @Override
    public void go(View v) {
        Intent i = new Intent();
        EditText from = (EditText) activity.findViewById(R.id.etFromDateTime);
        EditText to = (EditText) activity.findViewById(R.id.etToDateTime);
        EditText keywords = (EditText) activity.findViewById(R.id.etKeywords);
        EditText latitude = (EditText) activity.findViewById(R.id.etLocationLat);
        EditText longitude = (EditText) activity.findViewById(R.id.etLocationLong);
        i.putExtra("STARTTIMESTAMP", from.getText() != null ? from.getText().toString() : "");
        i.putExtra("ENDTIMESTAMP", to.getText() != null ? to.getText().toString() : "");
        i.putExtra("KEYWORDS", keywords.getText() != null ? keywords.getText().toString() : "");
        i.putExtra("LATITUDE", latitude.getText() != null ? latitude.getText().toString() : "");
        i.putExtra("LONGITUDE", longitude.getText() != null ? longitude.getText().toString() : "");
        activity.setResult(RESULT_OK, i);
        activity.finish();
    }

    @Override
    public void createCalendar() {
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd");
            Date now = calendar.getTime();
            String todayStr = new SimpleDateFormat("yyyy‐MM‐dd", Locale.getDefault()).format(now);
            Date today = format.parse((String) todayStr);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrowStr = new SimpleDateFormat("yyyy‐MM‐dd", Locale.getDefault()).format( calendar.getTime());
            Date tomorrow = format.parse((String) tomorrowStr);
            ((EditText) activity.findViewById(R.id.etFromDateTime)).setText(new SimpleDateFormat(
                    "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(today));
            ((EditText) activity.findViewById(R.id.etToDateTime)).setText(new SimpleDateFormat(
                    "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(tomorrow));
        } catch (Exception ex) { }
    }


}
