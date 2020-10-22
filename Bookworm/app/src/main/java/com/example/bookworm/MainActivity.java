package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Refer to https://cloud.google.com/firestore/docs/manage-data/add-data#javaandroid_3 for adding objects to database

        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available");
        Owner exampleOwner = new Owner("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        Borrower exampleBorrower = new Borrower(exampleOwner);
        exampleLibrary = new Library();
        exampleLibrary.addOwner(exampleOwner);
        exampleLibrary.addBorrower(exampleBorrower);
        exampleLibrary.addBook(exampleBook);

        final CollectionReference libraryCollection = db.collection("libraries");

        libraryCollection
                .document("example")
                .set(exampleLibrary)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });

        libraryCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData()));
                    exampleLibrary = doc.toObject(Library.class);
                }
            }
        });

        Log.d(TAG, String.valueOf(exampleLibrary.getBooks()));
    }
}