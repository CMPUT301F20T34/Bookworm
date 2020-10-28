package com.example.bookworm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";
    private Button myBooklistButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.createListener();

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database
//
//        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available");
//        Owner exampleOwner = new Owner(new Borrower());
//        Borrower exampleBorrower = new Borrower(exampleOwner);
//        Request exampleReq = new Request(exampleBook, exampleBorrower, "Status");
//        exampleLibrary = new Library();
//        exampleLibrary.addOwner(exampleOwner);
//        exampleLibrary.addBorrower(exampleBorrower);
//        exampleLibrary.addBook(exampleBook);
//        exampleLibrary.addRequest(exampleReq);
//
//        Database.writeLibrary(exampleLibrary);

//        Log.d(TAG, String.valueOf(exampleLibrary.getBooks()));

        myBooklistButton = findViewById(R.id.button6);
        myBooklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myBooklistButton.getContext(), OwnerBooklistActivity.class);
                startActivity(intent);
            }
        });
    }
}