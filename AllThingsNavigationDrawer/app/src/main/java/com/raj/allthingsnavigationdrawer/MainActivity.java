package com.raj.allthingsnavigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        navDrawer = findViewById(R.id.nav_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Toolbar is an advanced version of action bar... We already created our toolbar in the xml file for this activity... So just set it here!
        setSupportActionBar(toolbar);

        //The below line of code is to display the hamburger button in the toolbar
        //We use the toolbar instead of the action bar in order to display the hamburger button... The toolbar is an advanced action bar that supports the hamburger button
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //The last 2 string params are not gonna be shown anywhere... Its for blind people who need a voiceover to know whats going on on the screen

        navDrawer.addDrawerListener(actionBarDrawerToggle); //Toggle nav drawer open and close when hamburger is clicked
        actionBarDrawerToggle.syncState(); //This is just for sync purposes... Just add it here!!!

        navigationView.setNavigationItemSelectedListener(this);

        //If this is the first time the activity is being invoked
        if (savedInstanceState == null) {
            //The fragment_container here refers to the id of the frame layout... The frame layout contains the fragment stack
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment1()).commit(); //Initially display fragment 1
            navigationView.setCheckedItem(R.id.fragment1); //This is to indicate in the nav drawer that fragment 1 is open
        }
    }

    //We don't wanna close the app or go to perv activity when back button is pressed if nav drawer... So we have this piece of code here to prevent exactly that...
    @Override
    public void onBackPressed() {
        //If nav drawer is open....
        if(navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START); //Close nav drawer
        } else {
            //If nav drawer is closed....
            super.onBackPressed(); //Do whatever the back button does usually
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.fragment1:
                //Display the clicked fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment1()).commit();
                break;
            case R.id.fragment2:
                //Display the clicked fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment2()).commit();
                break;
            case R.id.fragment3:
                //Display the clicked fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment3()).commit();
                break;
            case R.id.nav_clickable_item_1:
                Toast.makeText(this, "Clicked item 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_clickable_item_2:
                Toast.makeText(this, "Clicked item 2", Toast.LENGTH_SHORT).show();
                break;
        }

        //Close the nav drawer
        navDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}