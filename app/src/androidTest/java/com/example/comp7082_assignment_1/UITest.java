package com.example.comp7082_assignment_1;

import static androidx.test.espresso.Espresso.onView;
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
        onView(withId(R.id.etFromDateTime)).perform(typeText("2021"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(replaceText("2021‐09‐15 00:00:00"));

        onView(withId(R.id.etToDateTime)).perform(typeText("2021"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(replaceText("2021‐09‐17 00:00:00"));

        onView(withId(R.id.etKeywords)).perform(typeText("test picture 162754 B New mod"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.etCaption)).check(matches(withText("test picture 162754 B New mod")));
        onView(withId(R.id.btnNext)).perform(click());
        onView(withId(R.id.btnPrev)).perform(click());
    }
}
