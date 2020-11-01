package com.example.bookworm;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookworm.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for MainActivity. All the UI tests are written here.
 * Robotium test framework is used
 */
public class SignUpActivityTest {
    private Solo solo; // Main test class of robotium
    private FirebaseAuth fAuth;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance,
     * Signs out of firebase and goes to the SignUp activity
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
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
     * Ensures a username that already exists is not
     * accepted as a new registration.
     */
    @Test
    public void userExists() {
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
        String pass = Util.getRandomString(20);

        // Ensure there is no error to start
        EditText passView = (EditText) solo.getView(R.id.password1_signup);
        assertTrue(TextUtils.isEmpty(passView.getError()));

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(20));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass+"1"); // Ensure passwords don't match
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomEmail(30));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Attempt to register
        solo.clickOnView(solo.getView(R.id.register_button));

        // Get new state of editView, check that error is not empty
        passView = (EditText) solo.getView(R.id.password1_signup);
        assertFalse(TextUtils.isEmpty(passView.getError()));
    }

    /**
     * Ensures an incorrectly formatted email is not accepted.
     */
    @Test
    public void badEmail() {
        // Somehow get to the correct activity
        String pass = Util.getRandomString(20);

        EditText emailView = (EditText) solo.getView(R.id.email_address_signup);
        assertTrue(TextUtils.isEmpty(emailView.getError()));

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber());

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));

        assertFalse(TextUtils.isEmpty(emailView.getError()));
    }

    /**
     * Ensures a phone number that isn't correctly formatted isn't accepted.
     * Note: I don't know how android handles phone numbers
     */
    @Test
    public void badPhoneNumber() {
        // Somehow get to the correct activity
        String pass = Util.getRandomString(20);

        // Confirm error is not present
        EditText phoneView = (EditText) solo.getView(R.id.phone_number_signup);
        assertTrue(TextUtils.isEmpty(phoneView.getError()));

        // Enter random values
        solo.enterText((EditText) solo.getView(R.id.username_signup), Util.getRandomString(50));
        solo.enterText((EditText) solo.getView(R.id.password1_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.password2_signup), pass);
        solo.enterText((EditText) solo.getView(R.id.email_address_signup), Util.getRandomEmail(50));
        solo.enterText((EditText) solo.getView(R.id.phone_number_signup), Util.getRandomPhoneNumber() + "d");

        // Confirm the login
        solo.clickOnView(solo.getView(R.id.register_button));

        // Check to make sure the number is rejected
        assertFalse(TextUtils.isEmpty(phoneView.getError()));
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
