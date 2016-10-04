package com.example.kazu.myapplication;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kazu on 16/09/30.
 */
public class ModelList {

    @Getter
    @Setter
    private String datetime;


    public ModelList(String datetime){
        this.datetime = datetime;
    }
}
