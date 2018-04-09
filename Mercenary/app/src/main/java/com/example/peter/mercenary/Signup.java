package com.example.peter.mercenary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Allows user to create an account to use the app
 *
 * @author Melissa Buljubasic
 * @Version 1.0
 * @see LoginScreen
 */
public class Signup extends AppCompatActivity {
    Button cancel;
    Button signup;
    boolean alreadyExist=false;

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
                ElasticFactory.checkUserExist userExists = new ElasticFactory.checkUserExist();
                userExists.execute("{\n" + " \"query\": { \"match\": {\"username\":\"" + username.getText().toString() + "\"} }\n" + "}");

                try {
                    alreadyExist = userExists.get();
                    Log.i("check:", "singup Screen: is user exist?" + Boolean.toString(alreadyExist));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    if(alreadyExist){
                        errorText.setText("This user already exist, please pick a new name.");
                    }
                    else {
                        User newUser = new User(username.getText().toString(), email.getText().toString(),
                                phone.getText().toString(), 0);

                        ElasticFactory.AddingUser addUser = new ElasticFactory.AddingUser();
                        addUser.execute(newUser);
                        finish();
                    }
                } catch (UsernameTooLongException e) {
                    errorText.setText("Username must be less then 8 characters");
                } catch (InvalidEmailException e) {
                    errorText.setText("Email is invalid");
                } //catch(UserAlreadyExistException e){}
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
}
