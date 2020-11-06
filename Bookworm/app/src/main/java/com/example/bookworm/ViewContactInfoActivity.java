package com.example.bookworm;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * ViewContactInfoActivity class
 * Handles all functionality of activity_view_contact_info
 */
public class ViewContactInfoActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private TextView usernameView;
    private TextView phoneView;
    private TextView emailView;
    private ImageView contactImage;

    /**
     * onCreate initializer.
     * Initializes the ViewContactInfo activity and retrieves all relevant data from the database to display it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_info);

        fAuth = FirebaseAuth.getInstance();

        String username = "";
        if(getIntent().getExtras() != null){
            username = getIntent().getStringExtra("username");
        }

        usernameView = (TextView) findViewById(R.id.usernameView2);
        phoneView = (TextView) findViewById(R.id.viewPhoneNumber);
        emailView = (TextView) findViewById(R.id.viewEmail);
        contactImage = (ImageView) findViewById(R.id.contactImage);

        if(username != ""){
            usernameView.setText(username);
            phoneView.setText("Loading phone number...");
            emailView.setText("Loading email...");
            Database.getUser(username).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        phoneView.setText(task.getResult().get("phoneNumber").toString());
                        emailView.setText(task.getResult().get("email").toString());
                    }
                }});

            Database.getProfilePhoto(username)
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(contactImage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        contactImage.setImageResource(R.drawable.ic_book);
                    }
                });
        }
    }
}
