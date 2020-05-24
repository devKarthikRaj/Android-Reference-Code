package com.raj.bt;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BluetoothDeviceListAdapter extends RecyclerView.Adapter<BluetoothDeviceListAdapter.myViewHolder> {
    private ArrayList<BluetoothDevice> mBluetoothDevice; // Creating an instance of the data model class

    // View holder class
    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView mTv1;
        public TextView mTv2;

        // View holder holds the xml components displayed inside the recycler view
        public myViewHolder(View itemView) {
            super(itemView);

            // Inside view holder, we need to bind the xml components to this adapter class
            mTv1 = itemView.findViewById(R.id.tv1);
            mTv2= itemView.findViewById(R.id.tv2);
        }
    }

    // onCreateViewHolder comes into play when recycler view is created...
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate recyclerview_item layout file
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_rv_item, parent, false);
        // Create an instance of the view holder method (on top) and pass it the view v
        myViewHolder itemViewHolder = new myViewHolder(v);
        return itemViewHolder;
    }

    public BluetoothDeviceListAdapter(ArrayList<BluetoothDevice> RecyclerViewList) {
        mBluetoothDevice = RecyclerViewList;
    }

    // Get the data from the data model instance and display it in the recycler view
    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        BluetoothDevice currentItem = mBluetoothDevice.get(position);

        //get information from the data model using it getter methods
        holder.mTv1.setText(currentItem.getName());
        holder.mTv2.setText(currentItem.getAddress());
    }

    // data model size
    @Override
    public int getItemCount() {
        return mBluetoothDevice.size();
    }
}
