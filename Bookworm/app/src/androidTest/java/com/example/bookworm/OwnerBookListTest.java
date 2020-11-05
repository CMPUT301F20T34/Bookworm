package com.example.bookworm;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Intent tests for OwnerBookListActivity, AddBookActivity, and EditBookActivity.
 *
 * User Requirements covered:
 *   US 01.04.01 - Add List-like Display for books
 *   US 01.06.01 - Add Editing Functionality to Book Display
 *   US 01.07.01 - Add Deleting Functionality to Book Display
 *
 * Robotium test framework is used for this testing
 */
public class OwnerBookListTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<OwnerBooklistActivity> rule =
            new ActivityTestRule<>(OwnerBooklistActivity.class,
                    true, true);

    /**
     * Add a book to the list before running testEditBookNoPhoto
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        // click on the "ADD BOOK" button from OwnerBooklistActivity
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnButton("ADD BOOK");
        solo.sleep(1000);

        // Enter title and author in the edittexts
        // click on "ADD" button, which should return a toast message since isbn is required
        // enter isbn and description in the edittexts
        // click on "ADD" button again
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        solo.enterText((EditText) solo.getView(R.id.keywordSearchBar), "Harry Potter");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName2), "JK. Rowling");
        solo.clickOnView(solo.getView(R.id.button6));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editTextNumber), "1234567890");
        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName4),
                "fantasy magic classic");
        solo.clickOnView(solo.getView(R.id.button6));
        solo.sleep(5000);

        // The book entry is shown in OwnerBooklistActivity
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
    }

    /**
     * edit the first book in the list
     */
    @Test
    public void testEditBookNoPhoto() {
        // click the first book in OwnerBookListActivity
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.sleep(1000);

        // Check that the edittext is pre-filled with the title "Harry Potter"
        // Change the title to "Harry Potter - Philosopher's Stone"
        // click "SAVE CHANGES" button
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        EditText titleEditText = (EditText) solo.getView(R.id.editTextTextPersonName);
        assertEquals("Harry Potter", titleEditText.getText().toString());
        solo.clearEditText(titleEditText);
        solo.clickOnButton("SAVE CHANGES");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.enterText(titleEditText, "Harry Potter - Philosopher's Stone");
        solo.clickOnButton("SAVE CHANGES");
        solo.sleep(5000);

        // The title of the first book in the list should be "Harry Potter - Philosopher's Stone"
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
    }

    /**
     * delete the first book from the list after testEditBookNoPhoto
     */
    @After
    public void testDeleteBookNoPhoto() {
        // click the first book in OwnerBookListActivity
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.sleep(1000);

        // click "DELETE" button
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.clickOnView(solo.getView(R.id.button11));
        solo.sleep(5000);

        // The booklist should now be empty
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
    }
}
