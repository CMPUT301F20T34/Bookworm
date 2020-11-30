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
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

/**
 * Handles the barcode scanning for owners and borrowers that want to handover/retrieve books.
 *
 * Scanning is done using https://github.com/journeyapps/zxing-android-embedded
 * Used under the following license:
 *
 * Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ScanBarcodeActivity extends AppCompatActivity {

    String TAG = "Sample";
    private Button handoverButton;
    private Button retrieveButton;
    private Button borrowButton;
    private Button returnButton;
    private Button viewButton;
    private static int SCAN_MODE_CODE;
    private static boolean TEST_MODE;
    private static String TEST_ISBN;
    private static String TEST_USERNAME;
    FirebaseAuth fAuth;

    /**
     * On creation of activity all buttons are linked to their actions and codes
     * @param savedInstanceState contains the test_isbn and test_username
     */
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

        SCAN_MODE_CODE = -1; //Default scan mode for when no button has been pressed yet

        fAuth = FirebaseAuth.getInstance();

        //Starts the scan for handing over as an owner
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

        //Starts the scan for retrieving as an owner
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

        //Starts the scan for borrowing as a borrower
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

        //Starts the scan for returning as a borrower
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

        //Starts the scan for viewing the info of a book
        viewButton = findViewById(R.id.view_button);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processView(TEST_ISBN);
                }else {
                    SCAN_MODE_CODE = 5;
                    new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
                }
            }
        });

    }

    /**
     * Processes a scan for handover given a specified ISBN
     * @param isbn The isbn of the scanned book
     * @param currentUsername The username of the current user
     */
    protected void processHandOver(String isbn, String currentUsername){
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            //If no books are found with matching isbn
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                            Log.d(TAG, "scannedBook Owner: " + scannedBook.getOwner());
                            if (scannedBook.getOwner().equals(currentUsername)) {
                                //If the book is owned by the current user then start the activity for accepting requests
                                Intent requestActivity = new Intent(ScanBarcodeActivity.this, AcceptRequestActivity.class);
                                requestActivity.putExtra("isbn", isbn);
                                startActivity(requestActivity);
                            } else {
                                //If the book is not owned by the current user
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
     * @param isbn The isbn of the scanned book
     * @param currentUsername The username of the current user
     */
    protected void processRetrieve(String isbn, String currentUsername){
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            //If no book is found with the matching isbn
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                            if (scannedBook.getOwner().equals(currentUsername))  {
                                //If the book is owned by the current user then mark it as available
                                scannedBook.setStatus("available");
                                Database.writeBook(scannedBook);
                                Toast.makeText(ScanBarcodeActivity.this, scannedBook.getTitle() + " has been marked as available.", Toast.LENGTH_LONG).show();
                            } else {
                                //If the book is not owned by the current user
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
     * @param isbn The isbn of the scanned book
     * @param currentUsername The username of the current user
     */
    protected void processBorrow(String isbn, String currentUsername){
        Log.d(TAG, "Borrowing book.");
        Database.queryCollection("requests", new String[]{"book.isbn", "creator.username"}, new String[]{isbn, currentUsername})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            //If no request is found on the book made by the current user
                            Toast.makeText(ScanBarcodeActivity.this, "Error: No request has been made on book.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Request userRequest = queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class);
                            if (userRequest.getStatus().equals("accepted")) {
                                //If the request has been accepted
                                Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (queryDocumentSnapshots.getDocuments().size() == 0){
                                                    //If no book is found with the scanned isbn
                                                    Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system.", Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    //If a book is found then mark it as borrowed and set the current user as the borrower
                                                    Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                                                    scannedBook.setStatus("borrowed");
                                                    Log.d(TAG, "currentID: " + fAuth.getCurrentUser().getUid());
                                                    scannedBook.setBorrowerId(fAuth.getCurrentUser().getUid());
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

                            } else {
                                //If the request has not been accepted yet
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
     * @param isbn The isbn of the scanned book
     * @param currentUsername The username of the current user
     */
    protected void processReturn(String isbn, String currentUsername){
        Log.d(TAG, "Returning book.");
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            //If no book is found with the scanned isbn
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system", Toast.LENGTH_LONG).show();
                        }
                        else {
                            //If the book is found
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                            if (scannedBook.getBorrower() == null || !scannedBook.getBorrower().equals(currentUsername)){
                                //If the current user is not borrowing the book
                                Toast.makeText(ScanBarcodeActivity.this, "Error: " + scannedBook.getTitle() + " is not currently borrowed by you.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                //If the current user is borrowing the book then remove them as the borrower
                                scannedBook.setBorrower(null);
                                scannedBook.setBorrowerId(null);
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

    /**
     * Gets the book scanned and shows the information on that book
     * @param isbn the isbn of the scanned book
     */
    protected void processView(String isbn){
        Log.d(TAG, "Viewing book.");
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0){
                            //If no book is found with the scanned isbn
                            Toast.makeText(ScanBarcodeActivity.this, "Error: Book is not in the system", Toast.LENGTH_LONG).show();
                        }
                        else {
                            //If the book is found
                            Book scannedBook = queryDocumentSnapshots.getDocuments().get(0).toObject(Book.class);
                            Intent intent = new Intent(ScanBarcodeActivity.this, ViewBookActivity.class);
                            intent.putExtra("title", scannedBook.getTitle());
                            intent.putExtra("author", scannedBook.getAuthor());
                            intent.putExtra("owner", scannedBook.getOwner());
                            intent.putExtra("status", scannedBook.getStatus());
                            intent.putExtra("description", scannedBook.descriptionAsString());
                            intent.putExtra("isbn", scannedBook.getIsbn());
                            startActivity(intent);
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
     * Gets the results of a scan and uses the data depending on what mode has been chosen
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String isbn = result.getContents();     // get the isbn from the scan
            if(isbn == null) {
                //If the scan did not give an isbn
                Toast.makeText(this, "Scan failed, please try again.", Toast.LENGTH_LONG).show();
            } else {
                String currentEmail = fAuth.getCurrentUser().getEmail();
                Database.getUserFromEmail(currentEmail)
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String currentUsername = queryDocumentSnapshots.getDocuments().get(0).getId();      // Get the username of the current username to use in further methods

                                //Depending on the button pressed, do different actions
                                if (SCAN_MODE_CODE == 1){
                                    processHandOver(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 2){
                                    processRetrieve(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 3){
                                    processBorrow(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 4){
                                    processReturn(isbn, currentUsername);
                                } else if (SCAN_MODE_CODE == 5){
                                    processView(isbn);
                                } else {
                                    Log.d(TAG, "No matching code");
                                }
                                SCAN_MODE_CODE = -1;    //Set the scan code back to default
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
