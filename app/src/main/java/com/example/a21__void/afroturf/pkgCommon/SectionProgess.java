package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class SectionProgess extends View {
    boolean section = false;
    public SectionProgess(Context context) {
        super(context);
    }

    public SectionProgess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionProgess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SectionProgess(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!section)
            return;

        Paint p = new Paint();
        p.setColor(Color.parseColor("#fb6c66"));
        p.setStrokeWidth(5);


        double angle = Math.PI / 6;
        float t= 40;

        float spaceY = canvas.getHeight() * (float)Math.tan(angle);


        int count = (int)(canvas.getWidth() + spaceY)/ (int)t;
        for(int pos = 0; pos < count; pos++){
            float x0 = pos * t, y0 = 0;
            float x1  = x0 + spaceY, y1 = canvas.getHeight();

            canvas.drawLine(x0,y0, x1, y1, p);
        }

    }

    public void section(boolean b) {
        this.section = b;
    }
}
