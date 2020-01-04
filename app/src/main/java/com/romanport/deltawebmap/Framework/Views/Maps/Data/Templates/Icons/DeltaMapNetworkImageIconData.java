package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Icons;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapIconData;
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
        ModifiedImageView ig = new ModifiedImageView(c, 30);

        //Set image content
        String url = GetImageURL(cfg);
        ImageTool.SetImageOnView(url, ig);
        ImageTool.SetImageInverted(GetIsImageInverted(cfg), ig);

        return ig;
    }

    public class ModifiedImageView extends AppCompatImageView implements DeltaMapIconViewParams {

        int padding;

        public ModifiedImageView(Context ctx, int padding) {
            super(ctx);
            this.padding = padding;
        }

        @Override
        public Point GetViewOffset() {
            return new Point(padding, padding);
        }

        @Override
        public int GetViewSize(int parentSize) {
            return parentSize - padding - padding;
        }

        /*@Override
        public void onMeasure(int minor, int max) {
            int size = cfg.iconSize - padding - padding;
            setMeasuredDimension(size, size);
        }*/

    }
}
