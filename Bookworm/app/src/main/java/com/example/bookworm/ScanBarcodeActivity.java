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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
            }
        }

        SCAN_MODE_CODE = -1;

        handoverButton = findViewById(R.id.handover_button);
        handoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TEST_MODE){
                    processHandOver(TEST_ISBN);
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
                    processRetrieve(TEST_ISBN);
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
                    processBorrow(TEST_ISBN);
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
                    processReturn(TEST_ISBN);
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
    protected void processHandOver(String isbn){
        //Hand over a book from an owner to the borrower
        Toast.makeText(this, "Handing over book with isbn: " + isbn, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Handing over book.");
        //verify that book is owned by user
        Intent requestActivity = new Intent(this, AcceptRequestActivity.class);
        requestActivity.putExtra("isbn", isbn);
        startActivity(requestActivity);
    }

    /**
     * Processes a scan for retrieve given a specified ISBN
     * @param isbn
     */
    protected void processRetrieve(String isbn){
        //Retrieve a book from a borrower
        Log.d(TAG, "Retrieving book.");
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        Book tempBook = docs.get(0).toObject(Book.class);
                        tempBook.setStatus("available");
                        Database.writeBook(tempBook);
                        Toast.makeText(ScanBarcodeActivity.this, tempBook.getTitle() + " has been marked as available.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Processes a scan for borrow given a specified ISBN
     * @param isbn
     */
    protected void processBorrow(String isbn){
        //Borrow a book from an owner
        Log.d(TAG, "Borrowing book: " + isbn);
        //check that request has been marked as accepted
        //add book to borrowers book list and mark it as borrowed
    }

    /**
     * Processes a scan for return given a specified ISBN
     * @param isbn
     */
    protected void processReturn(String isbn){
        //Return a book to an owner
        Log.d(TAG, "Returning book.");
        //remove book from borrowers book list
        Database.queryCollection("books", new String[]{"isbn"}, new String[]{isbn})
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        Book tempBook = docs.get(0).toObject(Book.class);
                        tempBook.setStatus("available");
                        Database.writeBook(tempBook);
                        Toast.makeText(ScanBarcodeActivity.this, tempBook.getTitle() + " has been marked as available.", Toast.LENGTH_LONG).show();
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
                if (SCAN_MODE_CODE == 1){
                    processHandOver(isbn);
                } else if (SCAN_MODE_CODE == 2){
                    processRetrieve(isbn);
                } else if (SCAN_MODE_CODE == 3){
                    processBorrow(isbn);
                } else if (SCAN_MODE_CODE == 4){
                    processReturn(isbn);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        SCAN_MODE_CODE = -1;
    }
}
