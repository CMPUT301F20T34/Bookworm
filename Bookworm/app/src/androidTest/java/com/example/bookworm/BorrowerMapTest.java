package com.example.bookworm;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Intent tests for BorrowerMapActivity.
 *
 * User Requirements covered:
 *   US 09.02.01 - Map for Borrower
 *
 * Robotium test framework is used for this testing
 */
public class BorrowerMapTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<BorrowerMainActivity> rule =
            new ActivityTestRule<>(BorrowerMainActivity.class,
                    true, true);

    /**
     * Set up a mock accepted request on the database.
     */
    @Before
    public void setUpAcceptedRequest() {
        // create a mock book
        Book book = new Book("test owner", "test owner id");
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setIsbn("11223344556677889900");

        // upload the mock accepted request for the mock book
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Database.getUserFromEmail(email)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    Request request = new Request(book, doc.toObject(User.class), "available");
                    request.getCreator().setUsername(doc.getId());
                    Database.createRequest(request)
                            .addOnSuccessListener(e -> {
                                setUpHelper(doc.getId());
                            })
                            .addOnFailureListener(e -> {
                                fail("failed creating request");
                            });
                });

        // Initialize the solo instance on BorrowerMainActivity
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.sleep(3000);
    }

    /**
     * From BorrowerMainActivity, navigate to BorrowerMapActivity
     */
    @Test
    public void testMap() {
        solo.clickOnView(solo.getView(R.id.button_accepted));
        solo.assertCurrentActivity("Wrong Activity", ListAccepted.class);
        solo.clickInRecyclerView(0);
        solo.sleep(3000);
    }

    /**
     * Helper function for marking the uploaded request as accepted with a meeting location.
     * For the mock request, the meeting location is set between Alaska and Russia.
     * @param username current user name
     */
    private void setUpHelper(String username) {
        String isbn = "11223344556677889900";
        Database.getRequestsForBook(isbn)
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {

                    } else {
                        String id = isbn + "-" + username;
                        Map<String, Object> acc = new HashMap<>();
                        Map<String, Object> dec = new HashMap<>();
                        Map<String, Object> acc2 = new HashMap<>();
                        Map<String, Object> acc3 = new HashMap<>();
                        acc.put("status", "accepted");
                        acc2.put("lat", 65.34);
                        acc3.put("lng", -169.9);
                        dec.put("status", "declined");
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.getId().equals(id)) {
                                doc.getReference().set(acc, SetOptions.merge());
                                doc.getReference().set(acc2, SetOptions.merge());
                                doc.getReference().set(acc3, SetOptions.merge());
                            } else {
                                doc.getReference().set(dec, SetOptions.merge());
                            }
                        }
                    }
                }
            });
    }
}
