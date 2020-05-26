package com.raj.allthingsrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<DataModel> mDataModel;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDesc;

        public MyViewHolder(View v) {
            super(v);
            mTitle = itemView.findViewById(R.id.rv_item_title);
            mDesc = itemView.findViewById(R.id.rv_item_desc);
        }
    }

    // This "constructor" gets the data passed to this class when this class
    // is called (i.e. an instance of this class is created in another class)
    public MyAdapter(ArrayList<DataModel> dataModel) {
        mDataModel = dataModel;
    }

    // onCreateViewHolder is called when recycler view needs a new ViewHolder to represent an item)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the recycler view item layout file which is in the main xml
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        // Create an instance of MyViewHolder and pass View v to it
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Display stuff in the view inflated above in onCreateViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Get the data of the "current item to be displayed" from the data model instance created earlier
        // and stores it in the "DataMode;" data type
        DataModel currentItem = mDataModel.get(position);

        // Get the respective info from the data model class and display it in the recyclerview_item xml which is in in the main xml
        holder.mTitle.setText(currentItem.getTitle());
        holder.mDesc.setText(currentItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return mDataModel.size();
    }
}