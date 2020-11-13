package com.example.bookworm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The main activity of the program which delegates all tasks,
 * other than signing-in or registering an account.
 */
public class MainActivity extends AppCompatActivity {

    Library exampleLibrary;
    String TAG = "Sample";
    private Button myBooklistButton;
    private Button myProfileButton;
    private Button mySearchButton;
    private Button myBorrowerInfoButton;
    private Button scanBarcodeButton;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        final EditText mySearchTerm = findViewById(R.id.keywordSearchBar);

        // Allow realtime updates for database
        Database.createListener();

        // If a user is not registered, redirect them
        // to the signup screen.
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }

        // Create listeners for the buttons
        myBooklistButton = findViewById(R.id.booklist_button);
        myBooklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(myBooklistButton.getContext(), OwnerBooklistActivity.class));
            }
        });


        myProfileButton = findViewById(R.id.profile_button);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });



        mySearchButton = findViewById(R.id.search_button);
        mySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox titleCheckbox = findViewById(R.id.titleCheckbox);
                CheckBox descCheckbox = findViewById(R.id.descCheckbox);
                TextView keywordView = findViewById(R.id.keywordSearchBar);
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                if (titleCheckbox.isChecked()){
                    intent.putExtra("type", "title");
                }
                else if (descCheckbox.isChecked()){
                    intent.putExtra("type", "description");
                }
                if (titleCheckbox.isChecked() ^ descCheckbox.isChecked()){
                    intent.putExtra("searchTerm", keywordView.getText().toString());
                    startActivity(intent);
                }
            }
        });
        myBorrowerInfoButton = findViewById(R.id.borrow_info_button);
        myBorrowerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BorrowerMainActivity.class));
            }
        });

        scanBarcodeButton = findViewById(R.id.scan_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(v);
            }

        });
    }

    private void dispatchTakePictureIntent(View v) {
        Log.d(TAG, "clicked barcode 1");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Log.d(TAG, "clicked barcode 2");
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
            Log.d(TAG, "clicked barcode 3");
        } catch (IOException ex) {
            Log.w(TAG, "Error: cannot access camera");
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d(TAG, "clicked barcode 4");
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            Log.d(TAG, "clicked barcode 5");
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
