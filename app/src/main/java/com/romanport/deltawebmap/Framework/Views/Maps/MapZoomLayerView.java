package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.romanport.deltawebmap.R;

import java.util.Random;

public class MapZoomLayerView extends ViewGroup {

    public int gridPosX; //Specifies where this'll be placed in a parent quadrant. Only 0-1
    public int gridPosY; //Specifies where this'll be placed in a parent quadrant. Only 0-1
    public int depth; //How deep this is (AKA zoom level)
    public int tilesPerAxis; //The number of tiles on our
    public int x; //Position from within our zoom level
    public int y; //Position from within our zoom level
    public View[] layers; //The full screen image displayed upon this
    public DeltaMapContainer controller;
    public boolean isQuadrantsDeployed; //Are there quadrants for this?

    private Context context;

    public MapZoomLayerView(Context ctx, DeltaMapContainer controller, int depth, int gridPosX, int gridPosY, int parentX, int parentY) {
        super(ctx);

        //Set vars
        this.context = ctx;
        this.controller = controller;
        this.depth = depth;
        this.gridPosY = gridPosY;
        this.gridPosX = gridPosX;
        this.isQuadrantsDeployed = false;

        //Calculate sizes
        tilesPerAxis = (int)Math.pow(2, depth);
        x = (parentX*2) + gridPosX;
        y = (parentY*2) + gridPosY;

        //Create background for DEBUG
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        setBackgroundColor(color);

        //Obtain the views that we'll apply as our actual content
        layers = controller.GetDisplayImageViews(depth, x, y);

        //Apply these layers
        for(View v : layers) {
            addView(v);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;

        //Get the width based on the measure specs
        widthSize = getDefaultSize(0, widthMeasureSpec);

        //Get the height based on measure specs
        heightSize = getDefaultSize(0, heightMeasureSpec);

        int majorDimension = Math.min(widthSize, heightSize);

        //Measure all child views
        int blockDimension = majorDimension / 2;
        int blockSpec = MeasureSpec.makeMeasureSpec(blockDimension,
                MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(majorDimension, majorDimension);
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
            int cTop;
            int cLeft;
            int cWidth;
            int cHeight;

            //If this child is the same type as us, we'll make it fill the quadrant
            if(child.getClass().equals(MapZoomLayerView.class)) {
                //This should be placed in a quadrant
                cWidth = size / 2;
                cHeight = size / 2;
                MapZoomLayerView cChild = (MapZoomLayerView)child;
                cLeft = cChild.gridPosX * cWidth;
                cTop = cChild.gridPosY * cHeight;
            } else {
                //This should fill our entire view
                cWidth = size;
                cHeight = size;
                cLeft = 0;
                cTop = 0;
            }

            //Apply
            Log.d("DEBUG_AT", "A "+getAlpha()+" top "+(cLeft + offsetLeft) + "; left "+(cTop + offsetTop));
            child.layout(cLeft + offsetLeft, cTop + offsetTop, cLeft + cWidth + offsetLeft, cTop + cHeight + offsetTop);
        }
    }

    public void AddChildrenZoomQuadrants() {
        //If we already have quadrants, ignore
        if(isQuadrantsDeployed) {
            Log.w("MapZoomLayerView", "Tried to deploy quadrants when they were already deployed!");
            return;
        }

        //Add our children
        for(int x = 0; x<2; x++) {
            for(int y = 0; y<2; y++) {
                MapZoomLayerView v = new MapZoomLayerView(context, controller, depth + 1, x, y, this.x, this.y);
                controller.views.add(v);
                addView(v);
            }
        }

        //Bring our layers back to the foreground
        for(int i = layers.length - 1; i>=0; i--) {
            layers[i].bringToFront();
        }

        //Redraw
        invalidate();
        isQuadrantsDeployed = true;
    }

    public void SetLayersAlpha(float a) {
        for(View v : layers) {
            v.setAlpha(a);
        }
    }

    public boolean IsVisible() {
        //Get our position
        Rect actualPosition = new Rect();
        boolean successful = getGlobalVisibleRect(actualPosition);
        actualPosition.offset(-(int)controller.getPosX(), -(int)controller.getPosY());
        if(!successful)
            return false;

        //Get container position
        Rect screen = new Rect(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

        if(depth == 3 && x == 0 && y == 0)
            Log.d("TEST", "T: "+actualPosition.width());

        //Check if they overlap
        return actualPosition.intersect(screen);
    }

    public Rect GetQuadrantRect(int gridX, int gridY) {
        //Get our position
        Rect pos = new Rect();
        getGlobalVisibleRect(pos);

        //Set it to only equal the size of one quadrant
        pos.right -= (pos.width() / 2);
        pos.top -= (pos.height() / 2);

        //Offset it
        pos.offset(gridX * pos.height(), gridY * pos.height());

        return pos;
    }

    public boolean IsQuadrantVisible(int gridX, int gridY) {
        //Get our position
        Rect actualPosition = GetQuadrantRect(gridX, gridY);

        //Get container position
        Rect screen = new Rect(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

        //Check if they overlap
        return actualPosition.intersect(screen);
    }

    public String GetDebugString() {
        return "Z:"+depth+"; X:"+x+"; Y:"+y;
    }

    public void GetTilePosOnScreen() {
        //Get our position
        Rect actualPosition = new Rect();
        boolean successful = getGlobalVisibleRect(actualPosition);
        Log.d("MAP-POS", "L-P:"+actualPosition.left);
    }
}
