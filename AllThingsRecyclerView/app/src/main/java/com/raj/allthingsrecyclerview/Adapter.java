package com.raj.allthingsrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
    private ArrayList<DataModel> mDataModelInstance; // Creating an instance of the data model class

    // View holder class
    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        // View holder holds the xml components displayed inside the recycler view
        public myViewHolder(View itemView) {
            super(itemView);

            // Inside view holder, we need to bind the xml components to this adapter class
            mTextView1 = itemView.findViewById(R.id.item_title);
            mTextView2 = itemView.findViewById(R.id.item_desc);
        }
    }

    public Adapter(ArrayList<DataModel> RecyclerViewList) {
        mDataModelInstance = RecyclerViewList;
    }

    // onCreateViewHolder comes into play when recycler view is created...
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate recyclerview_item layout file
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        // Create an instance of the view holder method (on top) and pass it the view v
        myViewHolder itemViewHolder = new myViewHolder(v);
        return itemViewHolder;
    }
    // Get the data from the data model instance and display it in the recycler view
    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        DataModel currentItem = mDataModelInstance.get(position);

        //get information from the data model using it getter methods
        holder.mTextView1.setText(currentItem.getTitle());
        holder.mTextView2.setText(currentItem.getDesc());
    }

    // data model size
    @Override
    public int getItemCount() {
        return mDataModelInstance.size();
    }
}
