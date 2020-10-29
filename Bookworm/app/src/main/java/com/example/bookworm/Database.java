package com.example.bookworm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all methods related to reading and writing from the database.
 */
public class Database {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
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
        final WriteBatch batch = db.batch();
        ArrayList<Book> books = lib.getBooks();
        ArrayList<User> users = lib.getUsers();
        ArrayList<Request> requests = lib.getRequests();

        CollectionReference bookCollection = libraryCollection.document(libraryName).collection("books");
        CollectionReference userCollection = libraryCollection.document(libraryName).collection("users");
        CollectionReference requestCollection = libraryCollection.document(libraryName).collection("requests");

        for (Book book : books) {
            batch.set(bookCollection.document(), book);
        }

        for (User user : users) {
            batch.set(userCollection.document(), user);
        }

        for (Request request : requests) {
            batch.set(requestCollection.document(), request);
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
     * Creates a user in the database with their username,
     * email, and phone number
     * @param username the username of the user
     * @param phoneNumber email of the user
     * @param email phone number of the user
     */
    static void createUser(final String username, String phoneNumber, String email) {
        DocumentReference documentReference = libraryCollection
                .document(libraryName)
                .collection("users")
                .document(username);
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("phoneNumber", phoneNumber);
        userInfo.put("email", email);
        documentReference.set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User profile is created for " + username);
                    }
                });
    }

    /**
     * Checks if a user exists in the database with the given username
     * @param username the username to be checked
     * @return Task<DocumentSnapshot>
     */
    static Task<DocumentSnapshot> userExists(final String username) {
        return libraryCollection.document(libraryName)
                .collection("users").document(username)
                .get();
    }

    /**
     * Queries a collection for a field matching a value
     * @param collection the collection to be queried
     * @param fields a list of fields to be looked at
     * @param values a list of values to be checked for
     * @return a Task for a QuerySnapshot that contains zero or more DocumentReferences
     */
    static Task<QuerySnapshot> getBook(String collection, ArrayList<String> fields, ArrayList<String> values){
        if (fields.size() != values.size()){
            throw new IllegalArgumentException("Size of fields must match size of values");
        }
        if (fields.size() == 0){
            throw new IllegalArgumentException("ArrayList cannot be empty");
        }
        CollectionReference queryCollection = libraryCollection.document(libraryName).collection(collection);
        Query query = (Query) queryCollection;
        for (int i = 0; i < fields.size(); i++){
            query = query.whereEqualTo(fields.get(i), values.get(i));
        }
        return query.get();
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
