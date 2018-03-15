package com.example.peter.mercenary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
                        phone.getText().toString(), 0);
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

    //based on the code from the lab
    public void saveUser(User user) {
        try {
            FileOutputStream fos = openFileOutput("users.txt", Context.MODE_APPEND);
            fos.write(new String(user.getUsername() + " | " + user.getEmail() + " | " +
                    user.getPhoneNumber() + " | " + Float.toString(user.getRating()) + " | ").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
