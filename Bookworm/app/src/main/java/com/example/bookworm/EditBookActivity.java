package com.example.bookworm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.bookworm.ViewPhotoFragment.newInstance;

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
    private ImageView BookPhoto;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private String sPhoto;

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
        BookPhoto = findViewById(R.id.book_photo);
        BookPhoto.setImageResource(R.drawable.ic_book);
        BookPhoto.setTag(R.drawable.ic_book);

        String[] fields = {"ownerId", "isbn"};
        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");
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
                if (selectedBook.getPhotograph() != null) {
                    BookPhoto.setImageBitmap(StringToBitMap(selectedBook.getPhotograph()));
                    BookPhoto.setTag(0);
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
                    Toast.makeText(EditBookActivity.this, "No book photo available", Toast.LENGTH_SHORT).show();
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
                            finish();
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
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                ArrayList<String> descriptions = new ArrayList<String>();
                String[] ss = descriptionEditText.getText().toString().split(" ");
                for (String s : ss) {
                    if (!s.equals("")){
                        descriptions.add(s);
                    }
                }
                //BitmapDrawable drawable = (BitmapDrawable) BookPhoto.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();
                if (title.equals("") || author.equals("")) {
                    Toast.makeText(EditBookActivity.this, "Title and author are required", Toast.LENGTH_SHORT).show();
                } else {
                    selectedBook.setTitle(title);
                    selectedBook.setAuthor(author);
                    selectedBook.setDescription(descriptions);
                    //if (sPhoto != null) {
                    selectedBook.setPhotograph(sPhoto);
                    //}
                    final ArrayList<Integer> returnValue = new ArrayList<Integer>();
                    returnValue.add(0);
//                    Database.writeBook(selectedBook, returnValue);
//                    int waitTime = 2000;
//                    if (selectedBook.getPhotograph() == null) {
//                        waitTime = 1000;
//                    }
                    Database.writeBook(selectedBook);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (Database.getListenerSignal() == 1) {
                                Toast.makeText(EditBookActivity.this, "Your book is successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(saveChangesButton.getContext(), OwnerBooklistActivity.class);
                                startActivity(intent);
                            } else if (Database.getListenerSignal() == -1){
                                Toast.makeText(EditBookActivity.this, "Something went wrong while updating your book", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditBookActivity.this, "Something went wrong while updating your book, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);
                }
            }
        });
    }

    private void AddImage() {
        if ((int) BookPhoto.getTag() == R.drawable.ic_book) {
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

    private void DelImage() {
        if ((int) BookPhoto.getTag() != R.drawable.ic_book) {
            sPhoto = null;
            BookPhoto.setImageResource(R.drawable.ic_book);
            BookPhoto.setTag(R.drawable.ic_book);
        }
        else{
            Toast.makeText(EditBookActivity.this, "Book Photo is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
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
        if (requestCode == PICK_IMAGE_REQUEST
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
                sPhoto = BitMapToString(bitmap);
                BookPhoto.setImageBitmap(bitmap);
                BookPhoto.setTag(0);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}