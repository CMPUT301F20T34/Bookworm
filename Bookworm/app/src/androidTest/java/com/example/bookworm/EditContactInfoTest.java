package com.example.bookworm;

import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.widget.EditText;

=======
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
>>>>>>> Fixed saving contact info and added my functionality to profile view.
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

<<<<<<< HEAD
import com.example.bookworm.util.Util;
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
>>>>>>> Fixed saving contact info and added my functionality to profile view.
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
public class EditContactInfoTest {

    private Solo solo; // Test class for robotium

    @Rule
    public ActivityTestRule<EditContactInfoActivity> rule =
            new ActivityTestRule<>(EditContactInfoActivity.class, true, true);

<<<<<<< HEAD
=======
    public User getMockUser(){
        return new User("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        //return new User("Daniel", "abc123", "daniel@hotmail.com", "587-999-1234");
    }

>>>>>>> Fixed saving contact info and added my functionality to profile view.
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
<<<<<<< HEAD
    @Test
    public void correctDisplayInfo() {

        String username = "myUserName123";

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", username);
=======

    public void correctDisplayInfo() {

        User testUser = getMockUser();

        Database.updateUser(testUser);

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", testUser.getUsername());
>>>>>>> Fixed saving contact info and added my functionality to profile view.
        activity.startActivity(intent);

        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", EditContactInfoActivity.class);

<<<<<<< HEAD
        assertTrue(solo.waitForText("123 " + username + " Ave", 1, 2000));
        assertTrue(solo.waitForText("123-456-7890"));
=======
        assertTrue(solo.waitForText(testUser.getEmail(), 1, 15000));
        assertTrue(solo.waitForText(testUser.getPhone(), 1, 15000));
>>>>>>> Fixed saving contact info and added my functionality to profile view.
    }

    /**
     * Tests for save functionality when the corresponding button is pressed
     */
    @Test
    public void saveButton() {
<<<<<<< HEAD
        String username = "mySavedUserName123";

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", username);
=======
        User testUser = getMockUser();

        Database.updateUser(testUser);

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", testUser.getUsername());
>>>>>>> Fixed saving contact info and added my functionality to profile view.
        activity.startActivity(intent);

        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", EditContactInfoActivity.class);

<<<<<<< HEAD
        assertTrue(solo.waitForText("123 " + username + " Ave", 1, 2000));
        assertTrue(solo.waitForText("123-456-7890"));

        solo.clearEditText((EditText) solo.getView(R.id.editAddress));
        solo.enterText((EditText) solo.getView(R.id.editAddress), "12345 86th Ave");
        assertTrue(solo.waitForText("12345 86th Ave"));

        solo.clearEditText((EditText) solo.getView(R.id.editPhoneNumber));
        solo.enterText((EditText) solo.getView(R.id.editPhoneNumber), "098-765-4321");
        assertTrue(solo.waitForText("098-765-4321"));

        solo.clickOnView(solo.getView(R.id.saveChangesButton));

        assertTrue(solo.waitForText("Contact info saved for user:"));
        assertTrue(solo.waitForText(username));
=======
        assertTrue(solo.waitForText(testUser.getEmail(), 1, 15000));
        assertTrue(solo.waitForText(testUser.getPhone(), 1, 15000));

        String newEmail = "mikehunter2@hotmail.com";
        String newPhoneNumber = "587-111-1234";

        EditText editPhoneNumberView = (EditText) solo.getView(R.id.editPhoneNumber);
        solo.clearEditText(editPhoneNumberView);
        solo.enterText(editPhoneNumberView, newPhoneNumber);
        assertTrue(solo.waitForText(newPhoneNumber));

        EditText editEmailView = (EditText) solo.getView(R.id.editEmail);
        solo.clearEditText(editEmailView);
        solo.enterText(editEmailView, newEmail);
        assertTrue(solo.waitForText(newEmail));

        solo.clickOnView(solo.getView(R.id.saveChangesButton));

        /*while(Database.getListenerSignal() == 0){
            try{
                Thread.sleep(100);
            }catch (Exception e){}
        }*/

        Log.d("EditContactInfo Test", "Starting database check for save");

        Task<DocumentSnapshot> getuserTask = Database.getUser(testUser.getUsername());
        getuserTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("EditContactInfo Test", "Database info: " + task.getResult().get("phoneNumber").toString() + ".");
                    assertTrue(task.getResult().get("phoneNumber").toString() == newPhoneNumber);
                    assertTrue(task.getResult().get("email").toString() == newEmail);
                }
            }});

        while(!getuserTask.isComplete()){
            try {
                Thread.sleep(100);
            }catch (Exception e){}
        }
>>>>>>> Fixed saving contact info and added my functionality to profile view.
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
