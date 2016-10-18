package com.example.kazu.myapplication;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kubox on 2016/10/18.
 */

public class ItemModel {

    @Getter
    @Setter
    public String id;

    @Getter
    @Setter
    public String body;

    @Getter
    @Setter
    public String created_at;

    @Getter
    @Setter
    public String updated_at;

    public ItemModel(String id, String body, String created_at, String updated_at){
        this.id = id;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
