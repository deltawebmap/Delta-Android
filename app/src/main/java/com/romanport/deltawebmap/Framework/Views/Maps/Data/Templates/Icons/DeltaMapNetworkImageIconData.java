package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Icons;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapIconData;
import com.romanport.deltawebmap.Framework.Views.Maps.DeltaMapIconImageView;
import com.romanport.deltawebmap.Framework.Views.Maps.DeltaMapIconViewParams;

public abstract class DeltaMapNetworkImageIconData extends DeltaMapIconData {

    public abstract String GetImageURL(DeltaMapConfig config);
    public abstract Boolean GetIsImageInverted(DeltaMapConfig config);

    @Override
    public View[] GetInnerViews(Context c, DeltaMapConfig cfg) {
        return new View[] {
                CreateImageView(c, cfg)
        };
    }

    private ImageView CreateImageView(Context c, DeltaMapConfig cfg) {
        DeltaMapIconImageView ig = new DeltaMapIconImageView(c, 30);

        //Set image content
        String url = GetImageURL(cfg);
        ImageTool.SetImageOnView(url, ig);
        ImageTool.SetImageInverted(GetIsImageInverted(cfg), ig);

        return ig;
    }


}
