package com.example.peter.mercenary;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class RateReviewActivity extends AppCompatActivity {
    private RatingBar rating;
    private EditText review;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);

        rating = findViewById(R.id.ratingBar2);
        review = findViewById(R.id.editText);

        submit = findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");

                String query = "{\n" + " \"query\": { \"match\": {\"username\":\"" + username + "\"} }\n" + "}";
                ElasticFactory.GetUser getUser = new ElasticFactory.GetUser();
                getUser.execute(query);

                try {
                    User user = getUser.get();
                    if (review.getText() != null) {
                        user.addReview(review.getText().toString());
                    }
                    user.addRating(rating.getRating());
                    String[] newQuery = {"{\n" + " \"query\": { \"match\": {\"rating\":\"" + user.getRating() + "\"} }\n" + "}",
                            user.getId()};
                    ElasticFactory.UpdateUser updateUser = new ElasticFactory.UpdateUser();
                    updateUser.execute(newQuery);
                    finish();
                } catch (Exception e) {
                    Log.i("Error", "Search failed");
                }
            }
        });
    }
}
