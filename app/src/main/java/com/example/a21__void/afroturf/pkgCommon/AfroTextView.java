package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by ASANDA on 2018/12/21.
 * for Pandaphic
 */
public class AfroTextView extends android.support.v7.widget.AppCompatTextView {

    public AfroTextView(Context context) {
        super(context);
        this.init();
    }

    public AfroTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();

    }

    public AfroTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();

    }

    private void init(){
        Typeface typeface = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/barlow_semi_bold.ttf");
        this.setTypeface(typeface);
    }
}
