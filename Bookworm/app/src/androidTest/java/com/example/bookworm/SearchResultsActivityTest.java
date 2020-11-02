package com.example.bookworm;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;


public class SearchResultsActivityTest {
    private Solo solo; // Main test class of robotium
    private FirebaseAuth fAuth;

    public Book mockBook() {
        return new Book("Title", "Author", "Description", "ISBN", "Status");
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule =
        new ActivityTestRule<>(LoginActivity.class, true, true);

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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "psaunder@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password_login), "abcdefg");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

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

    @Test
    public void testSearchResult() throws InterruptedException {
        // Ensure we are logged in
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Write an arbitrary book to the database
        Book book = mockBook();
        ArrayList<Integer> returnVals = new ArrayList<>();
        returnVals.add(0);
        Database.writeBook(book, returnVals);
        while (returnVals.get(0) == 0) {
            Thread.sleep(100);
        }
        returnVals.set(0, 0);

        // Enter the title of the book to search
        solo.enterText((EditText) solo.getView(R.id.keywordSearchBar), "Happy Lands");
        solo.clickOnView(solo.getView(R.id.search_button));

        // Ensure that we went to the search activity
        solo.assertCurrentActivity("Wrong Activity", SearchResultsActivity.class);

        // Ensure that there is at least one result
        assertTrue(solo.waitForText("Happy Lands", 2, 1000));

        // Delete the book
        Database.deleteBook(book, returnVals);
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
