package com.raj.allthingsrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<DataModel> mDataModel;
    private RecyclerViewClickInterface mRecyclerViewClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDesc;

        /*
            A ViewHolder is more than just a dumb object that only holds the item’s views. It is the very object that represents each item in
            our collection and will be used to display it. Each item in a recycler view has its own view holder
        */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.rv_item_title);
            mDesc = itemView.findViewById(R.id.rv_item_desc);

            // OnClickListeners for each and every way in which the user is expected to "touch" the recycler view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerViewClickInterface.onItemClick(getLayoutPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mRecyclerViewClickInterface.onLongItemClick(getLayoutPosition());

                    return true;
                }
            });
            //---------------------------------------------------------------------------------------------------
        }
    }
    /*
        This "constructor" gets the data passed to this class when this class
        is called (i.e. an instance of this class is created in another class)
    */
    public MyAdapter(ArrayList<DataModel> dataModel, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.mDataModel = dataModel;
        this.mRecyclerViewClickInterface = recyclerViewClickInterface;
    }
    /*
        onCreateViewHolder is called when recycler view needs a new ViewHolder to represent an item
        When ever a new item previously not visible, but just became visible cuz the user scrolled up/down - then the onCreateViewHolder
        method is called to display that item
        In short, whenever the recycler view needs to (display a new item / item that just became visible), onCreateViewHolder is called
    */
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the recycler view item layout file which is in the main xml
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        // Create an instance of MyViewHolder and pass itemView to it
        // itemView contains the inflated recyclerview_item layout file
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    /*
        Once the onCreateViewHolder method has been called... we need to fill the xml components in the recyclerview_item layout
        onBindViewHolder comes into to play now... it fills up the text vies, etc, etc in the  recyclerview_item layout
    */
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