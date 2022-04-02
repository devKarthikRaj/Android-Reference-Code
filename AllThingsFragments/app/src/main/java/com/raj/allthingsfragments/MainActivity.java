package com.raj.allthingsfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Config adapter
        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager(), getLifecycle());
        sectionsStatePagerAdapter.addFragment(new Fragment1()); //Since this is the first frag created... Fragment 1 will be on top.. Displayed initially
        sectionsStatePagerAdapter.addFragment(new Fragment2());
        sectionsStatePagerAdapter.addFragment(new Fragment3());

        //Config viewPager2 - This is what holds the fragments... All 3 of our fragments are held inside viewPager2
        //viewPager2 is a component in our main activity layout
        viewPager2 = findViewById(R.id.pager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(sectionsStatePagerAdapter);
    }

    //This is a "PUBLIC" method specially written for the fragment classes...
    //When you are inside a fragment class and you want to change to another fragment...
    //You can't call the viewPager2.setCurrentItem() method from inside the fragment class...
    //So this method is placed in the main activity to be accessed by the fragment classes when we want to change to another fragment from inside a fragment
    public void setViewPager2(int fragmentNumber) {
        viewPager2.setCurrentItem(fragmentNumber);
    }
}