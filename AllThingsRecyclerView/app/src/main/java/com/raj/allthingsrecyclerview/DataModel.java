package com.raj.allthingsrecyclerview;

// Data model class defines the data that is to be displayed in the recycler vieww
// Data model class consists of the getter and setter methods for everything displayed inside the recycler view

public class DataModel {
    private String mTitle;
    private String mDesc;

    // Setter methods
    // The setter methods are used to add/edit data in the data model
    public DataModel(String title, String desc) {
        mTitle = title;
        mDesc = desc;
    }

    // Getter methods
    // The getter methods are accessed by the recycler view adapter to get the data from the data model and display it in the recycler view
    public String getTitle() {
        return mTitle;
    }

    public String getDesc() {
        return mDesc;
    }
}