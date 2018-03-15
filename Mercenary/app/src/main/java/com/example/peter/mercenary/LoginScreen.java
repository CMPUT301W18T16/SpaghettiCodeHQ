package com.example.peter.mercenary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginScreen extends AppCompatActivity {
    EditText username;
    Button login;
    Button signup;
    TextView errorText;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        username = (EditText) findViewById(R.id.usernameText);
        login = (Button) findViewById(R.id.loginBtn);
        signup = (Button) findViewById(R.id.signupBtn);
        errorText = (TextView) findViewById(R.id.error);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                if (isValid(usernameText)) {
                    errorText.setText("Valid username");
                } else {
                    errorText.setText("Invalid username");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValid(String name) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader in =  new BufferedReader(new InputStreamReader(fis));
            String line = in.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts[0] == name) {
                    user = new User(parts[0], parts[1], parts[2], Float.parseFloat(parts[4]));
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
