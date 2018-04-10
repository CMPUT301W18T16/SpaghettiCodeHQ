package com.example.peter.mercenary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditUserProfile extends AppCompatActivity {
    User user;

    EditText username;
    EditText email;
    EditText phone;

    Button finish;
    Button cancel;

    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        user = getIntent().getParcelableExtra("user");

        email = (EditText) findViewById(R.id.emailText);
        phone = (EditText) findViewById(R.id.phoneText);

        error = (TextView) findViewById(R.id.error);

        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());

        finish = (Button) findViewById(R.id.editBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user.setEmail(email.getText().toString());
                    user.setPhoneNumber(phone.getText().toString());

                    String[] query = {"{\n" + " \"query\": { \"match\": {\"email\":\"" + user.getEmail()
                            + "\"phone\":\"" + user.getPhoneNumber() + "} }\n" + "}",
                            user.getId()};

                    ElasticFactory.UpdateUser update = new ElasticFactory.UpdateUser();
                    update.execute(query);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("user", user);
                    setResult(1, resultIntent);
                    finish();
                } catch (InvalidEmailException e) {
                    error.setText("Email must be in a valid format");
                }
            }
        });

        cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0, null);
                finish();
            }
        });
    }
}
