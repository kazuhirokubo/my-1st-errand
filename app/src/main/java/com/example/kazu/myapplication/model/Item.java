package com.example.kazu.myapplication.model;

import lombok.Data;

/**
 * Created by kbx on 2017/01/06.
 */

@Data
public class Item {
    private int id;
    private String body;
    private String created_at;
    private String updated_at;
}
