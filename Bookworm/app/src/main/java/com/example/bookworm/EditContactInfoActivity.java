package com.example.bookworm;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 * EditContactInfoActivity class
 * Handles all functionality of activity_edit_contact_info
 */
public class EditContactInfoActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private Uri imageFilePath;

    private String username = "";
    private TextView usernameView;
    private TextView phoneEditView;
    private TextView emailEditView;
    private ImageView contactImage;

    private final int PICK_IMAGE_REQUEST = 22;

    /**
     * onCreate initializer.
     * Initializes the EditContactInfo activity and retrieves all relevant data from the database to display it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        fAuth = FirebaseAuth.getInstance();

        if(getIntent().getExtras() != null){
            username = getIntent().getStringExtra("username");
        }

        usernameView = (TextView) findViewById(R.id.usernameView);
        phoneEditView = (TextView) findViewById(R.id.editPhoneNumber);
        emailEditView = (TextView) findViewById(R.id.editEmail);
        contactImage = (ImageView) findViewById(R.id.contactImage);

        if(username != ""){
            usernameView.setText(username);
            phoneEditView.setText("Loading phone number...");
            emailEditView.setText("Loading email...");
            Database.getUser(username).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        phoneEditView.setText(task.getResult().get("phoneNumber").toString());
                        emailEditView.setText(task.getResult().get("email").toString());
                    }
                }
            });
            Database.getProfilePhoto(fAuth.getUid()).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Picasso.get().load(task.getResult()).into(contactImage);
                    } else {
                        contactImage.setImageResource(R.drawable.ic_book);
                    }
                }
            });
        }
    }

    /**
     * Edit image button functionality
     * Starts the galley activity with intent to get an image.
     * @param view
     */
    public void editImageButton(View view){
        ImageView contactImage = (ImageView) findViewById(R.id.contactImage);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    /**
     * Button functionality for save button
     * Updates the user specified information to the database and also updates the database profile image
     * @param view
     */
    public void saveContactInfo(View view){

        if(username != "") {
            User userUpdate = new User(username, "", emailEditView.getText().toString(), phoneEditView.getText().toString());

            Database.updateUser(userUpdate);

            String userId = FirebaseAuth.getInstance().getUid();
            if(imageFilePath != null) {
                Database.writeProfilePhoto(userId, imageFilePath);
            }

            fAuth = FirebaseAuth.getInstance();
            fAuth.getCurrentUser().updateEmail(emailEditView.getText().toString());
        }

        AlertDialog inputAlert = new AlertDialog.Builder(this).create();
        inputAlert.setTitle("Contact info saved for user:");
        inputAlert.setMessage(username);
        inputAlert.show();
    }

    /**
     * Overriding onActivityResult to handle image return from gallery if available.
     * Calls super to avoid any unintended changes.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageFilePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                contactImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
