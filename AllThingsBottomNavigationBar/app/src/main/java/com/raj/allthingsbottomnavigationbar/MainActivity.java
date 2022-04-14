package com.raj.allthingsbottomnavigationbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottom_navigation_view);
        bottomNavView.setOnItemSelectedListener(this); //Similar to button onclicklistener... Remember to implement NavigationBarView.OnItemSelectedListener

        //The fragment_container here refers to the id of the frame layout... The frame layout contains the fragment stack
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment1()).commit(); //Initially display fragment 1
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch(item.getItemId()) {
            case R.id.fragment1:
                selectedFragment = new Fragment1();
                break;
            case R.id.fragment2:
                selectedFragment = new Fragment2();
                break;
            case R.id.fragment3:
                selectedFragment = new Fragment3();
                break;
        }

        //Display the clicked fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    }
}