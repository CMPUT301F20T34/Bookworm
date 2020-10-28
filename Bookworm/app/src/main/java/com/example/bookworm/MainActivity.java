package com.example.bookworm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";
    private Button myBooklistButton;
    private Button myProfileButton;
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        // If a user is not registered, redirect them
        // to the login screen.
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }

        Database.createListener();

//        Firebase Firestore db = Firebase Firestore.getInstance();
//        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database

//        User exampleUser = new User("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
//        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available", exampleUser);
//        Request exampleReq = new Request(exampleBook, exampleUser, "Status");
//        exampleLibrary = new Library();
//        exampleLibrary.addUser(exampleUser);
//        exampleLibrary.addBook(exampleBook);
//        exampleLibrary.addRequest(exampleReq);
//
//        Database.writeLibrary(exampleLibrary);

//        Log.d(TAG, String.valueOf(exampleLibrary.getBooks()));

        myBooklistButton = findViewById(R.id.booklist_button);
        myBooklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(myBooklistButton.getContext(), OwnerBooklistActivity.class));
            }
        });

        myProfileButton = findViewById(R.id.profile_button);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
    }
}