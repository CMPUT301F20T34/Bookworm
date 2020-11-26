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

public class BorrowerMainActivityTest {
    private Solo solo;
    private FirebaseAuth fAuth;

    public Book mockBook() {
        return new Book("Title", "author", "description", "isbn", "available");
    }
    private User mockUser() {
        //the mockUser use the same email address with the user used for tests
        return new User("peisong", "12345678", "peisong@ualberta.ca", "88888888");
    }
    private Request mockRequest() {
        return new Request(mockBook(), mockUser(), "Status");
    }
    private Request mockAcceptedRequest() {
        //return a request with status "Accepted"
        return new Request(mockBook(), mockUser(), "Accepted");
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance,
     * Signs out of firebase and goes to the SignUp activity
     */
    @Before
    public void setUp() {
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
     * Tests getBorrowingBook, getRequestedBooks, and getAcceptedBooks
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void testGetMethods() throws InterruptedException{
        Book book = mockBook();
        book.setBorrowerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(book);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        Request request = mockAcceptedRequest();
        Database.createSynchronousRequest(request);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        Database.getBorrowingBooks()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //convert the QuerySnapshot to the book object
                            Book book = doc.toObject(Book.class);
                            //check if the borrowerId of the book is equal to the current user's ID
                            assertEquals(book.getBorrowerId(),fAuth.getCurrentUser().getUid());
                        }
                    }
                });
        Database.getRequestedBooks()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //convert the QuerySnapshot to the book object
                            Request request = doc.toObject(Request.class);
                            //check if the creator-email of the requested book is equal to the current user's email
                            assertEquals(request.getCreator().getEmail(),fAuth.getCurrentUser().getEmail());
                        }
                    }
                });
        Database.getAcceptedBooks()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            //convert the QuerySnapshot to the book object
                            Request request = doc.toObject(Request.class);
                            //check if the status of the book is "Accepted"
                            assertEquals(request.getStatus(),"Accepted");
                        }
                    }
                });
    }

    /**
     * Tests the list of borrowing books
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void testBorrowing() throws InterruptedException {
        //add a book to the database and set its borrowerId to the current user's ID
        Book book = mockBook();
        book.setBorrowerId(fAuth.getCurrentUser().getUid());
        Database.writeBookSynchronous(book);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        //go to the BorrowerMainActivity, then go to the ListBorrowing activity
        solo.clickOnView(solo.getView(R.id.borrow_info_button));
        solo.assertCurrentActivity("Wrong Activity", BorrowerMainActivity.class);
        solo.clickOnView(solo.getView(R.id.button_borrowing));
        solo.assertCurrentActivity("Wrong Activity", ListBorrowing.class);
        //The book name should be found in this page
        assertTrue(solo.waitForText("Title", 0, 1000));
        //remove it from the database
        Database.deleteBookSynchronous(book);
    }

    /**
     * Tests the list of requested books
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void testRequested() throws InterruptedException {
        //create a request, and its creator-email is equal to the current user's email
        Request request = mockRequest();
        //add it to the database
        Database.createSynchronousRequest(request);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        solo.clickOnView(solo.getView(R.id.borrow_info_button));
        solo.assertCurrentActivity("Wrong Activity", BorrowerMainActivity.class);
        solo.clickOnView(solo.getView(R.id.button_requested));
        solo.assertCurrentActivity("Wrong Activity", ListRequested.class);
        //As there are originally four other books here, a scroll should be performed for reaching the target requested book
        assertTrue(solo.waitForText("Title", 0, 1000,true));
        Database.deleteRequest(request);
    }

    /**
     * Tests the list of accepted books
     * @throws InterruptedException Sleeping thread is interrupted
     */
    @Test
    public void testAccepted() throws InterruptedException {
        //create a request with status "Accepted"
        Request request = mockAcceptedRequest();
        Database.createSynchronousRequest(request);
        while (Database.getListenerSignal() == 0) {
            Thread.sleep(100);
        }
        solo.clickOnView(solo.getView(R.id.borrow_info_button));
        solo.assertCurrentActivity("Wrong Activity", BorrowerMainActivity.class);
        solo.clickOnView(solo.getView(R.id.button_accepted));
        solo.assertCurrentActivity("Wrong Activity", ListAccepted.class);
        assertTrue(solo.waitForText("Title", 0, 1000));
        Database.deleteRequest(request);
    }

    /**
     * Closes the activity after each test.
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

}
