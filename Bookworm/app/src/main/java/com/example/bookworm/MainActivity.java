package com.example.bookworm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";
    private Button myBooklistButton;
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        // If a user is not registered, redirect them
        // to the login screen.
//        if (fAuth.getCurrentUser() == null) {
//            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
//        }

        Database.createListener();

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database

        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available");
        Borrower exampleBorrower = new Borrower("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        Owner exampleOwner = exampleBorrower.getOwner();
        Request exampleReq = new Request(exampleBook, exampleBorrower, "Status");
        exampleLibrary = new Library();
        exampleLibrary.addOwner(exampleOwner);
        exampleLibrary.addBorrower(exampleBorrower);
        exampleLibrary.addBook(exampleBook);
        exampleLibrary.addRequest(exampleReq);

        Database.writeLibrary(exampleLibrary);

        Log.d(TAG, String.valueOf(exampleLibrary.getBooks()));

        myBooklistButton = findViewById(R.id.button6);
        myBooklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(myBooklistButton.getContext(), OwnerBooklistActivity.class));
            }
        });
    }
}