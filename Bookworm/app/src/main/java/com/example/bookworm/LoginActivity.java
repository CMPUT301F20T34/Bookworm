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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailField;
    EditText passwordField;
    FirebaseAuth fAuth;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.username_login);
        passwordField = findViewById(R.id.password_login);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // CHANGE THIS TO USE EMAILS
                /* (query email from provided username) */

                // Ensure email is non-empty
                if (TextUtils.isEmpty(email)) {
                    emailField.setError("Email is a required value.");
                    return;
                }

                // Ensure password is non-empty
                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Password is a required value.");
                    return;
                }

                // Authenticate user's information
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,
                                            "Login successful.",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Redirects the user to the signup page.
     *
     * @param view the textview that was clicked on.
     */
    public void loginRedirectSignup(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}