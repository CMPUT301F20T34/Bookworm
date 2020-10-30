package com.example.bookworm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
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
     * Updates a book in the database or writes a new one if it does not exist yet
     * @param book the book to be written
     * @param returnValue an array containing a single value changed to 1 for success and -1 for failure
     */
    static void writeBook(final Book book, final int[] returnValue){
        if (returnValue.length == 0){
            throw new IllegalArgumentException("returnValue must have a value in it.");
        }
        final CollectionReference bookCollection = libraryCollection.document(libraryName).collection("books");
        Task bookTask = bookCollection.whereEqualTo("isbn", book.getIsbn()).get();
        bookTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                //If the book does not exist yet then a new one gets added
                if (querySnapshot.getDocuments().size() == 0) {
                    Map<String, Object> bookInfo = new HashMap<>();
                    bookInfo.put("author", book.getAuthor());
                    bookInfo.put("borrower", book.getBorrower());
                    bookInfo.put("description", book.getDescription());
                    bookInfo.put("isbn", book.getIsbn());
                    bookInfo.put("owner", book.getOwner());
                    bookInfo.put("photograph", book.getPhotograph());
                    bookInfo.put("status", book.getStatus());
                    bookInfo.put("title", book.getTitle());
                    bookCollection.add(bookInfo)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    returnValue[0] = 1;
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    returnValue[0] = -1;
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
                //If the book already exist it is updated by its id
                else{
                    String bookId = querySnapshot.getDocuments().get(0).getId();
                    bookCollection.document(bookId)
                        .update(
                                "author", book.getAuthor(),
                                "borrower", book.getBorrower(),
                                "description", book.getDescription(),
                                "isbn", book.getIsbn(),
                                "owner", book.getOwner(),
                                "photograph", book.getPhotograph(),
                                "status", book.getStatus(),
                                "title", book.getTitle()
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                returnValue[0] = 1;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                                returnValue[0] = -1;
                            }
                        });
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error querying collection");
                returnValue[0] = -1;
            }
        });
    }

    /**
     * Deletes a book from the database
     * @param book the book to be deleted
     * @param returnValue an array containing a single value changed to 1 for success and -1 for failure
     */
    static void deleteBook(final Book book, final int[] returnValue){
        if (returnValue.length == 0){
            throw new IllegalArgumentException("returnValue must have a value in it.");
        }
        final CollectionReference bookCollection = libraryCollection.document(libraryName).collection("books");
        Task bookTask = bookCollection.whereEqualTo("isbn", book.getIsbn()).get();
        bookTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                //If the book does not exist yet then a new one gets added
                if (querySnapshot.getDocuments().size() == 0) {
                    Log.d(TAG, "Book does not exist in database");
                    returnValue[0] = 1;
                }
                //If the book already exist it is updated by its id
                else{
                    String bookId = querySnapshot.getDocuments().get(0).getId();
                    bookCollection.document(bookId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    returnValue[0] = 1;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    returnValue[0] = -1;
                                }
                            });
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error querying collection");
                returnValue[0] = -1;
            }
        });
    }

    /**
     * Returns all books that contain the searchTerm as their exact title.
     * Will rework in the future to return books that contain the searchTerm.
     * @param searchTerm The keyword that is being searched
     * @return Task<QuerySnapshot> The result of the query.
     */
    public Task<QuerySnapshot> searchBooks(final String searchTerm) {
        CollectionReference books = libraryCollection.document(libraryName)
            .collection(bookName);

        return books.whereEqualTo("title", searchTerm).get();
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
     * Returns the contact info associated with a given username
     * @param username the username of the user
     * @return Task<DocumentSnapshot> A Task containing a DocumentSnapshot with the contact info
     */
    static Task<DocumentSnapshot> getUser(final String username){
        return libraryCollection.document(libraryName).collection("users").document(username).get();
    }

    /**
     * Returns the user from a given email.
     * Used to retrieve the user info from the signed in user
     * @param email the email of the user
     * @return Task<QuerySnapshot>
     */
    static Task<QuerySnapshot> getUserFromEmail(final String email) {
        return libraryCollection.document(libraryName).collection(userName).whereEqualTo("email", email).get();
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
     * @return a Task for a QuerySnapshot that contains zero or more DocumentReferences that can be retrieved by QuerySnapshot.getDocuments()
     */
    static Task<QuerySnapshot> queryCollection(String collection, String[] fields, String[] values){
        if (fields.length != values.length){
            throw new IllegalArgumentException("Size of fields must match size of values");
        }
        if (fields.length == 0){
            throw new IllegalArgumentException("ArrayList cannot be empty");
        }
        CollectionReference queryCollection = libraryCollection.document(libraryName).collection(collection);
        Query query = (Query) queryCollection;
        for (int i = 0; i < fields.length; i++){
            query = query.whereEqualTo(fields[i], values[i]);
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
