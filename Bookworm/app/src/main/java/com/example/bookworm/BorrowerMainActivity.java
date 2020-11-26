package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Represents the main activity for all borrower actions
 */
public class BorrowerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);
    }

    /**
     * List all currently borrowed books
     * @param view the button that was clicked on
     */
    public void listBorrowing(View view) {
        startActivity(new Intent(this, ListBorrowing.class));
    }

    /**
     * List all requested books
     * @param view the button that was clicked on
     */
    public void listRequested(View view){
        startActivity(new Intent(this, ListRequested.class));
    }

    /**
     * List all accepted books
     * @param view the button that was clicked on
     */
    public void listAccepted(View view){
        startActivity(new Intent(this, ListAccepted.class));
    }

    /**
     * Add a new book to the database
     * @param view the button that was clicked on
     */
    public void addBook(View view) {
        startActivity(new Intent(this, AddBookActivity.class));
    }
}