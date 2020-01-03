package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class DeltaMapTile extends ViewGroup {

    public int x;
    public int y;
    public int zoom;
    public DeltaMapContainer container;

    public int pixelSize;

    public DeltaMapTile(Context ctx, int x, int y, int zoom, DeltaMapContainer container) {
        super(ctx);

        this.x = x;
        this.y = y;
        this.zoom = zoom;
        this.container = container;

        Init();
    }

    private void Init() {
        //Get views to display
        View[] views = container.GetDisplayImageViews(zoom, x, y);
        for(View v : views)
            addView(v);
        invalidate();
    }

    private void DebugBackground() {
        //Create background for DEBUG
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        setBackgroundColor(color);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Get child count
        int childCount = getChildCount();

        //Since we want to enforce this being STRICTLY a square, find the min bounds
        int size = Math.min(getMeasuredHeight(), getMeasuredWidth());
        int offsetTop = (getMeasuredHeight() - size) / 2;
        int offsetLeft = (getMeasuredWidth() - size) / 2;

        //Loop through children
        //We position all items of this class to be in four quadrants of this. If the type doesn't match this,
        //we'll position it on top of us. That'll usually be our image until a user zooms in.
        for(int i = 0; i<childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            //This should fill our entire view
            int cWidth = size;
            int cHeight = size;
            int cLeft = 0;
            int cTop = 0;

            //Apply
            Log.d("DEBUG_AT", "A "+getAlpha()+" top "+(cLeft + offsetLeft) + "; left "+(cTop + offsetTop));
            child.layout(cLeft + offsetLeft, cTop + offsetTop, cLeft + cWidth + offsetLeft, cTop + cHeight + offsetTop);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Size ourselves
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int blockSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);
        setMeasuredDimension(width, height);
    }
}
