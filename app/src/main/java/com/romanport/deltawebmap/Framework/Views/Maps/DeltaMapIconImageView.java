package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class DeltaMapIconImageView extends AppCompatImageView implements DeltaMapIconViewParams {

    int padding = 30;

    public DeltaMapIconImageView(Context ctx, int padding) {
        super(ctx);
        this.padding = padding;
    }

    public DeltaMapIconImageView(Context ctx) {
        super(ctx);
    }

    public DeltaMapIconImageView(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet, 0);
    }

    @Override
    public Point GetViewOffset() {
        return new Point(padding, padding);
    }

    @Override
    public int GetViewSize(int parentSize) {
        return parentSize - padding - padding;
    }

}