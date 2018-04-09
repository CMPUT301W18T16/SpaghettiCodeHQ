package com.example.peter.mercenary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

/**
 * Login screen to log into the app with a user account
 *
 * @author Melissa Buljubasic
 * @Version 1.0
 */
public class LoginScreen extends AppCompatActivity {
    private EditText username;
    private Button login;
    private Button signup;
    private TextView errorText;
    private User user;
    private NetworkStatus status;

    //Code for returning user is from here:
    //https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
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
                ElasticFactory.checkUserExist checkUser = new ElasticFactory.checkUserExist();
                String query = "{\n" + " \"query\": { \"match\": {\"username\":\"" + usernameText + "\"} }\n" + "}";
                try {
                    if (checkUser.execute(query).get()) {
                        ElasticFactory.GetUser getUser = new ElasticFactory.GetUser();
                        user = getUser.execute(query).get();

                        Intent intent = new Intent(LoginScreen.this, MainActivity.class);

                        intent.putExtra("user", user);
                        //intent.putExtra("username", user.getUsername());
                        startActivity(intent);

                    } else {
                        errorText.setText("Invalid username");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                username.setText("");
                errorText.setText("");
            }
        });
    }
}
