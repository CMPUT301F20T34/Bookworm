package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser fUser;
    String authEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView phoneNumber = findViewById(R.id.phone_profile);
        final TextView email = findViewById(R.id.email_profile);
        final TextView username = findViewById(R.id.username_profile);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        /* Get the email of the user. If they don't
            exist, redirect user to sign-up screen */
        if (fUser != null) {
            authEmail = fUser.getEmail();
        } else {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }

        System.out.println("here");
        Database.getUserFromEmail(authEmail)
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> data = doc.getData();
                        phoneNumber.setText(data.get("phoneNumber").toString());
                        email.setText(data.get("email").toString());
                        username.setText(doc.getId());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,
                    "Can't retrieve user information from database",
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}