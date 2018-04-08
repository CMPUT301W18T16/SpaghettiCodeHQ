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
    EditText username;
    Button login;
    Button signup;
    TextView errorText;
    User user;

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

                        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                        // build user from es returned JSON
                        Log.i("query",query);
                        ElasticFactory.returnUser returnedUser = new ElasticFactory.returnUser();
                        String userQuery = query;
                        User loginUser = returnedUser.execute(userQuery).get();
                        intent.putExtra("USER", loginUser);
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

    /**
     * Checks if the username entered by the user is valid
     * @param name Username entered by the user
     * @return True is the username is valid, otherwise returns false
     */
    private boolean isValid(String name) {
        try {
            FileInputStream fis = openFileInput("users.sav");
            BufferedReader in =  new BufferedReader(new InputStreamReader(fis));
            String line = in.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(name)) {
                    try {
                        user = new User(parts[0], parts[1], parts[2], Float.parseFloat(parts[3]));
                    } catch (UsernameTooLongException e) {
                        e.printStackTrace();
                    } catch (InvalidEmailException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                line = in.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
