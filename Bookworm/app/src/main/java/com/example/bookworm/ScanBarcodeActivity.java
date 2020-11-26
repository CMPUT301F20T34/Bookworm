package com.example.bookworm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class ScanBarcodeActivity extends AppCompatActivity {

    String TAG = "Sample";
    private Button handoverButton;
    private Button retrieveButton;
    private Button borrowButton;
    private Button returnButton;
    private static int SCAN_MODE_CODE;
    private static boolean TEST_MODE;
    private static String TEST_ISBN;
    private static String TEST_USERNAME;
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_prescreen);

        if(getIntent().getExtras() != null){
            TEST_MODE = getIntent().getBooleanExtra("test", false);
            if(TEST_MODE){
                TEST_ISBN = getIntent().getStringExtra("test_isbn");
                TEST_USERNAME = getIntent().getStringExtra("test_username");
            }
        }

        SCAN_MODE_CODE = -1;

        fAuth = FirebaseAuth.getInstance();

        handoverButton = findViewById(R.id.handover_button);
        handoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processHandOver(TEST_ISBN, TEST_USERNAME);
                }else {
                    SCAN_MODE_CODE = 1;
                    new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
                }
            }
        });

        retrieveButton = findViewById(R.id.retrieve_button);
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processRetrieve(TEST_ISBN, TEST_USERNAME);
                }else {
                    SCAN_MODE_CODE = 2;
                    new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
                }
            }
        });

        borrowButton = findViewById(R.id.borrow_button);
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processBorrow(TEST_ISBN, TEST_USERNAME);
                }else {
                    SCAN_MODE_CODE = 3;
                    new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
                }
            }
        });

        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processReturn(TEST_ISBN, TEST_USERNAME);
                }else {
                    SCAN_MODE_CODE = 4;
                    new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
                }
            }
        });

    }

    /**
     * Processes a scan for handover given a specified ISBN
     * @param isbn
     */
    protected void processHandOver(String isbn, String currentUsername){
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                            Log.d(TAG, "scannedBook Owner: " + scannedBook.getOwner());
                            if (scannedBook.getOwner().equals(currentUsername)) {
                                Intent requestActivity = new Intent(ScanBarcodeActivity.this, AcceptRequestActivity.class);
                                requestActivity.putExtra("isbn", isbn);
                                startActivity(requestActivity);
                            } else {
                                Toast.makeText(ScanBarcodeActivity.this, "Error: You do not own this book.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Processes a scan for retrieve given a specified ISBN
     * @param isbn
     */
    protected void processRetrieve(String isbn, String currentUsername){
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);

                            if (scannedBook.getOwner().equals(currentUsername))  {
                                scannedBook.setStatus("available");
                                Database.writeBook(scannedBook);
                                while (Database.getListenerSignal() == 0){
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (Database.getListenerSignal() == 1) {
                                    Toast.makeText(ScanBarcodeActivity.this, scannedBook.getTitle() + " has been marked as available.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(ScanBarcodeActivity.this, "Error: Failed to mark book as available.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ScanBarcodeActivity.this, "Error: You do not own this book.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Processes a scan for borrow given a specified ISBN
     * @param isbn
     */
    protected void processBorrow(String isbn, String currentUsername){
        //Borrow a book from an owner
        Log.d(TAG, "Borrowing book.");
        //check that request has been marked as accepted
        //add book to borrowers book list and mark it as borrowed
        Database.queryCollection("requests", new String[]{"book.isbn", "creator.username"}, new String[]{isbn, currentUsername})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            Toast.makeText(ScanBarcodeActivity.this, "Error: No request has been made on book.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Request userRequest = queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class);
                            if (userRequest.getStatus().equals("accepted")) {

                                Database.getUser(currentUsername)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                User currentUser = documentSnapshot.toObject(User.class);

                                                Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                if (queryDocumentSnapshots.getDocuments().size() == 0){
                                                                    Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                                                                }
                                                                else {
                                                                    Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                                                                    scannedBook.setStatus("borrowed");
                                                                    scannedBook.setBorrower(currentUsername);
                                                                    Database.writeBook(scannedBook);
                                                                    Toast.makeText(ScanBarcodeActivity.this, scannedBook.getTitle() + " has been added to your collection.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(ScanBarcodeActivity.this, "Error: Request has not been accepted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Processes a scan for return given a specified ISBN
     * @param isbn
     */
    protected void processReturn(String isbn, String currentUsername){
        //Return a book to an owner
        Log.d(TAG, "Returning book.");
        //remove book from borrowers book list
        Database.getUser(currentUsername)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User currentUser = documentSnapshot.toObject(User.class);

                        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                                            if (scannedBook.getBorrower() == null || scannedBook.getBorrower().equals(currentUsername)){
                                                Toast.makeText(ScanBarcodeActivity.this, "Error: " + scannedBook.getTitle() + " is not currently borrowed by you.", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                scannedBook.setBorrower(null);
                                                Database.writeBook(scannedBook);
                                                Toast.makeText(ScanBarcodeActivity.this, scannedBook.getTitle() + " has been returned.", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScanBarcodeActivity.this, "Error: Issue with database query, try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }


    /**
     * Gets the results of a scan and uses the data depending on what mode has been chosen
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String isbn = result.getContents();
            if(isbn == null) {
                Toast.makeText(this, "Scan failed, please try again.", Toast.LENGTH_LONG).show();
            } else {
                String currentEmail = fAuth.getCurrentUser().getEmail();
                Database.getUserFromEmail(currentEmail)
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d(TAG, "querySnapshot: " + queryDocumentSnapshots.getDocuments().toString());
                                String currentUsername = queryDocumentSnapshots.getDocuments().get(0).getId();
                                Log.d(TAG, "Current email: " + currentEmail);
                                Log.d(TAG, "Current username: " + currentUsername);

                                if (SCAN_MODE_CODE == 1){
                                    processHandOver(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 2){
                                    processRetrieve(isbn, currentUsername);

                                } else if (SCAN_MODE_CODE == 3){
                                    processBorrow(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 4){
                                    processReturn(isbn, currentUsername);
                                }
                                else{
                                    Log.d(TAG, "No matching code");
                                }
                                SCAN_MODE_CODE = -1;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ScanBarcodeActivity.this, "Error: Could not retrieve current user.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
