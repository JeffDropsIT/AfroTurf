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
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.a21__void.afroturf.R;

public class SectionProgess extends CardView {
    boolean section = false;

    private float angleDeg = (float)(Math.PI / 4f);
    private float sectionSpacing = 20, sectionThickness = 4, sectionTextSize;
    private int sectionColor = Color.BLACK, sectionTextColor = Color.BLACK;
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


    private void init(AttributeSet attrs, int defStyle, int defRes) {
        TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.SectionProgess, defStyle, defRes);
        this.angleDeg = arr.getFloat(R.styleable.SectionProgess_angleDeg, (float)(Math.PI / 4f));
        this.sectionThickness = arr.getDimensionPixelSize(R.styleable.SectionProgess_sectionThickness, 4);
        this.sectionSpacing = arr.getDimensionPixelSize(R.styleable.SectionProgess_sectionSpacing, 20);
        this.sectionColor = arr.getColor(R.styleable.SectionProgess_sectionColor, Color.GRAY);
        this.text = arr.getString(R.styleable.SectionProgess_sectionText);
        this.sectionTextColor = arr.getColor(R.styleable.SectionProgess_sectionTextColor, Color.BLACK);
        this.sectionTextSize =  arr.getDimension(R.styleable.SectionProgess_sectionTextSize, 10);

        arr.recycle();

        float density = this.getContext().getResources().getDisplayMetrics().density;

        this.setCardBackgroundColor(Color.WHITE);
        this.setCardElevation(.5f * density);
        this.setRadius(3 * density);
        this.setPreventCornerOverlap(true);
        this.setUseCompatPadding(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(section){
        Paint p = new Paint();
        p.setColor(this.sectionColor);
        p.setStrokeWidth(this.sectionThickness);


        this.angleDeg = this.angleDeg % 90;
        float spaceY = canvas.getHeight() * (float) Math.tan((this.angleDeg / 180.0) * Math.PI);


        int count = (int) (canvas.getWidth() + spaceY / this.sectionSpacing);
        for (int pos = 0; pos < count; pos++) {
            float x0 = -spaceY + pos * this.sectionSpacing, y0 = 0;
            float x1 = x0 + spaceY, y1 = canvas.getHeight();

            canvas.drawLine(x0, y0, x1, y1, p);
        }

    }

        Rect bounds = new Rect();
        Paint textPaint = new Paint();
        textPaint.setFakeBoldText(true);
        textPaint.setColor(sectionTextColor);
        textPaint.setTextSize(sectionTextSize);
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        float textLeft = (canvas.getWidth() - bounds.width()) / 2.0f;
        float textBottom = (canvas.getHeight() + bounds.height()) / 2.0f;
        canvas.drawText(text,textLeft, textBottom, textPaint);
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public void setAngleDeg(float angleDeg) {
        this.angleDeg = angleDeg;
    }

    public void setSectionSpacing(float sectionSpacing) {
        this.sectionSpacing = sectionSpacing;
    }

    public void setSectionThickness(float sectionThickness) {
        this.sectionThickness = sectionThickness;
    }

    public void setSectionColor(int sectionColor) {
        this.sectionColor = sectionColor;
    }

    public void setSectionTextSize(float sectionTextSize){
        this.sectionTextSize = sectionTextSize;
    }

    public void setSectionTextColor(int sectionTextColor){
        this.sectionTextColor = sectionTextColor;
    }

    public void setText(String text) {
        this.text = text;
    }
}
