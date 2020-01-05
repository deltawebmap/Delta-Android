package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DeltaMapCompleteHolder extends ViewGroup {

    public DeltaMapCompleteHolder(Context ctx) {
        super(ctx);
    }

    public DeltaMapCompleteHolder(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Get child count
        int childCount = getChildCount();

        //Get width of ourselves
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        //Loop through children
        for(int i = 0; i<childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(0, 0, width, height);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;

        //Get the width based on the measure specs
        widthSize = getDefaultSize(0, widthMeasureSpec);

        //Get the height based on measure specs
        heightSize = getDefaultSize(0, heightMeasureSpec);

        //Measure all child tiles
        int blockSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //JANKY: Dispatch touch events to all children
        //This allows us to both move the canvas and tap on icons
        //Icons might have to handle this specially themselves
        int childCount = getChildCount();
        for(int i = 0; i<childCount; i++) {
            View child = getChildAt(i);
            child.dispatchTouchEvent(event);
        }
        return true;
    }

}
