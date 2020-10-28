package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
                String password1 = password1Field.getText().toString().trim();
                String password2 = password2Field.getText().toString().trim();
                final String email = emailField.getText().toString().trim();
                final String phoneNumber = phoneNumberField.getText().toString().trim();

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

                // Ensure other validation as needed.

                // write to DB
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
                                    uid = fAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(uid);
                                    Map<String, Object> userInfo = new HashMap<String, Object>();
                                    userInfo.put("username", username);
                                    userInfo.put("phoneNumber", phoneNumber);
                                    userInfo.put("email", email);
                                    documentReference.set(userInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "User profile is created for " + username);
                                                }
                                            });
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(SignUpActivity.this,
                                            "Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
            }
        });
    }

    public void signupRedirectLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}