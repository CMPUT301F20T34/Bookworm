package com.example.bookworm;

<<<<<<< HEAD
public class ViewContactInfoActivity {
=======
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ViewContactInfoActivity  extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private TextView phoneView;
    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_info);

        fAuth = FirebaseAuth.getInstance();

        String username = "";
        if(getIntent().getExtras() != null){
            username = getIntent().getStringExtra("username");
        }

        phoneView = (TextView) findViewById(R.id.viewPhoneNumber);
        emailView = (TextView) findViewById(R.id.viewEmail);
        ImageView contactImage = (ImageView) findViewById(R.id.contactImage);

        if(username != ""){
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
        }
        //contactImage.setImageResource(); // Need more info on how we are handling images
    }

>>>>>>> Fixed saving contact info and added my functionality to profile view.
}
