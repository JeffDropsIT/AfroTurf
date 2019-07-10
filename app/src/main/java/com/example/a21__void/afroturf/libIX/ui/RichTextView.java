package com.example.a21__void.afroturf.libIX.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

/**
 * Created by ASANDA on 2019/07/07.
 * for Pandaphic
 */
public class RichTextView extends LinearLayout {
    private TextView txtMessage;
    private ImageView imgIcon;

    public RichTextView(Context context) {
        super(context);
        this.init(null, 0,0);
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0,0);
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defRes) {
        this.inflate(this.getContext(), R.layout.layout_rich_textview, this);

        this.imgIcon = this.findViewById(R.id.img_icon);
        this.txtMessage = this.findViewById(R.id.txt_message);

        this.imgIcon.setBackgroundColor(Color.parseColor("#11000000"));

        if(attrs == null){
            this.setBackgroundColor(Color.TRANSPARENT);
            this.txtMessage.setText("");
        }else{
            TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.rich_textview, defStyle, defRes);
            this.setIcon(arr.getResourceId(R.styleable.rich_textview_icon, R.drawable.circle));
            this.setMessage(arr.getString(R.styleable.rich_textview_message));
            this.setPrimaryColor(arr.getColor(R.styleable.rich_textview_primaryColor, Color.WHITE));
            arr.recycle();
        }

    }

    public void setIcon(int resID){ this.imgIcon.setImageResource(resID); }
    public void setMessage(String message){ this.txtMessage.setText(message); }
    public void setPrimaryColor(int color){
        ImageViewCompat.setImageTintList(this.imgIcon, ColorStateList.valueOf(color));
        this.txtMessage.setTextColor(color);
    }
}
