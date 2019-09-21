package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

/**
 * Created by ASANDA on 2018/12/20.
 * for Pandaphic
 */
public class AfroEditText extends LinearLayout implements View.OnClickListener {
    private int resIcon = R.drawable.ic_map_deselected;
    private String hint = "";

    private ImageView imgIcon, imgToggle;
    private EditText edtInput;
    private Boolean showPasswordToggle, isPasswordVisible = false;

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
        this.inflate(this.getContext(), R.layout.afro_editext_layout, this);

        this.imgIcon = this.findViewById(R.id.img_icon);
        this.imgToggle = this.findViewById(R.id.img_toggle);
        this.edtInput = this
                .findViewById(R.id.edt_inputa);

        this.imgToggle.setOnClickListener(this);
        this.edtInput.setOnClickListener(this);

        if(attrs == null){
            this.imgIcon.setImageResource(this.resIcon);
            this.edtInput.setHint(this.hint);
        }else{

            TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.AfroEditText, defStyle, defRes);
            this.setResIcon(arr.getResourceId(R.styleable.AfroEditText_iconRes, R.drawable.ic_map_deselected));
            this.setHint( arr.getString(R.styleable.AfroEditText_hint));
            this.setShowPasswordToggle(arr.getBoolean(R.styleable.AfroEditText_showPasswordToggle, false));
            arr.recycle();
        }

    }

    private void requestFocusEditText(){
        this.edtInput.requestFocus();
    }


    public void setShowPasswordToggle(Boolean showPasswordToggle) {
        this.showPasswordToggle = showPasswordToggle;
        if(this.showPasswordToggle){
            this.imgToggle.setVisibility(VISIBLE);
            this.imgToggle.setImageDrawable(ContextCompat.getDrawable(this.getContext(), this.isPasswordVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility_on));
            this.edtInput.setInputType(this.isPasswordVisible ? TYPE_CLASS_TEXT : TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
            this.edtInput.setSelection(this.edtInput.getText().length());
        }else {
            this.imgToggle.setVisibility(GONE);
            this.edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
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

    public void setNextFocusDownId(int id){
        if(this.getParent() != null && this.getParent() instanceof ViewGroup){
            ViewGroup parent = (ViewGroup)this.getParent();
            final View nextView = parent.findViewById(id);

            if(nextView != null){
                this.edtInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if(nextView instanceof AfroEditText){

                    this.edtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if(actionId == EditorInfo.IME_ACTION_NEXT){
                                ((AfroEditText) nextView).requestFocusEditText();
                                Log.i("EDT", "next");
                                return true;
                            }else{
                                return false;
                            }
                        }
                    });
                }else{
                    this.edtInput.setNextFocusDownId(id);
                }
            }
        }
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
        this.imgIcon.setImageResource(this.resIcon);
    }

    public void setHasError(boolean hasError){
        this.setSelected(hasError);
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        Log.i("dispatchSetSelected", "dispatchSetSelected: " + selected);
    }

    public EditText getEdtInput() {
        return edtInput;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_toggle: {
                this.isPasswordVisible = !this.isPasswordVisible;
                this.imgToggle.setImageDrawable(ContextCompat.getDrawable(this.getContext(), this.isPasswordVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility_on));
                this.edtInput.setInputType(this.isPasswordVisible ? TYPE_CLASS_TEXT : TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                this.edtInput.setSelection(this.edtInput.getText().length());
                break;
            }
            case R.id.edt_inputa:{
                this.setSelected(false);
                break;
            }
        }
    }
}
