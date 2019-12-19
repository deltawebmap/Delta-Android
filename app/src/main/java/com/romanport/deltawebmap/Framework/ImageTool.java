package com.romanport.deltawebmap.Framework;

import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import com.romanport.deltawebmap.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageTool {

    public static void SetImageOnView(String url, ImageView v) {
        RequestCreator p = Picasso.get().load(url);
        //p.noFade();
        p.error(R.drawable.img_failed);
        p.into(v);
    }

    public static void SetImageInverted(Boolean inverted, ImageView v) {
        if(inverted) {
            v.setColorFilter(new ColorMatrixColorFilter(new float[]{
                    -1.0f,     0,     0,    0, 255, // red
                    0, -1.0f,     0,    0, 255, // green
                    0,     0, -1.0f,    0, 255, // blue
                    0,     0,     0, 1.0f,   0  // alpha
            }));
        } else {
            v.clearColorFilter();
        }
    }

}
