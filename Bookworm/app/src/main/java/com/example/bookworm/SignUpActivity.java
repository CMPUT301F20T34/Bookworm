package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText usernameField;
    EditText password1Field;
    EditText password2Field;
    EditText emailField;
    EditText phoneNumberField;
    FirebaseAuth fAuth;
    Button register;
    String uid;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.username_signup);
        password1Field = findViewById(R.id.password1_signup);
        password2Field = findViewById(R.id.password2_signup);
        emailField = findViewById(R.id.email_address_signup);
        phoneNumberField = findViewById(R.id.phone_number_signup);
        register = findViewById(R.id.register_button);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameField.getText().toString().trim();
                final String password1 = password1Field.getText().toString().trim();
                final String password2 = password2Field.getText().toString().trim();
                final String email = emailField.getText().toString().trim();
                final String phoneNumber = phoneNumberField.getText().toString().trim();
                Task userExists;

                // CHANGE THIS TO USE EMAILS

                // Ensure email is non-empty
                if (TextUtils.isEmpty(email)) {
                    emailField.setError("Email is a required value.");
                    return;
                }

                // Ensure password is non-empty
                if (TextUtils.isEmpty(password1)) {
                    password1Field.setError("Password is a required value.");
                    return;
                }

                // Ensure passwords match
                if (!TextUtils.equals(password1, password2)) {
                    password1Field.setError("Passwords must match.");
                    return;
                }

                Database.userExists(username)
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!documentSnapshot.exists()) {
                                    fAuth.createUserWithEmailAndPassword(email, password1)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this,
                                                                "User has been created",
                                                                Toast.LENGTH_SHORT)
                                                                .show();

                                                        // Store user information
                                                        Database.createUser(username, phoneNumber, email);
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this,
                                                                "Error: " + task.getException().getMessage(),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUpActivity.this,
                                            "Error: Username already exists",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this,
                                        "Unable to access database. Please try again later.",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                // write to DB

            }
        });
    }

    public void signupRedirectLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}