package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DeltaMapIconCircle extends View {

    public DeltaMapIconCircle(Context ctx, int borderColor) {
        super(ctx);
        this.borderColor = borderColor;
    }

    public int borderWidth = 5;
    public int borderColor = 0xff000000;

    @Override
    public void onDraw(Canvas c) {
        float halfWidth = getMeasuredHeight() / 2f;

        //Draw background
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xffffffff);
        c.drawCircle(halfWidth + borderWidth, halfWidth + borderWidth, halfWidth - (2 * borderWidth), p);

        //Draw stroke
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(borderWidth);
        p.setColor(borderColor);
        c.drawCircle(halfWidth + borderWidth, halfWidth + borderWidth, halfWidth - (2 * borderWidth), p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;

        //Get the width based on the measure specs
        widthSize = getDefaultSize(0, widthMeasureSpec);

        //Get the height based on measure specs
        heightSize = getDefaultSize(0, heightMeasureSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(widthSize, heightSize);
    }

}
