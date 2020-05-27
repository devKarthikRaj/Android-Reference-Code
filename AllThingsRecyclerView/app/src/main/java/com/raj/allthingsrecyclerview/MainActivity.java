package com.raj.allthingsrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private ArrayList<DataModel> dataModelInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating an instance of the data model class
        dataModelInstance = new ArrayList<>();
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
        mAdapter = new MyAdapter(dataModelInstance, this);

        // Finally, assign the layout manager and adapter to the recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
        These are the methods that define what happens when the user "touches" the recycler view

        The recycler view loves to be "touched" and this is where you put the code that executes when the recycler view is "touched"

        These methods are defined in the RecyclerViewClickInterface and since this MainActivity.Java class implements this interface,
        these methods must be defined in this class
    */
    @Override
    public void onItemClick(int position) {
        DataModel modelOfTouchedItem = dataModelInstance.get(position);
        Toast.makeText(getApplicationContext(),"Item Clicked - " + modelOfTouchedItem.getTitle(),Toast.LENGTH_SHORT).show();

        //Do whatever you want to do when a particular recycler view item is clicked
    }

    @Override
    public void onLongItemClick(int position) {
        DataModel modelOfTouchedItem = dataModelInstance.get(position);
        Toast.makeText(getApplicationContext(),"Item Long Clicked - " + modelOfTouchedItem.getTitle(),Toast.LENGTH_SHORT).show();

        //Do whatever you want to do when a particular recycler view item is long clicked
    }
}
