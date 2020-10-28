package com.example.bookworm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EditBookActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private EditText descriptionEditText;
    private TextView ownerNameText;
    private Button viewPhotoButton;
    private Button addPhotoButton;
    private Button deletePhotoButton;
    private Button currentBorrowerButton;
    private Button viewAllRequestsButton;
    private Button deleteButton;
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        titleEditText = findViewById(R.id.editTextTextPersonName);
        authorEditText = findViewById(R.id.editTextTextPersonName2);
        isbnEditText = findViewById(R.id.editTextNumber);
        descriptionEditText = findViewById(R.id.editTextTextPersonName4);
        ownerNameText = findViewById(R.id.textView8);
        viewPhotoButton = findViewById(R.id.button3);
        addPhotoButton = findViewById(R.id.button4);
        deletePhotoButton = findViewById(R.id.button5);
        currentBorrowerButton = findViewById(R.id.button6);
        viewAllRequestsButton = findViewById(R.id.button10);
        deleteButton = findViewById(R.id.button11);
        saveChangesButton = findViewById(R.id.button12);

        Intent intent = getIntent();
        titleEditText.setText(intent.getStringExtra("title"));
        authorEditText.setText(intent.getStringExtra("author"));
        isbnEditText.setText(intent.getStringExtra("isbn"));

        viewPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Library library = Database.getLibrary();
                ArrayList<Book> books = library.getBooks();
                for (library)
            }
        });

        currentBorrowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewAllRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}