package com.example.kazu.myapplication.validation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.example.kazu.myapplication.MainActivity;

/**
 * Created by kbx on 2017/01/17.
 */

public class Common {

    public static boolean isMailAddress(String mailAddress) {
        String mailFormat = "^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$";

        if (!mailAddress.matches(mailFormat)) {
            return false;
        }
        return true;
    }

    public static boolean isRequiredLength(String text){

        Log.d("isRequiredLength", String.valueOf(text.length()));

        if (4 <= text.length() && text.length() <= 8){
            Log.d("isRequiredLength", "true");
            return true;
        }
        Log.d("isRequiredLength", "false");
        return false;
    }
}
