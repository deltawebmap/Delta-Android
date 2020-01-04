package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.ViewGroup;

import com.romanport.deltawebmap.R;

public class DeltaMapIconPane extends ViewGroup {

    public DeltaMapContainer controller;

    public DeltaMapIconPane(Context c, DeltaMapContainer controller) {
        super(c);
        this.controller = controller;
        iconSize = controller.config.iconSize;
    }

    public int iconSize = 40;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Get child count
        int childCount = getChildCount();

        //Loop through children
        for(int i = 0; i<childCount; i++) {
            DeltaMapIcon child = (DeltaMapIcon)getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            //Get the position
            PointF posF = controller.ConvertNormalizedPosToScreenPos(child.normalizedPosX, child.normalizedPosY);
            int x = (int)posF.x - (iconSize / 2);
            int y = (int)posF.y - (iconSize / 2);

            //Apply
            child.layout(x, y, x + iconSize, y + iconSize);
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
        int blockSpec = MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(widthSize, heightSize);
    }
}
