package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

/**
 * Created by ASANDA on 2018/12/20.
 * for Pandaphic
 */
public class AfroDropDown extends RelativeLayout {
    private String selection = null;

    private TextView txtSelection;

    public AfroDropDown(Context context) {
        super(context);
        this.init(null, 0, 0);

    }

    public AfroDropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0, 0);
    }

    public AfroDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AfroDropDown(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defRes) {
        inflate(this.getContext(), R.layout.template_dropdown, this);

        this.txtSelection = this.findViewById(R.id.txt_selection);

        if(attrs == null){
            this.txtSelection.setText(this.selection);
        }else{
            TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.AfroDropDown, defStyle, defRes);
            this.setSelection(arr.getString(R.styleable.AfroDropDown_selection));
            arr.recycle();
        }

    }


    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
        if(this.selection != null)
            this.txtSelection.setText(this.selection);
        else
            this.txtSelection.setText("Select option");
    }
}
