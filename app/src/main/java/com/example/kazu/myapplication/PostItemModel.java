package com.example.kazu.myapplication;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kubox on 2016/10/18.
 */

public class PostItemModel {

    @Getter
    @Setter
    public String id;

    @Getter
    @Setter
    public String body;

    @Getter
    @Setter
    public String created_at;

    public PostItemModel(String id, String body, String created_at){
        this.id = id;
        this.body = body;
        this.created_at = created_at;
    }
}
