package com.example.peter.mercenary;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    User user;

    Button edit;
    TextView usernameText;
    TextView emailText;
    TextView phoneText;

    RatingBar ratings;
    ListView reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_userprofile);

        String username_current = getIntent().getStringExtra("user"); //currently signed in user
        String username_clicked = getIntent().getStringExtra("clicked_user"); //user whose profile we are looking at

        String query = "{\n" + " \"query\": { \"match\": {\"username\":\"" + username_clicked + "\"} }\n" + "}";
        ElasticFactory.GetUser getUser = new ElasticFactory.GetUser();
        getUser.execute(query);
        
        try {
            user = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "search failed");    
        }
        
        if (username_current.equals(username_clicked)) {
            edit = (Button) findViewById(R.id.editButton);
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditUserProfile.class);
                    intent.putExtra("user", user);
                    startActivityForResult(intent, 0);
                }
            });
        }

        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        ratings = findViewById(R.id.ratingBar);
        reviewsList = findViewById(R.id.reviewsList);

        usernameText.setText(user.getUsername());
        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhoneNumber());
        ratings.setRating(user.getRating());

        ArrayList<String> reviews = user.getReviews();
        if (reviews != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, reviews);
            reviewsList.setAdapter(adapter);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_userprofile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_userprofile);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            user = data.getParcelableExtra("user");
            usernameText.setText(user.getUsername());
            emailText.setText(user.getEmail());
            phoneText.setText(user.getPhoneNumber());
            ratings.setRating(user.getRating());
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_profile) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_userprofile);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_task_list) {
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.nav_assigned_list) {
            Intent intent = new Intent(UserProfile.this, AssignedTaskList.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }

        return true;
    }
}
