package com.raj.bt;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BluetoothDeviceListAdapter extends RecyclerView.Adapter<BluetoothDeviceListAdapter.MyViewHolder> {
    private ArrayList<BluetoothDevice> mBTDevicesInfo;
    private PairedDevicesRVClickInterface mPairedDevicesRVClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mBTName;
        TextView mBTAddr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mBTName = itemView.findViewById(R.id.bt_name);
            mBTAddr = itemView.findViewById(R.id.bt_mac_addr);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPairedDevicesRVClickInterface.onItemClick(getLayoutPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mPairedDevicesRVClickInterface.onLongItemClick(getLayoutPosition());

                    return true;
                }
            });
        }
    }

    public BluetoothDeviceListAdapter(ArrayList<BluetoothDevice> btDevicesInfo, PairedDevicesRVClickInterface pairedDevicesRVClickInterface) {
        this.mBTDevicesInfo = btDevicesInfo;
        this.mPairedDevicesRVClickInterface = pairedDevicesRVClickInterface;
    }

    @Override
    public BluetoothDeviceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_rv_item, parent, false);

        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BluetoothDevice currentItem = mBTDevicesInfo.get(position);

        holder.mBTName.setText(currentItem.getName());
        holder.mBTAddr.setText(currentItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return mBTDevicesInfo.size();
    }
}