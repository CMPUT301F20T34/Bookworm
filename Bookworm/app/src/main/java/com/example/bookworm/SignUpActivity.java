package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText username;
    EditText password1;
    EditText password2;
    EditText email;
    EditText phoneNumber;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username_signup);
        password1 = findViewById(R.id.password1_signup);
        password2 = findViewById(R.id.password2_signup);
        email = findViewById(R.id.email_address_signup);
        phoneNumber = findViewById(R.id.phone_number_signup);
        register = findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle validation for form
                // write to DB
                // redirect to login screen
            }
        });
    }

    public void signupRedirectLogin(View view) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}