package com.example.bookworm;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database

        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available");
        Borrower exampleBorrower = new Borrower("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        Owner exampleOwner = exampleBorrower.getOwner();
        Request exampleReq = new Request(exampleBook, exampleBorrower, "Status");
        exampleLibrary = new Library();
        exampleLibrary.addOwner(exampleOwner);
        exampleLibrary.addBorrower(exampleBorrower);
        exampleLibrary.addBook(exampleBook);
        exampleLibrary.addRequest(exampleReq);

        Database.createListener(exampleLibrary);
        Database.writeLibrary(exampleLibrary);

        Log.d(TAG, String.valueOf(exampleLibrary.getBooks()));
    }
}