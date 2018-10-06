package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.a21__void.afroturf.R;

public class SectionProgess extends View {
    boolean section = false;

    private float angleDeg = (float)(Math.PI / 4f);
    private float sectionSpacing = 20, sectionThickness = 4;
    private int sectionColor = Color.BLACK;
    private String text = "";

    public SectionProgess(Context context) {
        super(context);
    }

    public SectionProgess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public SectionProgess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SectionProgess(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, 0, 0);
    }

    private void init(AttributeSet attrs, int defStyle, int defRes) {
        TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.SectionProgess, defStyle, defRes);
        this.angleDeg = arr.getFloat(R.styleable.SectionProgess_angleDeg, (float)(Math.PI / 4f));
        this.sectionThickness = arr.getDimensionPixelSize(R.styleable.SectionProgess_sectionThickness, 4);
        this.sectionSpacing = arr.getDimensionPixelSize(R.styleable.SectionProgess_sectionSpacing, 20);
        this.sectionColor = arr.getColor(R.styleable.SectionProgess_sectionColor, Color.BLACK);
        this.text = arr.getString(R.styleable.SectionProgess_sectionText);
        arr.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(this.sectionColor);
        p.setStrokeWidth(this.sectionThickness);

        this.angleDeg = this.angleDeg % 90;
        float spaceY = canvas.getHeight() * (float)Math.tan((this.angleDeg / 180.0) * Math.PI);


        int count = (int)(canvas.getWidth() + spaceY/ this.sectionSpacing);
        for(int pos = 0; pos < count; pos++){
            float x0 = -spaceY + pos * this.sectionSpacing, y0 = 0;
            float x1  = x0 + spaceY, y1 = canvas.getHeight();

            canvas.drawLine(x0,y0, x1, y1, p);
        }

        Rect bounds = new Rect();
        p.setFakeBoldText(true);
        p.getTextBounds(text, 0, text.length(), bounds);
        float textLeft = (canvas.getWidth() - bounds.width()) / 2.0f;
        float textBottom = (canvas.)



    }

    public void section(boolean b) {
        this.section = b;
    }
}
