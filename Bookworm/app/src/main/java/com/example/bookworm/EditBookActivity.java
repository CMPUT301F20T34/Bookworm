package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditBookActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Book selectedBook;
    private EditText titleEditText;
    private EditText authorEditText;
    private TextView isbnText;
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
        isbnText = findViewById(R.id.textView9);
        descriptionEditText = findViewById(R.id.editTextTextPersonName4);
        ownerNameText = findViewById(R.id.textView8);
        viewPhotoButton = findViewById(R.id.button3);
        addPhotoButton = findViewById(R.id.button4);
        deletePhotoButton = findViewById(R.id.button5);
        currentBorrowerButton = findViewById(R.id.button6);
        viewAllRequestsButton = findViewById(R.id.button10);
        deleteButton = findViewById(R.id.button11);
        saveChangesButton = findViewById(R.id.button12);

        String[] fields = {"ownerId", "isbn"};
        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");
        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        String[] values = {uid, isbn};
        Database.queryCollection("books", fields, values).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    selectedBook = document.toObject(Book.class);
                }
                titleEditText.setText(selectedBook.getTitle());
                authorEditText.setText(selectedBook.getAuthor());
                isbnText.setText(selectedBook.getIsbn());
                descriptionEditText.setText(selectedBook.getDescription());
                ownerNameText.setText(selectedBook.getOwner());
            }
            }
        });

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
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String isbn = isbnText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String owner = ownerNameText.getText().toString();
                if (title.equals("") || author.equals("")) {
                    Toast.makeText(EditBookActivity.this, "Title and author are required", Toast.LENGTH_SHORT).show();
                } else {
                    selectedBook.setTitle(title);
                    selectedBook.setAuthor(author);
                    selectedBook.setIsbn(isbn);
                    selectedBook.setDescription(description);
                    final ArrayList<Integer> returnValue = new ArrayList<Integer>();
                    returnValue.add(0);
                    Database.writeBook(selectedBook, returnValue);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (returnValue.get(0) == 1) {
                                Toast.makeText(EditBookActivity.this, "Your book is successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(saveChangesButton.getContext(), OwnerBooklistActivity.class);
                                startActivity(intent);
                            } else if (returnValue.get(0) == -1){
                                Toast.makeText(EditBookActivity.this, "Something went wrong while updating your book", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditBookActivity.this, "Something went wrong while updating your book, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                }
            }
        });
    }
}