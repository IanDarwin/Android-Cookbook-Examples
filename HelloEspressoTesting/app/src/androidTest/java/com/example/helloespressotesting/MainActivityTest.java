package com.example.helloespressotesting;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Demonstrate use of Espresso testing.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void changeText_sameActivity() {
        final String MESG = "Hello Down There!";

        // Simulate typing text into 'tf'
        onView(withId(R.id.tf))
                .perform(typeText(MESG));
        closeSoftKeyboard();

        // Simulate clicking the Button 'startButton'
        onView(withId(R.id.startButton)).perform(click());

        // Find the target, and check that the text was changed.
        onView(withId(R.id.tvTarget))
                .check(matches(withText(MESG)));
    }
}
