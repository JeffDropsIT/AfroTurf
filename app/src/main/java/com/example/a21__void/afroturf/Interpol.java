package com.example.a21__void.afroturf;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ASANDA on 2018/12/18.
 * for Pandaphic
 */
public final class Interpol {
    public static void hideKeyBoard(Context context, View view){
        InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view == null)
            view = new View(context);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
