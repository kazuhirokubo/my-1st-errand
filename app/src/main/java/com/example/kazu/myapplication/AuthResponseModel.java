package com.example.kazu.myapplication;

/**
 * Created by kubox on 2016/10/17.
 */

public class AuthResponseModel {

    public String result;

    public String getResult(){
        return this.result;
    }

    public void setResult(String result){
        this.result = result;
    }

    public AuthResponseModel(String result){
        this.result = result;
    }
}
