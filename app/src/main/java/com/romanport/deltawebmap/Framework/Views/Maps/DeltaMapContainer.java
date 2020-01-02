package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.shopgun.android.zoomlayout.ZoomLayout;

import java.util.LinkedList;

public class DeltaMapContainer extends ZoomableViewGroup implements MapTileLoadCallback {

    public Context context;
    public DeltaMapConfig config;
    private int minRegisteredZoom;
    public LinkedList<MapZoomLayerView> views;

    public DeltaMapContainer(Context ctx) {
        super(ctx);
        Init(ctx);
    }

    public DeltaMapContainer(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        Init(ctx);
    }

    private void Init(Context ctx) {
        //Set values
        context = ctx;
        minRegisteredZoom = 0;
        views = new LinkedList<>();

        //Add listeners
        addOnZoomListener(new OnZoomListener() {
            @Override
            public void onZoomBegin(ZoomLayout view, float scale) {

            }

            @Override
            public void onZoom(ZoomLayout view, float scale) {
                OnZoom(scale);
            }

            @Override
            public void onZoomEnd(ZoomLayout view, float scale) {

            }
        });
        addOnPanListener(new OnPanListener() {
            @Override
            public void onPanBegin(ZoomLayout view) {

            }

            @Override
            public void onPan(ZoomLayout view) {
                CreateNewTiles(getScale());
                Log.d("MAP-POS", "P:"+getPosX());
                for(MapZoomLayerView layer : views) {
                    if(layer.depth == 3 && layer.x == 2 && layer.y == 0)
                        layer.GetTilePosOnScreen();
                }
            }

            @Override
            public void onPanEnd(ZoomLayout view) {

            }
        });
        setMaxScale(1000);
        setMinScale(0.5f);
    }

    public void LoadConfig(DeltaMapConfig config) {
        //Set config
        this.config = config;

        //Add content
        MapZoomLayerView v = new MapZoomLayerView(context, this, 0, 0, 0, 0, 0);
        views.add(v);
        addView(v);
        invalidate();
    }

    public View[] GetDisplayImageViews(int zoom, int x, int y) {
        //Used to obtain the actual image views that you'll be able to see

        //Run each of the layers specified in the config
        View[] views = new View[config.layers.length];
        for(int i = 0; i<config.layers.length; i+=1) {
            views[i] = config.layers[i].GetView(context, this, config, zoom, x, y);
        }
        return views;
    }

    @Override
    public void OnLayerLoadFinished(View v, DeltaMapLayer layer, int zoom, int x, int y) {
        //This is called when a layer, loaded in the above function, has finished processing.
    }

    public void OnZoom(float scale) {
        if(config == null)
            return;
        Log.d("ZOOM", "Z: "+scale);

        //Create new elements
        CreateNewTiles(scale);

        //Set opacity
        SetTileOpacity(scale);
    }

    private void CreateNewTiles(float scale) {
        //Get target
        int targetZoomLevel = (int)Math.floor(scale);

        //Get all elements on the current zoom layer
        LinkedList<MapZoomLayerView> currentLayerViews = GetAllZoomViewsOfLevel(targetZoomLevel - 1);

        //Check all
        int deployedCount = 0;
        for(MapZoomLayerView layer : currentLayerViews) {
            //If quadrants for this are already deployed, ignore
            if(layer.isQuadrantsDeployed)
                continue;

            //Check all quadrants to see if *any* are in view
            if(!layer.IsVisible())
                continue;

            //Deploy quadrants for this layer
            layer.AddChildrenZoomQuadrants();
            deployedCount++;

            Log.d("DEPLOY", layer.GetDebugString());
        }

        if(deployedCount > 0)
            Log.d("DEPLOY-COUNT", "Deployed "+deployedCount+" tiles in one batch.");
    }

    private void SetTileOpacity(float scale) {
        //Get the target zoom level
        int targetZoomLevel = (int)Math.floor(scale);

        //Find all zoom levels
        LinkedList<MapZoomLayerView> currentLayerViews = GetAllZoomViewsOfLevel(targetZoomLevel);
        LinkedList<MapZoomLayerView> lowerLayerViews = GetAllZoomViewsLessThanLevel(targetZoomLevel);
        LinkedList<MapZoomLayerView> higherLayerViews = GetAllZoomViewsLargerThanLevel(targetZoomLevel);

        //Set the opacity of elements
        for(MapZoomLayerView v : currentLayerViews) {
            float a = (float)(scale - Math.floor(scale));
            Log.d("ALPHA", "A: "+(1 - a)+"; Z: "+targetZoomLevel);
            v.SetLayersAlpha(1 - a);
        }
        for(MapZoomLayerView v : lowerLayerViews) {
            v.SetLayersAlpha(0);
        }
        for(MapZoomLayerView v : higherLayerViews) {
            v.SetLayersAlpha(1);
        }
    }

    private LinkedList<MapZoomLayerView> GetAllZoomViewsOfLevel(int level) {
        LinkedList<MapZoomLayerView> v = new LinkedList<>();
        for(MapZoomLayerView view : views) {
            if(view.depth == level)
                v.add(view);
        }
        return v;
    }

    private LinkedList<MapZoomLayerView> GetAllZoomViewsLargerThanLevel(int level) {
        LinkedList<MapZoomLayerView> v = new LinkedList<>();
        for(MapZoomLayerView view : views) {
            if(view.depth > level)
                v.add(view);
        }
        return v;
    }

    private LinkedList<MapZoomLayerView> GetAllZoomViewsLessThanLevel(int level) {
        LinkedList<MapZoomLayerView> v = new LinkedList<>();
        for(MapZoomLayerView view : views) {
            if(view.depth < level)
                v.add(view);
        }
        return v;
    }
}
