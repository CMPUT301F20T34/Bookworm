package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AddBookActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Book book;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private EditText descriptionEditText;
    private TextView ownerNameText;
    private Button viewPhotoButton;
    private Button addPhotoButton;
    private Button deletePhotoButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleEditText = findViewById(R.id.editTextTextPersonName);
        authorEditText = findViewById(R.id.editTextTextPersonName2);
        isbnEditText = findViewById(R.id.editTextNumber);
        descriptionEditText = findViewById(R.id.editTextTextPersonName4);
        ownerNameText = findViewById(R.id.textView8);
        viewPhotoButton = findViewById(R.id.button3);
        addPhotoButton = findViewById(R.id.button4);
        deletePhotoButton = findViewById(R.id.button5);
        addButton = findViewById(R.id.button6);

        fAuth = FirebaseAuth.getInstance();
        final String email = fAuth.getCurrentUser().getEmail();
        final String uid = fAuth.getCurrentUser().getUid();
        CollectionReference users = FirebaseFirestore.getInstance().collection("Libraries").document("Main_Library").collection("users");
        Query query = users.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ownerNameText.setText(document.getId());
                        book = new Book(document.getId(), uid);
                    }
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

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String isbn = isbnEditText.getText().toString();
                ArrayList<String> descriptions = new ArrayList<String>();
                String[] ss = descriptionEditText.getText().toString().split(" ");
                for (String s : ss) {
                    if (!s.equals("")){
                        descriptions.add(s);
                    }
                }

                String owner = ownerNameText.getText().toString();
                if (title.equals("") || author.equals("") || isbn.equals("")) {
                    Toast.makeText(AddBookActivity.this, "Title, author, and ISBN are required", Toast.LENGTH_SHORT).show();
                } else {
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setIsbn(isbn);
                    book.setDescription(descriptions);
                    final ArrayList<Integer> returnValue = new ArrayList<Integer>();
                    returnValue.add(0);
                    Database.writeBook(book, returnValue);
                    int waitTime = 2000;
                    if (book.getPhotograph() == null) {
                        waitTime = 1000;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (returnValue.get(0) == 1) {
                                Toast.makeText(AddBookActivity.this, "Your book is successfully added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(addButton.getContext(), OwnerBooklistActivity.class);
                                startActivity(intent);
                            } else if (returnValue.get(0) == -1){
                                Toast.makeText(AddBookActivity.this, "Something went wrong while adding your book", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddBookActivity.this, "Something went wrong while adding your book, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, waitTime);
                }
            }
        });
    }


}