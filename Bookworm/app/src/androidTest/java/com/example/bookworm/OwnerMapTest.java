package com.example.bookworm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OwnerMapTest {
    private Solo solo;
    private FirebaseAuth fAuth;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    public Book mockBook() {
        return new Book("Title", "author", "description", "isbn", "accepted");
    }

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
        activity.startActivity(intent);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "muligan");
        solo.enterText((EditText) solo.getView(R.id.password_login), "123456");
        solo.clickOnButton("LOGIN");
        Intent intent2 = new Intent(activity.getApplicationContext(), OwnerMapActivity.class);
        activity.startActivity(intent2);
        solo.assertCurrentActivity("Wrong Activity", OwnerMapActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.username_login), "muligan");
//        solo.enterText((EditText) solo.getView(R.id.password_login), "123456");
//        solo.clickOnButton("LOGIN");
//        //solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("MY BOOKLIST");
//        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void MapTest() throws InterruptedException {
        LatLng edmonton = new LatLng(53.52, -113.52);
        Book book = mockBook();
        book.setOwnerId(fAuth.getCurrentUser().getUid());
        Database.writeBook(book);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }

        solo.clickOnButton("CONFIRM");
        assertEquals(edmonton.latitude, book.getLocation().latitude, 0.0);
        assertEquals(edmonton.longitude, book.getLocation().longitude, 0.0);
    }

    /**
     * Closes the activity after each test.
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}