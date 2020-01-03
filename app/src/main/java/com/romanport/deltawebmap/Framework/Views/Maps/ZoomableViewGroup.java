package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import com.otaliastudios.zoom.ZoomLayout;

public class ZoomableViewGroup extends ZoomLayout {

    public ZoomableViewGroup(Context ctx) {
        super(ctx);
    }

    public ZoomableViewGroup(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }
}