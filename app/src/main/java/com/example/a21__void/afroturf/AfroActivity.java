package com.example.a21__void.afroturf;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ASANDA on 2018/12/02.
 * for Pandaphic
 */
public abstract class AfroActivity extends AppCompatActivity{
    public abstract void showIndeterminateProgress();
    public abstract void hideIndeterminateProgress();

    public void hideKeyBoard(){
        InputMethodManager imm =(InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null)
            view = new View(this);

        imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    }
  
}
