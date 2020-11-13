package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.bookworm.ViewPhotoFragment.newInstance;

/**
 * Handles the actions for editing the contents of a book
 * in the database
 */
public class EditBookActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Book selectedBook;
    private EditText titleEditText;
    private EditText authorEditText;
    private TextView isbnText;
    private EditText descriptionEditText;
    private TextView ownerNameText;
    private Button viewPhotoButton;
    private Button addPhotoButton;
    private Button deletePhotoButton;
    private Button currentBorrowerButton;
    private Button viewAllRequestsButton;
    private Button deleteButton;
    private Button saveChangesButton;
    private ImageView bookPhoto;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri photoUri;
    private String isbn;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        titleEditText = findViewById(R.id.editTextTextPersonName);
        authorEditText = findViewById(R.id.editTextTextPersonName2);
        isbnText = findViewById(R.id.textView9);
        descriptionEditText = findViewById(R.id.editTextTextPersonName4);
        ownerNameText = findViewById(R.id.textView8);
        viewPhotoButton = findViewById(R.id.button3);
        addPhotoButton = findViewById(R.id.button4);
        deletePhotoButton = findViewById(R.id.button5);
        currentBorrowerButton = findViewById(R.id.button6);
        viewAllRequestsButton = findViewById(R.id.button10);
        deleteButton = findViewById(R.id.button11);
        saveChangesButton = findViewById(R.id.button12);
        bookPhoto = findViewById(R.id.book_photo);

        String[] fields = {"ownerId", "isbn"};
        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");

        Database.getBookPhoto(FirebaseAuth.getInstance().getUid(), isbn)
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(context).load(task.getResult()).into(bookPhoto);
                        bookPhoto.setTag(-1);
                    } else {
                        bookPhoto.setImageResource(R.drawable.ic_book);
                        bookPhoto.setTag(R.drawable.ic_book);
                    }

                }
            });

        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        String[] values = {uid, isbn};
        Database.queryCollection("books", fields, values).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        selectedBook = document.toObject(Book.class);
                    }
                    Database.getBookPhoto(selectedBook.getOwner(), isbn)
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Glide.with(context).load(task.getResult()).into(bookPhoto);
                                        bookPhoto.setTag(-1);
                                    } else {
                                        bookPhoto.setImageResource(R.drawable.ic_book);
                                        bookPhoto.setTag(R.drawable.ic_book);
                                    }

                                }
                            });
                    titleEditText.setText(selectedBook.getTitle());
                    authorEditText.setText(selectedBook.getAuthor());
                    isbnText.setText(selectedBook.getIsbn());
                    ownerNameText.setText(selectedBook.getOwner());
                    ArrayList<String> descriptions = selectedBook.getDescription();
                    String description = "";
                    for (String s : descriptions) {
                       description = description.concat(s).concat(" ");
                    }
                    descriptionEditText.setText(description);
                }
            }
        });

        viewPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) bookPhoto.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ViewPhotoFragment fragment = newInstance(bitmap);
                    fragment.show(getSupportFragmentManager(), "VIEW_PHOTO");
                }
                catch (Exception e){
                    Toast.makeText(EditBookActivity.this, "No book photo available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });


        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delImage();
            }
        });

        currentBorrowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewContactInfoActivity.class);
                intent.putExtra("username", selectedBook.getBorrower());
                startActivity(intent);
            }
        });

        viewAllRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.deleteBook(selectedBook);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (Database.getListenerSignal() == 1) {
                            Toast.makeText(EditBookActivity.this, "Your book is successfully deleted", Toast.LENGTH_SHORT).show();
                            finishEditing();
                        } else if (Database.getListenerSignal() == -1){
                            Toast.makeText(EditBookActivity.this, "Something went wrong while deleting your book", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditBookActivity.this, "Something went wrong while deleting your book, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the fields from the UI
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String isbn = isbnText.getText().toString();
                ArrayList<String> descriptions;
                descriptions = new ArrayList<String>();
                String[] ss = descriptionEditText.getText().toString().split(" ");
                for (String s : ss) {
                    if (!TextUtils.isEmpty(s)) {
                        descriptions.add(s);
                    }
                }

                // If the form is missing information
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(isbn)) {
                    Toast.makeText(EditBookActivity.this, "Title, author, and ISBN are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt to upload the image, if the image exists
                    String userID = FirebaseAuth.getInstance().getUid();
                    if (photoUri == null) {
                        addBookToDB(title, author, isbn, descriptions);
                    } else {
                        Database.writeBookPhoto(ownerNameText.getText().toString(), isbn, photoUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        addBookToDB(title, author, isbn, descriptions);
                                        finishEditing();
                                    } else {
                                        Toast.makeText(EditBookActivity.this,
                                            "Image could not be written to database",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    }
                                }
                            });
                    }
                }
            }
        });
    }

    /**
     * Centralized logic for writing the book to the
     * DB and waiting for the response
     * @param title the title of the book
     * @param author the author of the book
     * @param isbn the isbn of the book
     * @param descriptions an array of the book's description's keywords
     */
    private void addBookToDB(String title, String author, String isbn, ArrayList<String> descriptions) {
        selectedBook.setTitle(title);
        selectedBook.setAuthor(author);
        selectedBook.setIsbn(isbn);
        selectedBook.setDescription(descriptions);

        // Write the book to the database
        Database.writeBook(selectedBook);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (Database.getListenerSignal() == 1) {
                Toast.makeText(EditBookActivity.this,
                    "Your book is successfully saved",
                    Toast.LENGTH_SHORT).show();
                finish();
            } else if (Database.getListenerSignal() == -1){
                Toast.makeText(EditBookActivity.this,
                    "Something went wrong while adding your book",
                    Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditBookActivity.this,
                    "Something went wrong while adding your book, please try again",
                    Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    /**
     * Creates the activity for adding an image to the book
     * from the user's phone
     */
    private void addImage() {
        if ((int) bookPhoto.getTag() == R.drawable.ic_book) {
            // Defining Implicit Intent to mobile gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
        else{
            Toast.makeText(EditBookActivity.this, "Book Photo already exists! please try to remove then add a new one.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes the previewed image from the book
     */
    private void delImage() {
        if ((int) bookPhoto.getTag() != R.drawable.ic_book) {
            bookPhoto.setImageResource(R.drawable.ic_book);
            bookPhoto.setTag(R.drawable.ic_book);
            Database.deleteBookPhoto(FirebaseAuth.getInstance().getUid(), isbn)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditBookActivity.this, "Image successfully deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
        } else{
            Toast.makeText(EditBookActivity.this, "Book Photo is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * reverts back to the original activity after finishing
     */
    private void finishEditing() {
        finish();
    }

    /**
     * Gets the result from the image selection activity
     * @param requestCode The success code of the request
     * @param resultCode the code for the activity
     * @param data the data returned by the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                this.photoUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(this.photoUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                bookPhoto.setImageBitmap(selectedImage);
                bookPhoto.setTag(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditBookActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}