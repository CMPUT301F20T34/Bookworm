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
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_prescreen);

        SCAN_MODE_CODE = -1;

        fAuth = FirebaseAuth.getInstance();

        handoverButton = findViewById(R.id.handover_button);
        handoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCAN_MODE_CODE = 1;
                new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
            }
        });

        retrieveButton = findViewById(R.id.retrieve_button);
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCAN_MODE_CODE = 2;
                new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
            }
        });

        borrowButton = findViewById(R.id.borrow_button);
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCAN_MODE_CODE = 3;
                new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
            }
        });

        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCAN_MODE_CODE = 4;
                new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
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
            if(result.getContents() == null) {
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
                                    //Hand over a book from an owner to the borrower
                                    Log.d(TAG, "Handing over book.");

                                    Database.queryCollection("books", new String[]{"isbn"}, new String[]{result.getContents()})
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
                                                            requestActivity.putExtra("isbn", result.getContents());
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
                                    SCAN_MODE_CODE = -1;
                                } else if (SCAN_MODE_CODE == 2){
                                    //Retrieve a book from a borrower
                                    Log.d(TAG, "Retrieving book.");
                                    Database.queryCollection("books", new String[]{"isbn"}, new String[]{result.getContents()})
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
                                    SCAN_MODE_CODE = -1;
                                } else if (SCAN_MODE_CODE == 3){
                                    //Borrow a book from an owner
                                    Log.d(TAG, "Borrowing book.");
                                    //check that request has been marked as accepted
                                    //add book to borrowers book list and mark it as borrowed
                                    Database.queryCollection("requests", new String[]{"book.isbn", "creator.username"}, new String[]{result.getContents(), currentUsername})
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

                                                                            Database.queryCollection("books", new String[]{"isbn"}, new String[]{result.getContents()})
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
                                    SCAN_MODE_CODE = -1;
                                } else if (SCAN_MODE_CODE == 4){
                                    //Return a book to an owner
                                    Log.d(TAG, "Returning book.");
                                    //remove book from borrowers book list
                                    Database.getUser(currentUsername)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    User currentUser = documentSnapshot.toObject(User.class);

                                                    Database.queryCollection("books", new String[]{"isbn"}, new String[]{result.getContents()})
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
                                    SCAN_MODE_CODE = -1;
                                }
                                else{
                                    Log.d(TAG, "No matching code");
                                }
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
