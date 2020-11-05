package com.example.bookworm;

<<<<<<< HEAD
public class EditContactInfoActivity {
=======
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.annotation.Nullable;

public class EditContactInfoActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private String username = "";
    private TextView phoneEditView;
    private TextView emailEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        fAuth = FirebaseAuth.getInstance();

        if(getIntent().getExtras() != null){
            username = getIntent().getStringExtra("username");
        }

        phoneEditView = (TextView) findViewById(R.id.editPhoneNumber);
        emailEditView = (TextView) findViewById(R.id.editEmail);
        ImageView contactImage = (ImageView) findViewById(R.id.contactImage);

        if(username != ""){
            phoneEditView.setText("Loading phone number...");
            emailEditView.setText("Loading email...");
            Database.getUser(username).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        phoneEditView.setText(task.getResult().get("phoneNumber").toString());
                        emailEditView.setText(task.getResult().get("email").toString());
                    }
                }});
        }
        //contactImage.setImageResource(); Need more info on how we are handling images
    }

    public void editImageButton(View view){
        ImageView contactImage = (ImageView) findViewById(R.id.contactImage);

        // Call to gallery activity to get image

        AlertDialog inputAlert = new AlertDialog.Builder(this).create();
        inputAlert.setTitle("Currently need gallery support!");
        inputAlert.setMessage("Will be here soon though!");
        inputAlert.show();
    }

    public void saveContactInfo(View view){
        // Implement save to firebase

        User userUpdate = new User(username, "", emailEditView.getText().toString(), phoneEditView.getText().toString());

        Database.updateUser(userUpdate);

        while(Database.getListenerSignal() == 0){
            try{
                Thread.sleep(100);
            }catch (Exception e){}
        }

        AlertDialog inputAlert = new AlertDialog.Builder(this).create();
        inputAlert.setTitle("Contact info saved for user:");
        inputAlert.setMessage(username);
        inputAlert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Handle gallery activity return
        // Set image
    }

>>>>>>> Fixed saving contact info and added my functionality to profile view.
}
