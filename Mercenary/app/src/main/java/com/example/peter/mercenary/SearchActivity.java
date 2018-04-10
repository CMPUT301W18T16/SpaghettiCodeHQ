package com.example.peter.mercenary;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user;

    EditText terms;
    Button search;
    ListView results;

    ArrayList<Task> tasks;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_search);

        user = getIntent().getParcelableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_search);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        search = (Button) findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terms = (EditText) findViewById(R.id.searchBar);

                tasks = new ArrayList<>();
                String[] keywords = terms.getText().toString().split(",");

                ElasticFactory.getListOfTask getTaskList
                        = new ElasticFactory.getListOfTask();
                for (int i = 0; i < keywords.length; i++) {
                    String query = "{\n" + " \"query\": { \"match\": {\"description\":\"" + keywords[i] + "\"} }\n" + "}";
                    getTaskList.execute(query);

                    try {
                        ArrayList<Task> temp = getTaskList.get();
                        for (int j = 0; j < temp.size(); j++) {
                            if (!tasks.contains(temp.get(j)) &&
                                    !temp.get(j).getStatus().equals("completed")) {
                                tasks.add(temp.get(j));
                            }
                        }
                    } catch (Exception e) {
                        Log.i("Error", "Search didn't work :(");
                    }

                    if (tasks.size() != 0) {
                        results = (ListView) findViewById(R.id.listView);

                        adapter = new TaskAdapter(SearchActivity.this, tasks);
                        results.setAdapter(adapter);

                        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task task = (Task) results.getAdapter().getItem(position);

                                Intent intent = new Intent(SearchActivity.this, SingleTaskActivity.class);
                                intent.putExtra("task", task);
                                intent.putExtra("user", user);

                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(SearchActivity.this, "No results found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_profile) {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("user",user);
            startActivity(intent);
        } else if (id == R.id.nav_task_list) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.nav_assigned_list) {
            Intent intent = new Intent(this, AssignedTaskList.class);
            intent.putExtra("user",user);
            startActivity(intent);

        }else if (id == R.id.activity_search){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_assigned_list);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }



}
