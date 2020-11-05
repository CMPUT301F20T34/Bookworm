package com.example.bookworm;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookworm.util.Util;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Test class for ViewContactInfo. All the UI tests for ViewContactInfo are written here.
 * Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class ViewContactInfoTest {

    private Solo solo; // Test class for robotium

    @Rule
    public ActivityTestRule<ViewContactInfoActivity> rule =
            new ActivityTestRule<>(ViewContactInfoActivity.class, true, true);

    /**
     * Setup for all tests
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Activity activity = rule.getActivity();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
    }

    /**
     * Gets the activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests for relevant contact info for a user based on a specific username
     * Relevant field's are checked to display correct info
     */
    @Test
    public void correctDisplayInfo() {

        String username = "myUserName123";

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ViewContactInfoActivity.class);
        intent.putExtra("username", username);
        activity.startActivity(intent);

        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", ViewContactInfoActivity.class);

        assertTrue(solo.waitForText("123 " + username + " Ave", 1, 2000));
        assertTrue(solo.waitForText("123-456-7890"));
    }

    /**
     * Closes the activity after each test.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
