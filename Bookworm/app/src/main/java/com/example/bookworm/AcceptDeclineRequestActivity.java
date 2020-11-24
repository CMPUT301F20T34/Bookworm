package com.example.bookworm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AcceptDeclineRequestActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private TextView usernameView;
    private TextView phoneView;
    private TextView emailView;
    private ImageView userImage;
    private Context context = this;
    private String username;
    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_decline_request);

        fAuth = FirebaseAuth.getInstance();

        if (getIntent().getExtras() != null) {
            username = getIntent().getStringExtra("username");
            isbn = getIntent().getStringExtra("isbn");
        } else {
            username = "";
        }

        usernameView = (TextView) findViewById(R.id.accept_decline_request_username);
        phoneView = (TextView) findViewById(R.id.accept_decline_request_phone_number_view);
        emailView = (TextView) findViewById(R.id.accept_decline_request_email_view);
        userImage = (ImageView) findViewById(R.id.accept_decline_request_user_image);

        if (!username.equals("")){
            usernameView.setText(username);
            phoneView.setText("Loading phone number...");
            emailView.setText("Loading email...");
            Database.getUser(username).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        phoneView.setText(task.getResult().get("phoneNumber").toString());
                        emailView.setText(task.getResult().get("email").toString());
                    } else {
                        Toast.makeText(context, "Could not load user's information. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Database.getProfilePhoto(username).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    userImage.setImageResource(R.drawable.ic_book);
                }
            });
        }
    }

    /**
     * Accepts the request when correct button is clicked
     * @param view the button that was clicked on.
     */
    public void acceptRequest(View view) {
        Database.acceptRequest(username, isbn)
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Could not decline request. Please try again later.", Toast.LENGTH_SHORT).show();
                    } else {
                        String id = isbn + "-" + username;
                        Map<String, Object> acc = new HashMap<>();
                        acc.put("status", "accepted");

                        /* Iterate over the documents, accepting if the
                         * username is correct and deleting the request
                         * is not correct */
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.getId().equals(id)) {
                                doc.getReference().set(acc, SetOptions.merge());
                            } else {
                                doc.getReference().delete();
                            }
                        }
                        Toast.makeText(context, "Successfully accepted request.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
    }

    /**
     * Declines the request when correct button is clicked
     * @param view the button that was clicked on.
     */
    public void declineRequest(View view) {
        Database.declineRequest(username, isbn)
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Could not decline request. Please try again later.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot doc : task.getResult()) {
                            doc.getReference().delete();
                        }
                        Toast.makeText(context, "Successfully declined request.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
    }
}