package com.example.peter.mercenary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        //implements NavigationView.OnNavigationItemSelectedListener
{

    private EditText bodyText;
    private ListView oldTaskList;
    private ArrayList<Task> taskList = new ArrayList<Task>();
    private ArrayAdapter<Task> adapter;
    private User user; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oldTaskList = (ListView) findViewById(R.id.myTaskView);
        Button addButton = (Button) findViewById(R.id.add1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                // minci: we need to know who is adding the task
                intent.putExtra("user", user);
                startActivity(intent);
            }
            /*public void onActivityResult(int requestCode, int resultCode, Intent data){
                String title = data.getStringExtra("title");
                String desc = data.getStringExtra("description");
                String status = data.getStringExtra("status");
                String id = data.getStringExtra("id");
                Task newTask = new Task(title, desc, status, id);
                taskList.add(newTask);

                Toast toast = Toast.makeText(getApplicationContext(), title+
                        desc+status+id, Toast.LENGTH_LONG);
                toast.show();
                adapter.notifyDataSetChanged();
            }*/
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);


    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //loadFromFile(); // TODO replace this with elastic search

        //testing maps
        /*Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("Lat", 53.526f);
        intent.putExtra("Long", -113.525f);
        startActivity(intent); */

        user = getIntent().getExtras().getParcelable("USER");

        String query = "{\n" + " \"query\": { \"term\": {\"message\":\"" + "text" + "\"} }\n" + "}";

        ElasticFactory.getListOfTask getTaskList
                = new ElasticFactory.getListOfTask();
        getTaskList.execute("");

        try {
            taskList = getTaskList.get();
        }
        catch (Exception e)
        {
            Log.i("Error","Failed to get the tweets from the async object");
        }
        adapter = new ArrayAdapter<Task>(this,
                R.layout.list_item, taskList);
        oldTaskList.setAdapter(adapter);


        // listen to task clicks


        oldTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Task task = (Task) oldTaskList.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
                intent.putExtra("task",task);
                intent.putExtra("task_title",task.getTitle());
                intent.putExtra("task_desc",task.getDescription());
                intent.putExtra("task_status",task.getStatus());
                intent.putExtra("task_id",task.getId());
                intent.putExtra("task_geo_loc",task.getGeoLoc());
                intent.putStringArrayListExtra("task_img",task.getPhoto());
                intent.putExtra("user", user);
                startActivityForResult(intent,0);

            }
        });

    }

    /*public void onResume(){

    }
*/

 /*   /**
     * Get the user data for the logged in user
     *
     * @param requestCode
     * @param resultCode
     * @param data user
     */
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        user = data.getExtras().getParcelable("USER");
    }
    */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    */
}
