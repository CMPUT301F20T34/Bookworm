package com.example.bookworm;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookworm.util.Util;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MainActivity. All the UI tests are written here.
 * Robotium test framework is used
 */
public class SignUpActivityTest {
    private Solo solo; // Main test class of robotium

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
     * Tests for a successful user registration
     * (all parameters satisfied)
     */
    @Test
    public void goodUser() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        String pass = Util.getRandomString(20);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomEmail(30));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));
        /* HOW DO I ENTER A GOOD REGISTRATION FORM WITHOUT CHANGING THE DB */
    }

    /**
     * Ensures a username that already exists is not
     * accepted as a new registration.
     */
    @Test
    public void userExists() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        String pass = Util.getRandomString(20);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), "user");
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomEmail(30));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));
        // Somehow get that the db has rejected
    }

    /**
     * Ensures that a registration with non-matching passwords is
     * rejected by the database.
     */
    @Test
    public void passwordsDontMatch() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        String pass = Util.getRandomString(20);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), "user");
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass+"1"); // Ensure passwords don't match
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomEmail(30));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));
        // Somehow get that the db has rejected
    }

    /**
     * Ensures an incorrectly formatted email is not accepted.
     */
    @Test
    public void badEmail() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        String pass = Util.getRandomString(20);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));

        // Somehow get that the db has rejected the values
    }

    /**
     * Ensures a phone number that isn't correctly formatted isn't accepted.
     * Note: I don't know how android handles phone numbers
     */
    @Test
    public void badPhoneNumber() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        String pass = Util.getRandomString(20);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber() + "d");

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));

        // Somehow get that the db has rejected the values
    }

    /**
     * Ensures that a user that doesn't exist entered with a password
     * that doesn't exist is rejected by the DB
     */
    @Test
    public void wrongUsernameAndPassword() {
        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_login), Util.getRandomString(100));
        solo.enterText((EditText) solo.getView(R.id.password_login), Util.getRandomString(100));

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));

        // Somehow get that the db has rejected the values
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
