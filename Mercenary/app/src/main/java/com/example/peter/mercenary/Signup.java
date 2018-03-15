package com.example.peter.mercenary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity {
    Button cancel;
    Button signup;

    EditText username;
    EditText email;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = (EditText) findViewById(R.id.usernameText);
                email = (EditText) findViewById(R.id.emailText);
                phone = (EditText) findViewById(R.id.phoneText);
                User newUser = new User(username.getText().toString(), email.getText().toString(),
                        phone.getText().toString());
                saveUser(newUser);
                finish();
            }
        });
        cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void saveUser(User user) {

    }
}
