package com.project.havewemet.utils;

import android.text.InputType;
import android.widget.EditText;

public class Text {
    //this is a custom utility class to reduce code writing for frequent tasks
    private Text(){} //can't initialize this class

    public static String getStr(EditText editText){
        return editText.getText().toString();
        //this method reduces to write "toString"
    }

    public static void showPassword(EditText editText){
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //set the cursor to the current typing position
        editText.setSelection(editText.length());
    }

    public static void hidePassword(EditText editText){
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        //set the cursor to the current typing position
        editText.setSelection(editText.length());
    }
}
