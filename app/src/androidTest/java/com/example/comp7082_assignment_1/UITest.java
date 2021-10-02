package com.example.comp7082_assignment_1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void searchGalleryByTimeTest() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(),typeText("20210915000000"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(replaceText("2021‐10-01 00:00:00"));

        onView(withId(R.id.etToDateTime)).perform(clearText(), typeText("20210917000000"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(replaceText("2021‐10-02 00:00:00"));

        onView(withId(R.id.etKeywords)).perform(typeText("Coffee"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("Coffee")));

        onView(withId(R.id.btnPrev)).perform(click());
        onView(withId(R.id.btnNext)).perform(click());
    }

    @Test
    public void searchGalleryByLocationTest() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(clearText());

        onView(withId(R.id.etLocationLat)).perform(clearText(), typeText("37.422"), closeSoftKeyboard());
        onView(withId(R.id.etLocationLong)).perform(clearText(), typeText("-122.084"), closeSoftKeyboard());

        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.tvLat)).check(matches(withText("37.422")));
        onView(withId(R.id.tvLong)).check(matches(withText("-122.084")));
    }

    @Test
    public void shareButton() {
        onView(withId(R.id.shareButton)).perform(click());
        onView(withId(R.id.shareBtn)).perform(click());
    }
}
