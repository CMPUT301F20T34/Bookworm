package com.example.bookworm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.FileInputStream;

public class ViewBookActivity extends AppCompatActivity {
    String title;
    String author;
    String owner;
    String status;
    String description;
    String isbn;
    Bitmap photograph;
    TextView titleView;
    TextView authorView;
    TextView ownerView;
    TextView descriptionView;
    TextView statusView;
    ImageView photoView;
    Button requestButton;
    final Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        // Get information so we don't have to re-query
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");
        owner = intent.getStringExtra("owner");
        status = intent.getStringExtra("status");
        description = intent.getStringExtra("description");
        isbn = intent.getStringExtra("isbn");

        String filename = getIntent().getStringExtra("photograph");

        titleView = findViewById(R.id.view_book_title);
        authorView = findViewById(R.id.view_book_author);
        ownerView = findViewById(R.id.view_book_owner);
        descriptionView = findViewById(R.id.view_book_static_description);
        statusView = findViewById(R.id.view_book_status);
        photoView = findViewById(R.id.view_book_image);

        titleView.setText("Title: " + title);
        authorView.setText("Author: " + author);
        ownerView.setText("Owner: " + owner);
        descriptionView.setText("Description:\n" + description);
        statusView.setText("Status: " + status.substring(0, 1).toUpperCase() + status.substring(1));
        if (filename != "null") {
            try {
                FileInputStream is = this.openFileInput(filename);
                photograph = BitmapFactory.decodeStream(is);
                is.close();
                photoView.setImageBitmap(Util.scaleToFitHeight(photograph, 650));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        requestButton = findViewById(R.id.view_book_request);
        requestButton.setOnClickListener(v -> {

            // Attempt to create a request from the book and the current
            // signed in user.
            Book book = new Book(title, author, description, isbn, status);
            book.setOwner(owner);
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Database.getUserFromEmail(email)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Successfully got username from the email of the user.
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    Request request = new Request(book, doc.toObject(User.class), "available");
                    Database.createRequest(request, doc.getId())

                        .addOnSuccessListener(aVoid -> {
                            // Successfully created the request in the database.
                            Toast.makeText(context,
                                "Request has successfully been made.",
                                Toast.LENGTH_LONG)
                                .show();
                            finish();
                        })

                        .addOnFailureListener(e -> {
                            // Request was not made.
                            Toast.makeText(context,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                        });
                })

                .addOnFailureListener(e -> Toast.makeText(context,
                    // Could not get username from current signed in user.
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show());
        });
    }
}