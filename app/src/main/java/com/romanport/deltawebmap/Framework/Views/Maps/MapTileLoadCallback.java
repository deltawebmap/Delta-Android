package com.romanport.deltawebmap.Framework.Views.Maps;

import android.view.View;

import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;

public interface MapTileLoadCallback {

    public void OnLayerLoadFinished(View v, DeltaMapLayer layer, int zoom, int x, int y);

}
