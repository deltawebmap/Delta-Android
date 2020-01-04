package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapIconData;
import com.romanport.deltawebmap.R;

public class DeltaMapIcon extends ViewGroup {

    public float normalizedPosX; //0-1, 0 being top left
    public float normalizedPosY; //0-1, 0 being top left
    public DeltaMapIconCircle circle;

    public DeltaMapIcon(Context c, DeltaMapIconData data, DeltaMapConfig cfg) {
        super(c);

        //Set pos
        PointF pos = data.GetNormalizedPos(cfg);
        normalizedPosY = pos.y;
        normalizedPosX = pos.x;

        //Add circle
        circle = new DeltaMapIconCircle(c, data.GetBorderColor(cfg));
        addView(circle);

        //Add all other views
        View[] v = data.GetInnerViews(c, cfg);
        for(View vv : v)
            addView(vv);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Get child count
        int childCount = getChildCount();

        //Get width of ourselves
        int size = Math.min(getMeasuredHeight(), getMeasuredWidth());

        //Loop through children
        for(int i = 0; i<childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if(child instanceof DeltaMapIconViewParams) {
                Point offset = ((DeltaMapIconViewParams) child).GetViewOffset();
                int cSize = ((DeltaMapIconViewParams) child).GetViewSize(size);
                child.layout(offset.x, offset.y, offset.x + cSize, offset.y + cSize);
            } else {
                child.layout(0, 0, size, size);
            }
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

}
