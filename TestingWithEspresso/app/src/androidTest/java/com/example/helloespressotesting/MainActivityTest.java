package com.example.helloespressotesting;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demonstrate use of Espresso testing to verify
 * that GUi controls do what they should.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // Tells Espresso to create the Activity before each test.
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void changeText_sameActivity() {
        final String MESG = "Hello Down There!";

        // Simulate typing text into 'tf'
        onView(withId(R.id.tf))
                .perform(typeText(MESG));
        closeSoftKeyboard();

        // Simulate clicking the Button 'startButton'
        onView(withId(R.id.startButton)).perform(click());

        // Find the target, which is supposed to get the text from "tf"
        // when "Start" is pressed, and check that the text was changed.
        onView(withId(R.id.tvTarget))
                .check(matches(withText(MESG)));
    }
}
