package com.raj.bt;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BluetoothDeviceListAdapter extends RecyclerView.Adapter<BluetoothDeviceListAdapter.MyViewHolder> {
    private ArrayList<BluetoothDevice> mBTDevicesInfo;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mBTName;
        TextView mBTAddr;

        public MyViewHolder(View v) {
            super(v);
            mBTName = itemView.findViewById(R.id.bt_name);
            mBTAddr = itemView.findViewById(R.id.bt_mac_addr);
        }
    }

    // This "constructor" gets the data passed to this class when this class
    // is called (i.e. an instance of this class is created in another class)
    public BluetoothDeviceListAdapter(ArrayList<BluetoothDevice> btDevicesInfo) {
        mBTDevicesInfo = btDevicesInfo;
    }

    // onCreateViewHolder is called when recycler view needs a new ViewHolder to represent an item)
    @Override
    public BluetoothDeviceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the recycler view item layout file which is in the main xml
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_rv_item, parent, false);

        // Create an instance of MyViewHolder and pass View v to it
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Display stuff in the view inflated above in onCreateViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Get the data of the "current item to be displayed" from the data model instance created earlier
        // and store it in the "DataMode;" data type
        BluetoothDevice currentItem = mBTDevicesInfo.get(position);

        // Get the respective info from the data model class and display it in the "devices_rv_item" which is in in the main xml
        holder.mBTName.setText(currentItem.getName());
        holder.mBTAddr.setText(currentItem.getAddress());
    }

    // Return the size of your data model
    @Override
    public int getItemCount() {
        return mBTDevicesInfo.size();
    }
}