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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class OwnerMapTest {
    private Solo solo;
    private FirebaseAuth fAuth;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    private User MockUser() {
        return new User("Steve", "1234567", "steve@gmail.com", "1234567890");
    }

    public Book MockBook() {
        return new Book("Title", "author", "description", "isbn", "available");
    }

    private Request MockRequest() {
        return new Request(MockBook(), MockUser(), "available");
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
        //perform login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "muligan");
        solo.enterText((EditText) solo.getView(R.id.password_login), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    /**
     * Tests the functionality of the owner map
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void OwnerMapTest() throws InterruptedException {
        //LatLng edmonton = new LatLng(53.52, -113.52);
        Book book = MockBook();
        book.setOwnerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(book);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        Request request = MockRequest();
        Database.createSynchronousRequest(request);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        clickOnRequest();
        //int views = solo.getCurrentViews().size();
        solo.clickOnView(solo.getView(R.id.accept_decline_request_accept_button));
        solo.assertCurrentActivity("Wrong Activity", OwnerMapActivity.class);
        TextView info = (TextView) solo.getView(R.id.location_info);
        String LocInfo = info.getText().toString();
        solo.clickOnButton("CONFIRM");
        solo.goBack();

        Database.getAcceptedRequest()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //convert the QuerySnapshot to the book object
                            Request request = doc.toObject(Request.class);
                            //check if the status of the book is "Accepted"
                            assertEquals(53.52,request.getLat(),0);
                            assertEquals(-113.52,request.getLng(),0);
                        }
                    }
                });

       Database.deleteBookSynchronous(MockBook());
       Database.deleteRequest(MockRequest());
    }

    /**
     * Handles all actions related to viewing and clicking on
     * the request previously made.
     */
    private void clickOnRequest() {
        // Go to the book where the requests were made
        solo.clickOnView(solo.getView(R.id.booklist_button));
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnText(MockBook().getTitle());

        // Go to the list of requests for the book
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.clickOnView(solo.getView(R.id.button10));
        solo.assertCurrentActivity("Wrong Activity", ViewRequestsActivity.class);

        // Click on the request that was just made
        assertTrue(solo.waitForText(MockUser().getUsername(), 1, 1000));
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", AcceptDeclineRequestActivity.class);

//        // Check that it fetches account of person that made request
//        assertTrue(solo.waitForText("pahasa", 1, 1000));
//        assertTrue(solo.waitForText("psaunder@ualberta.ca", 1, 1000));
//        assertTrue(solo.waitForText("7802467244", 1, 1000));
    }

    /**
     * Closes the activity after each test.
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}