package com.example.bookworm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Database {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference libraryCollection = db.collection("Libraries");

    /**
     * Writes a library to the Main_Library database
     * @param lib the library to be written
     */
    static void writeLibrary(Library lib){
        libraryCollection
                .document("Main_Library")
                .set(lib)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Sample", "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("Sample", "Data could not be added!" + e.toString());
                    }
                });
    }

    /**
     * Creates a snapshot listener for the given library so it is updated whenever a change is made to the database
     * @param lib the library to be updated
     */
    static void createListener(final Library lib){
        libraryCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //Log.d(TAG, String.valueOf(doc.getData()));
                    //Updates the array lists in the library object
                    Library tempLibrary = doc.toObject(Library.class);
                    lib.setBooks(tempLibrary.getBooks());
                    lib.setBorrowers(tempLibrary.getBorrowers());
                    lib.setOwners(tempLibrary.getOwners());
                    lib.setRequests(tempLibrary.getRequests());
                }
            }
        });
    }


}
