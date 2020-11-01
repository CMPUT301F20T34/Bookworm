package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.bookworm.ViewPhotoFragment.newInstance;

public class AddBookActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Book book;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private EditText descriptionEditText;
    private TextView ownerNameText;
    private Button viewPhotoButton;
    private Button addPhotoButton;
    private Button deletePhotoButton;
    private Button addButton;

    private ImageView BookPhoto;
    private Uri filePath;
    private final int ADD_IMAGE_REQUEST = 20;
    private String sPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleEditText = findViewById(R.id.editTextTextPersonName);
        authorEditText = findViewById(R.id.editTextTextPersonName2);
        isbnEditText = findViewById(R.id.editTextNumber);
        descriptionEditText = findViewById(R.id.editTextTextPersonName4);
        ownerNameText = findViewById(R.id.textView8);
        viewPhotoButton = findViewById(R.id.button3);
        addPhotoButton = findViewById(R.id.button4);
        deletePhotoButton = findViewById(R.id.button5);
        addButton = findViewById(R.id.button6);

        BookPhoto = findViewById(R.id.book_photo);
        //BookPhoto.setImageResource(R.drawable.ic_book);

        fAuth = FirebaseAuth.getInstance();
        final String email = fAuth.getCurrentUser().getEmail();
        final String uid = fAuth.getCurrentUser().getUid();
        CollectionReference users = FirebaseFirestore.getInstance().collection("Libraries").document("Main_Library").collection("users");
        Query query = users.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ownerNameText.setText(document.getId());
                        book = new Book(document.getId(), uid);
                    }
                }
            }
        });


        viewPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) BookPhoto.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ViewPhotoFragment fragment = newInstance(bitmap);
                    fragment.show(getSupportFragmentManager(), "VIEW_PHOTO");
                }
                catch (Exception e){
                    Toast.makeText(AddBookActivity.this, "No book photo available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddImage();
            }
        });

        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String isbn = isbnEditText.getText().toString();
                ArrayList<String> descriptions = new ArrayList<String>();
                String[] ss = descriptionEditText.getText().toString().split(" ");
                for (String s : ss) {
                    if (!s.equals("")){
                        descriptions.add(s);
                    }
                }

                String owner = ownerNameText.getText().toString();
                if (title.equals("") || author.equals("") || isbn.equals("")) {
                    Toast.makeText(AddBookActivity.this, "Title, author, and ISBN are required", Toast.LENGTH_SHORT).show();
                } else {
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setIsbn(isbn);
                    book.setDescription(description);
                    //book.setPhotograph(sPhoto);
                    final ArrayList<Integer> returnValue = new ArrayList<Integer>();
                    returnValue.add(0);
                    Database.writeBook(book, returnValue);
                    int waitTime = 2000;
                    if (book.getPhotograph() == null) {
                        waitTime = 1000;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (returnValue.get(0) == 1) {
                                Toast.makeText(AddBookActivity.this, "Your book is successfully added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(addButton.getContext(), OwnerBooklistActivity.class);
                                startActivity(intent);
                            } else if (returnValue.get(0) == -1){
                                Toast.makeText(AddBookActivity.this, "Something went wrong while adding your book", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddBookActivity.this, "Something went wrong while adding your book, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, waitTime);
                }
            }
        });
    }

    private void AddImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), ADD_IMAGE_REQUEST);
    }

    private void DelImage() {
        sPhoto = null;
        BookPhoto.setImageResource(R.drawable.ic_book);
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == ADD_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                //sPhoto = BitMapToString(bitmap);
                BookPhoto.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}