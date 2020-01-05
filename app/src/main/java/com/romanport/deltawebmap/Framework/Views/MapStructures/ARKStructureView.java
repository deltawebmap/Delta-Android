package com.romanport.deltawebmap.Framework.Views.MapStructures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructureData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructuresResponse;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;

import java.io.InputStream;
import java.util.HashMap;

public class ARKStructureView extends View {

    private StructuresResponse structures;
    private ArkMapData mapData;
    private ARKStructureImageCache cache;
    private int zoom;
    private int x;
    private int y;
    private int targetZoom;

    public ARKStructureView(Context c, ARKStructureImageCache cache, StructuresResponse structures, ArkMapData mapData, int targetZoom, int zoom, int x, int y) {
        super(c);
        this.cache = cache;
        this.structures = structures;
        this.mapData = mapData;
        this.zoom = zoom;
        this.x = x;
        this.y = y;
        this.targetZoom = targetZoom;
    }

    private void DrawStructureToCanvas(Canvas c, StructureData data, float x, float y, float angle, float size) {
        Bitmap bp = cache.GetBitmap(structures.nameTable[data.imageIndex]);

        Matrix rotator = new Matrix();
        float scale = size / (float)bp.getWidth();
        rotator.postRotate(angle, bp.getWidth() / 2f, bp.getHeight() / 2f);
        rotator.postScale(scale, scale, bp.getWidth() / 2f, bp.getHeight() / 2f);

        float tX = x - (bp.getWidth() / 2f) + (size / 2f);
        float tY = y - (bp.getHeight() / 2f) + (size / 2f);
        rotator.postTranslate(tX, tY);

        c.drawBitmap(bp, rotator, null);
    }

    @Override
    public void onDraw(Canvas c) {
        //Don't draw anything if we're too zoomed out for it to be remotely useful
        if(zoom != targetZoom)
            return;

        //Calculate some things
        float calcOffset = mapData.captureSize / 2f;
        float unitsPerTile = mapData.captureSize / (float)Math.pow(2, zoom);
        float gameMinX = (x * unitsPerTile) - calcOffset;
        float gameMinY = (y * unitsPerTile) - calcOffset;
        float gameMaxX = ((x+1) * unitsPerTile) - calcOffset;
        float gameMaxY = ((y+1) * unitsPerTile) - calcOffset;
        float tSizeX = getWidth();
        float tSizeY = getHeight();

        //Draw all
        for(int i = 0; i<structures.structures.length; i++) {
            StructureData s = structures.structures[i];

            //Check if this is within range
            if(s.x > gameMaxX + (s.size * 1.5) || s.x < gameMinX - (s.size * 1.5)) {
                continue;
            }
            if(s.y > gameMaxY + (s.size * 1.5) || s.y < gameMinY - (s.size * 1.5)) {
                continue;
            }

            //Do size calculations
            float size = (s.size / unitsPerTile) * tSizeX;

            //Skip if it is too small
            if(size < 1) {
                continue;
            }

            //Determine location
            float locTileX  = ((s.x - (s.size / 2) - gameMinX) / unitsPerTile) * tSizeX;
            float locTileY  = ((s.y - (s.size / 2) - gameMinY) / unitsPerTile) * tSizeY;

            //Draw
            DrawStructureToCanvas(c, s, locTileX, locTileY, s.rotation, size);
        }
    }

}
