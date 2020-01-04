package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Layers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.MapTileLoadCallback;

public abstract class DeltaMapTextLayer extends DeltaMapLayer {

    public abstract String GetText(DeltaMapConfig config, int zoom, int x, int y);

    @Override
    public View GetView(Context context, MapTileLoadCallback callback, DeltaMapConfig config, int zoom, int x, int y) {
        //Create an ImageView and attach an image to it
        TextView v = new TextView(context);

        //Get and set text
        v.setText(GetText(config, zoom, x, y));

        return v;
    }

}
