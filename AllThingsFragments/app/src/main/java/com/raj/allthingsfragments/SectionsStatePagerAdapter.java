package com.raj.allthingsfragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

//This is the fragment adapter thingy... The fragment is managed thru this adapter
public class SectionsStatePagerAdapter extends FragmentStateAdapter {

    //Array list of type Fragment to hold the fragments
    private final List<Fragment> fragmentList = new ArrayList<>();

    //Constructor
    public SectionsStatePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    //This method is called from the main activity for each fragment that we have...
    //An instance of the fragment class of the fragment we wanna add is passed to this method when its called
    public void addFragment(Fragment fragment) { fragmentList.add(fragment); }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
