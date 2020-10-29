package com.example.bookworm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains all methods related to reading and writing from the database.
 */
public class Database {

    private static Library library = new Library();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference libraryCollection = db.collection("Libraries");
    private static final String libraryName = "Main_Library";
    private static final String bookName = "books";
    private static final String requestName = "requests";
    private static final String userName = "users";
    private static final String TAG = "Sample";

    static Library getLibrary() {
        return library;
    }

    /**
     * Writes a library to the Main_Library database
     * @param lib the library to be written
     */
    static void writeLibrary(Library lib){
        WriteBatch batch = db.batch();
        ArrayList<Book> books = lib.getBooks();
        ArrayList<User> users = lib.getUsers();
        ArrayList<Request> requests = lib.getRequests();
        DocumentReference tmpDoc;

        CollectionReference bookCollection = libraryCollection.document(libraryName).collection("books");
        CollectionReference userCollection = libraryCollection.document(libraryName).collection("users");
        CollectionReference requestCollection = libraryCollection.document(libraryName).collection("requests");

        for (Book book : books) {
            tmpDoc = bookCollection.document();
            batch.set(tmpDoc, book);
        }

        for (User user : users) {
            tmpDoc = userCollection.document();
            batch.set(tmpDoc, user);
        }

        for (Request request : requests) {
            tmpDoc = requestCollection.document();
            batch.set(tmpDoc, request);
        }

        batch.commit()
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
    }

    /**
     * Creates a snapshot listener for the given library so it is updated whenever a change is made to the database
     *
     */
    static void createListener(){
        libraryCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //Log.d(TAG, String.valueOf(doc.getData()));
                    //Updates the array lists in the library object
                    Library newLibrary = doc.toObject(Library.class);
                    library.setBooks(newLibrary.getBooks());
                    library.setRequests(newLibrary.getRequests());
                    library.setUsers(newLibrary.getUsers());
                }
            }
        });
    }
}
