package com.example.kazu.myapplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kubox on 2016/10/07.
 */



public class ListViewItemModel {

    @Getter
    @Setter
    private String date;

    public ListViewItemModel(String date){
        this.date = date;
    }
}
