package com.example.kazu.myapplication;

/**
 * Created by kubox on 2016/10/07.
 */

public class ListViewItemModel {
    private String date;

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return this.date;
    }

    public ListViewItemModel(String date){
        this.setDate(date);
    }
}
