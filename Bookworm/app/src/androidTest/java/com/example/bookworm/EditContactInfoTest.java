package com.example.bookworm;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public User getMockUser(){
        return new User("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
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

        User testUser = getMockUser();

        Database.updateUser(testUser);

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", testUser.getUsername());
        activity.startActivity(intent);

        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", EditContactInfoActivity.class);

        assertTrue(solo.waitForText(testUser.getEmail(), 1, 15000));
        assertTrue(solo.waitForText(testUser.getPhone(), 1, 15000));
    }

    /**
     * Tests for save functionality when the corresponding button is pressed
     */
    @Test
    public void saveButton() {
        User testUser = getMockUser();

        Database.updateUser(testUser);

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), EditContactInfoActivity.class);
        intent.putExtra("username", testUser.getUsername());
        activity.startActivity(intent);

        // Somehow get to the correct activity
        solo.assertCurrentActivity("Wrong Activity", EditContactInfoActivity.class);

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

        while(Database.getListenerSignal() == 0){
            try{
                Thread.sleep(1000);
            }catch (Exception e){}
        }

        Task<DocumentSnapshot> getuserTask = Database.getUser(testUser.getUsername());
        getuserTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    assertTrue(new String(newPhoneNumber).equals(task.getResult().get("phoneNumber").toString()));
                    assertTrue(new String(newEmail).equals(task.getResult().get("email").toString()));
                }
            }});

        while(!getuserTask.isComplete()){
            try {
                Thread.sleep(100);
            }catch (Exception e){}
        }
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
