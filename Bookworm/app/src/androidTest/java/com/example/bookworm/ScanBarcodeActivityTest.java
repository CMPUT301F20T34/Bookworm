package com.example.bookworm;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ScanBarcodeActivityTest {

    private Solo solo; // Test class for robotium

    @Rule
    public ActivityTestRule<ScanBarcodeActivity> rule =
            new ActivityTestRule<>(ScanBarcodeActivity.class, true, true);

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
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests scanning for a book handover
     * Checks database to ensure desired functionality
     */
    @Test
    public void testScanForHandover() {
        String testISBN = "987654321";
        String testUsername = "danielmerryweather";

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ScanBarcodeActivity.class);
        intent.putExtra("test", true);
        intent.putExtra("test_isbn", testISBN);
        intent.putExtra("test_username", testUsername);
        activity.startActivity(intent);

        solo.assertCurrentActivity("Wrong Activity", ScanBarcodeActivity.class);

        solo.clickOnView(solo.getView(R.id.handover_button));

        Database.queryCollection("requests", new String[]{"book.isbn"}, new String[]{testISBN})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            assertTrue(doc.toObject(Request.class).getBook().getIsbn().equals(testISBN));
                        }
                    }
                });
    }

    /**
     * Tests scanning for a book retrieve
     * Checks database to ensure desired functionality
     */
    @Test
    public void testScanForRetrieve() {
        String testISBN = "987654321";
        String testUsername = "danielmerryweather";

        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ScanBarcodeActivity.class);
        intent.putExtra("test", true);
        intent.putExtra("test_isbn", testISBN);
        intent.putExtra("test_username", testUsername);
        activity.startActivity(intent);

        solo.assertCurrentActivity("Wrong Activity", ScanBarcodeActivity.class);

        solo.clickOnView(solo.getView(R.id.retrieve_button));

        Database.queryCollection("books", new String[]{"isbn"}, new String[]{testISBN})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        Book tempBook = docs.get(0).toObject(Book.class);
                        assertTrue(tempBook.getStatus().equals("available"));
                    }
                });
    }

    /**
     * Tests scanning for a book handover
     * Checks database to ensure desired functionality
     */
    @Test
    public void testScanForBorrow() {
        String testUsername = "HarrisonBorrower";
        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ScanBarcodeActivity.class);
        intent.putExtra("test", true);
        intent.putExtra("test_isbn", "1234567");
        intent.putExtra("test_username", testUsername);
        activity.startActivity(intent);

        solo.assertCurrentActivity("Wrong Activity", ScanBarcodeActivity.class);

        solo.clickOnView(solo.getView(R.id.borrow_button));

        // Check database for correctness
    }

    /**
     * Tests scanning for a book return
     * Checks database to ensure desired functionality
     */
    @Test
    public void testScanForReturn() {
        String testISBN = "987654321";
        String testUsername = "HarrisonBorrower";
        Activity activity = rule.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), ScanBarcodeActivity.class);
        intent.putExtra("test", true);
        intent.putExtra("test_isbn", testISBN);
        intent.putExtra("test_username", testUsername);
        activity.startActivity(intent);

        solo.assertCurrentActivity("Wrong Activity", ScanBarcodeActivity.class);

        solo.clickOnView(solo.getView(R.id.return_button));

        Database.queryCollection("books", new String[]{"isbn"}, new String[]{testISBN})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        Book tempBook = docs.get(0).toObject(Book.class);
                        assertTrue(tempBook.getStatus().equals("available"));
                    }
                });
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


