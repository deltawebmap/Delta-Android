package com.romanport.deltawebmap.Framework.Views.Maps.Data;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;

public abstract class DeltaMapIconData {

    public abstract View[] GetInnerViews(Context c, DeltaMapConfig cfg);
    public abstract int GetBorderColor(DeltaMapConfig cfg);
    public abstract PointF GetNormalizedPos(DeltaMapConfig cfg);

}
