package com.example.kazu.myapplication;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kubox on 2016/10/11.
 */

@Table(name = "Item")
public class Item extends Model {


    @Column(name = "date_at")
    @Getter
    @Setter
    public String dateAt;

    public Item(){
        super();
    }

    public Item(String dateAt){
        super();
        this.dateAt = dateAt;
    }
}
