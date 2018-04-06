package com.example.peter.mercenary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    User curUser; //the user currently logging in
    User pUser; //the user whose profile is being looked at

    Button edit;
    TextView usernameText;
    TextView emailText;
    TextView phoneText;

    RatingBar ratings;
    ListView reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        curUser = getIntent().getParcelableExtra("user");
        pUser = getIntent().getParcelableExtra("clicked_user");

        if (curUser.getUsername().equals(pUser.getUsername())) {
            edit = (Button) findViewById(R.id.editButton);
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditUserProfile.class);
                    intent.putExtra("user", curUser);
                    startActivityForResult(intent, 0);
                }
            });
        }

        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        ratings = findViewById(R.id.ratingBar);
        reviewsList = findViewById(R.id.reviewsList);

        usernameText.setText(pUser.getUsername());
        emailText.setText(pUser.getEmail());
        phoneText.setText(pUser.getPhoneNumber());
        ratings.setRating(pUser.getRating());

        ArrayList<String> reviews = pUser.getReviews();
        if (reviews != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, reviews);
            reviewsList.setAdapter(adapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            pUser = data.getParcelableExtra("user");
            usernameText.setText(pUser.getUsername());
            emailText.setText(pUser.getEmail());
            phoneText.setText(pUser.getPhoneNumber());
            ratings.setRating(pUser.getRating());
        }
    }
}
