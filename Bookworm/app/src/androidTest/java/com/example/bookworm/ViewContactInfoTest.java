package com.example.bookworm;

import android.app.Activity;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
     * Gives a mock user to be used in the tests
     * @return User mockUser
     */
    public User getMockUser(){
        return new User("danieltest", "testpass", "danieltesting@hotmail.com", "587-555-4321");
        //return new User("Daniel", "abc123", "daniel@hotmail.com", "587-999-1234");
    }

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
        User testUser = getMockUser();

        Database.updateUser(testUser);

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ViewContactInfoActivity.class);
        intent.putExtra("username", testUser.getUsername());
        activity.startActivity(intent);

        solo.assertCurrentActivity("Wrong Activity", ViewContactInfoActivity.class);

        assertTrue(solo.waitForText(testUser.getEmail(), 1, 15000));
        assertTrue(solo.waitForText(testUser.getPhone(), 1, 15000));
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
