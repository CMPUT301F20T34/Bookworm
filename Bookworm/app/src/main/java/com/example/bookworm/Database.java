package com.example.bookworm;

import android.net.Uri;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all methods related to reading, writing, and deleting from the database.
 */
public class Database {
    private static int listenerSignal = 0;  // Used to verify that a write/read/delete has completed
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final Library library = new Library();
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
     * Returns the listener signal to verify that the db operation has completed
     *
     * @return listenerSignal an int, 0 if incomplete, 1 if success, -1 if failed
     */
    static int getListenerSignal() {
        return listenerSignal;
    }

    /**
     * Writes a library to the Main_Library database
     *
     * @param lib the library to be written
     */
    static void writeLibrary(Library lib) {
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
     *
     * @param book the book to be written
     */
    static void writeBook(final Book book) {
        Database.listenerSignal = 0;
        final DocumentReference bookDocument = libraryCollection
            .document(libraryName)
            .collection(bookName)
            .document(book.getIsbn());
        Map<String, Object> bookInfo = new HashMap<>();
        bookInfo.put("author", book.getAuthor());
        bookInfo.put("borrower", book.getBorrower());
        bookInfo.put("borrowerId", book.getBorrowerId());
        bookInfo.put("description", book.getDescription());
        bookInfo.put("isbn", book.getIsbn());
        bookInfo.put("owner", book.getOwner());
        bookInfo.put("ownerId", book.getOwnerId());
        bookInfo.put("photograph", book.getPhotograph());
        bookInfo.put("status", book.getStatus());
        bookInfo.put("title", book.getTitle());
        bookDocument.set(bookInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot written");
                Database.listenerSignal = 1;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                Database.listenerSignal = -1;
            }
        });
    }

    /**
     * Deletes a book from the database
     *
     * @param book the book to be deleted
     */
    static void deleteBook(final Book book) {
        Database.listenerSignal = 0;
        libraryCollection
            .document(libraryName)
            .collection(bookName)
            .document(book.getIsbn())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Book deleted!");
                    Database.listenerSignal = 1;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting book.", e);
                    Database.listenerSignal = -1;
                }
            });
    }

    /**
     * Returns all books that contain the searchTerm as their exact title.
     * Will rework in the future to return books that contain the searchTerm.
     *
     * @param searchTerm The keyword that is being searched
     * @return Task<QuerySnapshot> The result of the query.
     */
    public static Task<QuerySnapshot> searchBooks(final String searchTerm) {
        CollectionReference books = libraryCollection.document(libraryName)
            .collection(bookName);

        return books.whereEqualTo("title", searchTerm).get();
    }

    /**
     * Finds all the books in which the status matches one of the provided statues and
     * the description contains the keyword given
     *
     * @param statuses An array of statues that the book can match
     * @param keyword  The keyword to be searched for
     * @return A task containing a querysnapshot that returns all documents matching the parameters
     */
    static Task<QuerySnapshot> bookKeywordSearch(String[] statuses, String keyword) {
        if (statuses.length == 0) {
            throw new IllegalArgumentException("statuses cannot be empty");
        }

        Query query = libraryCollection.document(libraryName).collection("books")
            .whereIn("status", Arrays.asList(statuses))
            .whereArrayContains("description", keyword);

        return query.get();
    }



    /**
     * Creates a user in the database with their username,
     * email, and phone number
     *
     * @param username    the username of the user
     * @param phoneNumber email of the user
     * @param email       phone number of the user
     * @return Task containing the result of the creation
     */
    static Task<Void> createUser(final String username, String phoneNumber, String email) {
        DocumentReference documentReference = libraryCollection
            .document(libraryName)
            .collection(userName)
            .document(username);
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("phoneNumber", phoneNumber);
        userInfo.put("email", email);
        return documentReference.set(userInfo);
    }

    /**
     * Updates the user registration token for the device each time the device is started.
     */
    static public void updateUserRegistrationToken() {
        Database.getUserFromEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Could not update registration token");
                } else {
                    QuerySnapshot qs = task.getResult();
                    DocumentSnapshot doc = qs.getDocuments().get(0);
                    String username = doc.getId();
                    Map<String, Object> obj = doc.getData();
                    FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                Log.d(TAG, "Could not update registration token");
                            } else {
                                // Move so that multiple device tokens can be kept
                                Map<String, Object> map = new HashMap<>();
                                map.put("registrationToken", task1.getResult());
                                libraryCollection.document(libraryName)
                                    .collection(userName)
                                    .document(username).
                                    set(map, SetOptions.merge());
                            }
                        });
                }
            });
    }

    /**
     * Updates the user in the database
     *
     * @param user The user to update with
     */
    static void updateUser(final User user) {
        Database.listenerSignal = 0;
        DocumentReference documentReference = libraryCollection
            .document(libraryName)
            .collection(userName)
            .document(user.getUsername());
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("phoneNumber", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("borrower", user.getBorrower());
        userInfo.put("owner", user.getOwner());
        documentReference.set(userInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "User profile updated");
                    Database.listenerSignal = 1;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Failed to update user profile");
                    Database.listenerSignal = -1;
                }
            });
    }

    /**
     * Deletes a user from the database
     *
     * @param user The user to be deleted
     */
    static void deleteUser(final User user) {
        Database.listenerSignal = 0;
        libraryCollection.document(libraryName).collection(userName)
            .document(user.getUsername())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Book does not exist in database");
                    Database.listenerSignal = 1;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                    Database.listenerSignal = -1;
                }
            });
    }

    /**
     * Write/updates a Request object into the database with "isbn-borrowerId" as a documentId
     *
     * @param request The request to be written int the database
     */
    static void createSynchronousRequest(final Request request) {
        Database.listenerSignal = 0;
        Database.createRequest(request)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot written");
                    Database.listenerSignal = 1;
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                Database.listenerSignal = -1;
            }
        });
    }

    /**
     * Deletes a Request from the database.
     *
     * @param request The request to be deleted
     */
    static void deleteRequest(final Request request) {
        Database.listenerSignal = 0;
        libraryCollection.document(libraryName).collection(requestName)
            .document(request.getBook().getIsbn() + "-" + request.getCreator().getUsername())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Request does not exist in database");
                    Database.listenerSignal = 1;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                    Database.listenerSignal = -1;
                }
            });
    }

    /**
     * Uploads an image on the user's phone to be the
     * image for the book with the listed isbn.
     * @param username username the username of the book owner
     * @param isbn the isbn of the book which we are creating an image for.
     * @param photoUri the Uri representing the image file
     * @return An asynchronous task that finishes when the upload finishes.
     */
    static UploadTask writeBookPhoto(String username, String isbn, Uri photoUri) {
        String path = getBookImagePath(username, isbn);
        StorageReference loc = FirebaseStorage.getInstance().getReference().child(path);
        loc.putFile(photoUri);
        loc.getDownloadUrl();
        return loc.putFile(photoUri);
    }

    /**
     * Gets the image of the book with the specified userID & ISBN
     * @param username username the username of the book owner
     * @param isbn the isbn of the book which image we want
     * @return a task containing the URI of the image.
     */
    static Task<Uri> getBookPhoto(String username, String isbn) {
        String path = getBookImagePath(username, isbn);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference loc = storageRef.child(path);
        return loc.getDownloadUrl();
    }

    /**
     * Deletes a book's image from storage
     * @param userID the userID of the currently signed-in user.
     * @param isbn the isbn of the book to delete
     * @return a task containing the result of the deletion.
     */
    static Task<Void> deleteBookPhoto(String userID, String isbn) {
        String path = getBookImagePath(userID, isbn);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference loc = storageRef.child(path);
        return loc.delete();
    }

    /**
     * Uploads an image on the user's phone to be the
     * profile image of that user's account
     * @param username the username of the profile
     * @param photoUri the Uri of the photo
     * @return an upload task representing the completion of the upload.
     */
    static UploadTask writeProfilePhoto(String username, Uri photoUri) {
        String path = getProfilePhotoPath(username);
        StorageReference loc = FirebaseStorage.getInstance().getReference().child(path);
        loc.putFile(photoUri);
        loc.getDownloadUrl();
        return loc.putFile(photoUri);
    }

    /**
     * Gets the profile photo of the specified user
     * @param username the username of the profile
     * @return a task containing the URI of the image
     */
    static Task<Uri> getProfilePhoto(String username) {
        String path = getProfilePhotoPath(username);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference loc = storageRef.child(path);
        return loc.getDownloadUrl();
    }


    /**
     * Returns the contact info associated with a given username
     * @param username the username of the user
     * @return Task<DocumentSnapshot> A Task containing a DocumentSnapshot with the contact info
     */
    static Task<DocumentSnapshot> getUser(final String username){
        return libraryCollection.document(libraryName).collection(userName).document(username).get();
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
     * @return The list of documents which match the user
     */
    static Task<DocumentSnapshot> getUserFromUsername(final String username) {
        return libraryCollection.document(libraryName)
                .collection(userName).document(username)
                .get();
    }

    /**
     * Creates a request in the database for a given request object
     * @param req the request that must be stored in the database
     * @return a task representing the eventual completion of the database access
     */
    static Task<Void> createRequest(final Request req) {
        return libraryCollection.document(libraryName)
            .collection(requestName).document(req.getBook().getIsbn() + "-" + req.getCreator().getUsername())
            .set(req);
    }

    /**
     * Get all books that are requested by the currently signed-in user
     *
     * @return a Task representing the result of the query
     */
    static Task<QuerySnapshot> getRequestedBooks() {
        return libraryCollection.document(libraryName)
            .collection(requestName).whereEqualTo("creator.email", fAuth.getCurrentUser().getEmail())
            .get();
    }

    /**
     * Get all books that are borrowing by the currently signed-in user
     *
     * @return a Task representing the result of the query
     */
    static Task<QuerySnapshot> getBorrowingBooks() {
        return libraryCollection.document(libraryName)
                .collection(bookName).whereEqualTo("borrowerId", fAuth.getCurrentUser().getUid())
                .get();
    }

    /**
     * Get all books that are requested by the currently signed-in user and their requests are accepted by the owners.
     *
     * @return a Task representing the result of the query
     */
    static Task<QuerySnapshot> getAcceptedBooks() {
        return libraryCollection.document(libraryName)
                .collection(requestName)
                .whereEqualTo("creator.email", fAuth.getCurrentUser().getEmail())
                .whereEqualTo("status","Accepted")
                .get();
    }

    /**
     * Get all requests on a given book (determined by isbn)
     * @param isbn The isbn of the book to get the requests
     * @return a Task representing the result of the query
     */
    static Task<QuerySnapshot> getRequestsForBook(String isbn) {
        return libraryCollection.document(libraryName)
            .collection(requestName)
            .whereEqualTo("book.isbn", isbn)
            .whereEqualTo("status", "available")
            .get();
    }

    static Task<QuerySnapshot> declineRequest(String username, String isbn) {
        return libraryCollection.document(libraryName)
            .collection(requestName)
            .whereEqualTo("book.isbn", isbn)
            .whereEqualTo("creator.username", username)
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
        Query query = queryCollection;
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

    /**
     * Returns the path to the user's profile photo in storage
     * @param username the username of the book owner
     * @return the path to the user's profile photo
     */
    private static String getProfilePhotoPath(String username) {
        return "profiles/users/" + username + "/profile.jpg";
    }

    /**
     * Creates the path to a book's image storage reference
     * @param username the username of the profile
     * @param isbn the isbn of the book which we are targeting
     * @return the string representing the path to the book's image.
     */
    private static String getBookImagePath(String username, String isbn) {
        return "images/users/" + username + "/books/" + isbn + ".jpg";
    }
}
