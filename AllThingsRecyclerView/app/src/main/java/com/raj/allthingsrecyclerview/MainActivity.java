package com.raj.allthingsrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating an instance of the data model class
        ArrayList<DataModel> dataModelInstance = new ArrayList<>();
        // Using the setter methods to add data to the data model
        dataModelInstance.add(new DataModel("Title1", "Description1\n" + "Description1\n" + "Description1"));
        dataModelInstance.add(new DataModel("Title2", "Description2\n" + "Description2\n" + "Description2"));
        dataModelInstance.add(new DataModel("Title3", "Description3\n" + "Description3\n" + "Description3"));

        // Recycler view config
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // recycler view uses the adapter class to access the data model...
        // so we need to pass the data model instance to the adapter to let it know which data model to access
        mAdapter = new MyAdapter(dataModelInstance);

        // Finally, assign the layout manager and adapter to the recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
