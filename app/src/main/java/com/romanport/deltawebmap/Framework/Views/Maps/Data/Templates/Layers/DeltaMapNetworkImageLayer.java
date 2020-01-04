package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Layers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.MapTileLoadCallback;

public abstract class DeltaMapNetworkImageLayer extends DeltaMapLayer {

    public abstract String GetImageURL(DeltaMapConfig config, int zoom, int x, int y);

    @Override
    public View GetView(Context context, MapTileLoadCallback callback, DeltaMapConfig config, int zoom, int x, int y) {
        //Create an ImageView and attach an image to it
        ImageView v = new ImageView(context);

        //Get image URL
        String url = GetImageURL(config, zoom, x, y);

        //Load
        ImageTool.SetImageOnView(url, v);

        return v;
    }

}
