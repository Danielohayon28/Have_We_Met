package com.project.havewemet.utils;

import android.widget.EditText;

public class Text {
    //this is a custom utility class to reduce code writing for frequent tasks
    private Text(){} //can't initialize this class

    public static String getStr(EditText editText){
        return editText.getText().toString();
        //this method reduces to write "toString"
    }
}
