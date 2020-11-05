package com.example.bookworm;

import android.app.Activity;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Intent tests for OwnerBookListActivity, AddBookActivity, and EditBookActivity.
 *
 * User Requirements covered:
 *   US 01.04.01 - Add List-like Display for books
 *   US 01.06.01 - Add Editing Functionality to Book Display
 *   US 01.07.01 - Add Deleting Functionality to Book Display
 *   US 08.01.01 - Add Photo to Book
 *   US 08.03.01 - View book photo
 *   US 08.02.01 - Owner Delete Book Photo
 *
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
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

//    @Test
//    public void testAddBookNoPhoto() {
//        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(1000);
//
//        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.keywordSearchBar), "Harry Potter");
//        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName2), "JK. Rowling");
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(1000);
//        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.editTextNumber), "1234567890");
//        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName4),
//                "fantasy magic classic");
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(3000);
//
//        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
//    }

    @Test
    public void testEditBookNoPhoto() {
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.sleep(1000);

        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.
    }

//    @Test
//    public void testAddBookWithPhoto() {
//        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(1000);
//
//        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.keywordSearchBar), "Harry Potter");
//        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName2), "JK. Rowling");
//        solo.clickOnButton("ADD PHOTO");
//        solo.sleep(10000);
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(1000);
//        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.editTextNumber), "1234567890");
//        solo.enterText((EditText) solo.getView(R.id.editTextTextPersonName4),
//                "fantasy magic classic");
//        solo.clickOnButton("ADD BOOK");
//        solo.sleep(10000);
//
//        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
//    }


}
