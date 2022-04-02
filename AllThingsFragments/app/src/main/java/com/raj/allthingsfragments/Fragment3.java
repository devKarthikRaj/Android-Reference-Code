package com.raj.allthingsfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Fragment3 extends Fragment implements View.OnClickListener{

    Button btnGotoFrag1;
    Button btnGotoFrag2;
    Button btnGotoActivity2;

    public Fragment3() {
        // Required empty public constructor
    }

    //Instead of onCreate... Fragments have onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_3, container, false); //Inflate the fragment

        //Notice that the finViewById is preceded by view. cuz this is the world of fragments... A lil' different from your normal activity
        btnGotoFrag1 = view.findViewById(R.id.BTN_Goto_Fragment_1);
        btnGotoFrag2 = view.findViewById(R.id.BTN_Goto_Fragment_2);
        btnGotoActivity2 = view.findViewById(R.id.BTN_Goto_Activity_2);

        btnGotoFrag1.setOnClickListener(this);
        btnGotoFrag2.setOnClickListener(this);
        btnGotoActivity2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_Goto_Fragment_1:
                ((MainActivity)getActivity()).setViewPager2(0);
                Toast.makeText(getActivity(), "Going to fragment 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.BTN_Goto_Fragment_3:
                ((MainActivity)getActivity()).setViewPager2(1);
                Toast.makeText(getActivity(), "Going to fragment 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.BTN_Goto_Activity_2:
                startActivity(new Intent(getActivity(), Activity2.class));
                Toast.makeText(getActivity(), "Going to activity 2", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}