package com.romanport.deltawebmap.Framework.Views.Maps.Data;

import android.content.Context;
import android.view.View;

import com.romanport.deltawebmap.Framework.Views.Maps.MapTileLoadCallback;

public abstract class DeltaMapLayer {
    public abstract View GetView(Context context, MapTileLoadCallback callback, DeltaMapConfig config, int zoom, int x, int y);
    public abstract int GetMaxZoom(DeltaMapConfig config);
    //public abstract void DestroyView(View v); //Called when it's time to deinit a view
}
