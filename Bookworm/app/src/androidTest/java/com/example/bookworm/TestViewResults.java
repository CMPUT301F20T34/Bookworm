package com.example.bookworm;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestViewResults {
    private Solo solo;
    private FirebaseAuth fAuth;
    private String title = "hxujc";

    private void makeRequest() {
        // Enter the title of the book to search
        solo.enterText((EditText) solo.getView(R.id.keywordSearchBar), title);
        solo.clickOnView(solo.getView(R.id.search_button));

        // Ensure that we went to the search activity
        solo.assertCurrentActivity("Wrong Activity", SearchResultsActivity.class);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", ViewBookActivity.class);
        solo.clickOnView(solo.getView(R.id.view_book_request));
        solo.assertCurrentActivity("Wrong Activity", SearchResultsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

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
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "pahasa");
        solo.enterText((EditText) solo.getView(R.id.password_login), "abcdefg");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Tests that a request appears in the search results, and clicking on the request
     * in the results displays the information for the user that made the request.
     */
    @Test
    public void testInSearchResults() {
        makeRequest();

        // Go to the book where the requests were made
        solo.clickOnView(solo.getView(R.id.booklist_button));
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnText(title);

        // Go to the list of requests for the book
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.clickOnView(solo.getView(R.id.button10));
        solo.assertCurrentActivity("Wrong Activity", ViewRequestsActivity.class);

        // Click on the request that was just made
        solo.waitForText("pahasa", 1, 1000);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", AcceptDeclineRequestActivity.class);

        // Check that it fetches account of person that made request
        solo.waitForText("pahasa", 1, 1000);
        solo.waitForText("psaunder@ualberta.ca", 1, 1000);
        solo.waitForText("7802467244", 1, 1000);
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
