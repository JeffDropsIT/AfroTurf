package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.a21__void.afroturf.R;

/**
 * Created by ASANDA on 2018/12/20.
 * for Pandaphic
 */
public class AfroEditText extends RelativeLayout {
    private int resIcon = R.drawable.ic_map_deselected;
    private String hint = "";

    private LinearLayout linHolder;
    private ImageView imgIcon;
    private EditText edtInput;

    public AfroEditText(Context context) {
        super(context);
        this.init(null, 0, 0);
    }

    public AfroEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0, 0);
    }

    public AfroEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AfroEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defRes) {
        inflate(this.getContext(), R.layout.afro_editext_layout, this);

        this.linHolder = this.findViewById(R.id.lin_holder);
        this.imgIcon = this.findViewById(R.id.img_icon);
        this.edtInput = this.findViewById(R.id.edt_input);

        if(attrs == null){
            this.imgIcon.setImageResource(this.resIcon);
            this.edtInput.setHint(this.hint);
        }else{

            TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.AfroEditText, defStyle, defRes);
            this.setResIcon(arr.getResourceId(R.styleable.AfroEditText_iconRes, R.drawable.ic_map_deselected));
            this.setHint( arr.getString(R.styleable.AfroEditText_hint));
            arr.recycle();
        }

    }



    public int getResIcon() {
        return resIcon;
    }


    public String getHint() {
        return hint;
    }

    public String getText(){
        return this.edtInput.getText().toString();
    }

    public void setHint(String hint) {
        this.hint = hint;
        this.edtInput.setHint(this.hint);
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
        this.imgIcon.setImageResource(this.resIcon);
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        if(this.linHolder != null)
            this.linHolder.setSelected(selected);
    }
}
