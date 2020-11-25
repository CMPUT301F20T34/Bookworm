package com.example.bookworm;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BookListFilterTest {
    private Solo solo;
    private FirebaseAuth fAuth;

    //create four books with different status for testing
    public Book mockAvailableBook() {
        return new Book("AvailableBook", "author", "description", "111", "available");
    }
    public Book mockRequestedBook() {
        return new Book("RequestedBook", "author", "description", "222", "requested");
    }
    public Book mockAcceptedBook() {
        return new Book("AcceptedBook", "author", "description", "333", "accepted");
    }
    public Book mockBorrowedBook() {
        return new Book("BorrowedBook", "author", "description", "444", "borrowed");
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance,
     * Signs out of firebase and goes to the SignUp activity

     */
    @Before
    public void setUp()  {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //perform login
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "peisong");
        solo.enterText((EditText) solo.getView(R.id.password_login), "12345678");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }

    /**
     * Tests radio button filters in OwnerBooklistActivity
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void testButtons() throws InterruptedException {
        //create mocks
        Book availableBook = mockAvailableBook();
        Book requestedBook = mockRequestedBook();
        Book acceptedBook = mockAcceptedBook();
        Book borrowedBook = mockBorrowedBook();
        availableBook.setOwnerId(fAuth.getCurrentUser().getUid());
        //write them into the user's book list
        Database.writeBookSynchronous(availableBook);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        requestedBook.setOwnerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(requestedBook);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        acceptedBook.setOwnerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(acceptedBook);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        borrowedBook.setOwnerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(borrowedBook);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }

        //go to the book list
        solo.clickOnView(solo.getView(R.id.booklist_button));
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);

        //there are four books created, so a scroll should be performed if the default radio button is pressed
        solo.clickOnView(solo.getView(R.id.radio_default));
        assertTrue(solo.waitForText("AcceptedBook", 0, 1000,true));

        //the requested book is the only book shown when the request radio button is pressed
        solo.clickOnView(solo.getView(R.id.radio_requested));
        assertTrue(solo.waitForText("RequestedBook", 0, 1000));
        Database.deleteBookSynchronous(requestedBook);

        //the accepted book is the only book shown when the accepted radio button is pressed
        solo.clickOnView(solo.getView(R.id.radio_accepted));
        assertTrue(solo.waitForText("AcceptedBook", 0, 1000));
        Database.deleteBookSynchronous(acceptedBook);

        //the borrowed book is the only book shown when the borrowed radio button is pressed
        solo.clickOnView(solo.getView(R.id.radio_borrowed));
        assertTrue(solo.waitForText("BorrowedBook", 0, 1000));
        Database.deleteBookSynchronous(borrowedBook);

        //when the available radio button is pressed, the AvailableBook should be detected
        solo.clickOnView(solo.getView(R.id.radio_available));
        assertTrue(solo.waitForText("AvailableBook", 0, 1000));
        Database.deleteBookSynchronous(availableBook);

    }

    /**
     * Closes the activity after each test.
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
