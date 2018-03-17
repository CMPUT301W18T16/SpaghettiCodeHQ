package com.example.peter.mercenary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Signup extends AppCompatActivity {
    Button cancel;
    Button signup;

    EditText username;
    EditText email;
    EditText phone;

    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signupBtn);
        errorText = (TextView) findViewById(R.id.error);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = (EditText) findViewById(R.id.usernameText);
                email = (EditText) findViewById(R.id.emailText);
                phone = (EditText) findViewById(R.id.phoneText);
                try {
                    User newUser = new User(username.getText().toString(), email.getText().toString(),
                            phone.getText().toString(), 0);
                    saveUser(newUser);
                    finish();
                } catch (UsernameTooShortException e) {
                    errorText.setText("Username must be at least 8 characters");
                } catch (InvalidEmailException e) {
                    errorText.setText("Email is invalid");
                }
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

    //based on the code from the lab
    public void saveUser(User user) {
        try {
            FileOutputStream fos = openFileOutput("users.sav", Context.MODE_APPEND);
            fos.write((user.getUsername() + "|" + user.getEmail() + "|" +
                    user.getPhoneNumber() + "|" + Float.toString(user.getRating()) + "\n").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
