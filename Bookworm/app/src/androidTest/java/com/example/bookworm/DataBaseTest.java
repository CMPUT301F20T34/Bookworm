package com.example.bookworm;

import android.app.Activity;
import android.provider.ContactsContract;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookworm.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for MainActivity. All the UI tests are written here.
 * Robotium test framework is used
 */
public class DataBaseTest {
    private Solo solo; // Main test class of robotium

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
     * Tests the writing of books by the writeBook method
     */
    @Test
    public void databaseTest() throws InterruptedException {
        //Writes a new book to the database
        Book testBook = new Book("1984", "George Orwell", "1621325");
        int[] callback = new int[]{0};
        Database.writeBook(testBook, callback);
        while (callback[0] == 0){
            Thread.sleep(100);
        }
        assertEquals(callback[0],1);

        Thread.sleep(5000);
        //Updates a book in the database
        testBook.setTitle("Animal Farm");
        callback[0] = 0;
        Database.writeBook(testBook, callback);
        while (callback[0] == 0){
            Thread.sleep(100);
        }
        assertEquals(callback[0],1);

        Thread.sleep(5000);
        //Tests for a correct response with a single field
        Database.queryCollection("books", new String[]{"title"}, new String[]{"Animal Farm"})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        assertEquals(docs.get(0).get("title"), "Animal Farm");
                    }
                });
        //Tests for a correct response with multiple fields
        Database.queryCollection("books", new String[]{"title", "author"}, new String[]{"Animal Farm", "George Orwell"})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        assertEquals(docs.get(0).get("title"), "Animal Farm");
                    }
                });
        //Tests for querying for a book that doesn't exist
        Database.queryCollection("books", new String[]{"title"}, new String[]{"1984"})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        assertEquals(docs.size(), 0);
                    }
                });

        //Tests the deletion of books
        Thread.sleep(5000);
        callback[0] = 0;
        Database.deleteBook(testBook, callback);
        while (callback[0] == 0){
            Thread.sleep(100);
        }
        assertEquals(callback[0],1);

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
