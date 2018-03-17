package com.example.peter.mercenary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);




        NavigationView navigationView = findViewById(R.id.nav_view);
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        setContentView(R.layout.activity_main2);
                        mDrawerLayout = findViewById(R.id.drawer_layout);
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setContentView(R.layout.activity_main2);
                mDrawerLayout = findViewById(R.id.drawer_layout);
                mDrawerLayout.openDrawer(GravityCompat.START);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void establishNavDrawer(Toolbar toolbar){
        NavigationView view  = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }*/


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
}
