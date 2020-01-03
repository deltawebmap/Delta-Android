package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class DeltaMapTileHolder extends ViewGroup {

    public DeltaMapContainer container;

    public DeltaMapTileHolder(Context ctx, DeltaMapContainer container) {
        super(ctx);
        this.container = container;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Get child count
        int childCount = getChildCount();

        //Since we want to enforce this being STRICTLY a square, find the min bounds
        int size = Math.min(getMeasuredHeight(), getMeasuredWidth());
        int offsetTop = 0;//(getMeasuredHeight() - size) / 2;
        int offsetLeft = 0;//(getMeasuredWidth() - size) / 2;

        //Loop through children
        //We position all items of this class to be in four quadrants of this. If the type doesn't match this,
        //we'll position it on top of us. That'll usually be our image until a user zooms in.
        for(int i = 0; i<childCount; i++) {
            DeltaMapTile child = (DeltaMapTile)getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            //Determine how many tiles per unit this has and how large it should be
            int tilesPerUnit = (int)Math.pow(2, child.zoom);
            int cSize = size / tilesPerUnit;

            //Determine size
            int cWidth = cSize;
            int cHeight = cSize;

            //Set this to occupy the target location
            int cLeft = cSize * child.x;
            int cTop = cSize * child.y;

            //Apply
            child.layout(cLeft + offsetLeft, cTop + offsetTop, cLeft + cWidth + offsetLeft, cTop + cHeight + offsetTop);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Measure ourselves
        int size = container.GetMaxCanvasPixels();
        setMeasuredDimension(size, size);

        //Measure our children
        int childCount = getChildCount();
        for(int i = 0; i<childCount; i++) {
            DeltaMapTile child = (DeltaMapTile)getChildAt(i);
            int tilesPerUnit = (int)Math.pow(2, child.zoom);
            int cSize = size / tilesPerUnit;
            int blockSpec = MeasureSpec.makeMeasureSpec(cSize, MeasureSpec.EXACTLY);
            measureChild(child, blockSpec, blockSpec);
        }
    }

    public void AddTile(Context ctx, int x, int y, int zoom) {
        DeltaMapTile t = new DeltaMapTile(ctx, x, y, zoom, container);
        addView(t);
        container.tiles.add(t);
    }

}
