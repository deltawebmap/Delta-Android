package com.romanport.deltawebmap.Framework.Views.Maps;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.zoom.ZoomEngine;
import com.otaliastudios.zoom.internal.movement.ZoomManager;
import com.romanport.deltawebmap.Framework.API.Entities.Vector3;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapIconData;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

public class DeltaMapContainer extends ZoomableViewGroup implements MapTileLoadCallback {

    public static float ZOOM_SCALE_FACTOR = 1.5f;

    public Context context;
    public DeltaMapConfig config;
    public int maxUsefulZoom;
    public LinkedList<DeltaMapTile> tiles;
    public DeltaMapTileHolder holder;
    public DeltaMapIconPane iconPane;
    public DeltaMapUserInterface userInterface;
    private LinkedList<Runnable> queuedActions = new LinkedList<>();

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
        tiles = new LinkedList<>();

        //Add listener
        getEngine().addListener(new ZoomEngine.Listener() {
            @Override
            public void onUpdate(@NotNull ZoomEngine zoomEngine, @NotNull Matrix matrix) {
                if(iconPane != null)
                    iconPane.requestLayout();
                ManageTiles();

                //Update location
                Log.d("MAP_MOVE_GET_GOTO", getPanX()+", "+getPanY());
                userInterface.OnMapMove(GetCenterNormalized(), getZoom());

                //Run queued actions
                LinkedList<Runnable> queue = new LinkedList<>(queuedActions);
                queuedActions.clear();
                for(Runnable r : queue)
                    r.run();
            }

            @Override
            public void onIdle(@NotNull ZoomEngine zoomEngine) {

            }
        });
    }

    public void LoadConfig(final DeltaMapConfig config, DeltaMapUserInterface userInterface) {
        //WARNING: This has undefined behavior if this is called twice. Avoid doing that.

        //Set config
        this.config = config;
        this.userInterface = userInterface;

        //Get the maximum useful zoom (the maximum zoom supported by the maximum layer)
        maxUsefulZoom = 1;
        for(DeltaMapLayer l : config.layers) {
            maxUsefulZoom = (int)Math.max(maxUsefulZoom, l.GetMaxZoom(config));
        }

        //Set params
        setMaxZoom(ZOOM_SCALE_FACTOR * config.maxNativeZoom, TYPE_ZOOM);

        //Create holder
        holder = new DeltaMapTileHolder(context, this);
        addView(holder);

        //Create icon holder
        iconPane = new DeltaMapIconPane(context, this);
        ((ViewGroup)getParent()).addView(iconPane);

        //Add icons
        for(DeltaMapIconData icd : config.icons) {
            DeltaMapIcon icon = new DeltaMapIcon(context, icd, config, this);
            iconPane.addView(icon);
        }

        //Update location
        final Vector3 pos = config.initialPos;
        queuedActions.add(new Runnable() {
            @Override
            public void run() {
                //Zoom to
                PointF f = SetCenterNormalized(pos.x, pos.y, pos.z);
            }
        });

        //Refresh
        ManageTiles();
    }

    public View[] GetDisplayImageViews(int zoom, int x, int y) {
        //Used to obtain the actual image tiles that you'll be able to see

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

    public PointF ConvertScreenPosToNormalizedPos(Point pos) {
        //Convert this to be in the zoom area space
        float x = pos.x - getScaledPanX();
        float y = pos.y - getScaledPanY();

        //Find where this lands in the total space allowed
        x /= getRealZoom();
        y /= getRealZoom();

        //Normalize this
        x /= GetMaxCanvasPixels();
        y /= GetMaxCanvasPixels();

        return new PointF(x, y);
    }

    public PointF ConvertScreenPosToTilePos(Point pos, int zoomLevel) {
        //Calculate tilesPerAxis
        int tilesPerAxis = (int)Math.pow(2, zoomLevel);

        //Get normalized
        PointF normalizedPos = ConvertScreenPosToNormalizedPos(pos);
        float x = normalizedPos.x;
        float y = normalizedPos.y;

        //Multiply this by the number of tiles to bring this into tile space
        x *= tilesPerAxis;
        y *= tilesPerAxis;

        return new PointF(x, y);
    }

    public PointF ConvertNormalizedPosToScreenPos(float x, float y) {
        //Un-Normalize this
        x *= GetMaxCanvasPixels();
        y *= GetMaxCanvasPixels();

        //Un-zoom this
        x *= getRealZoom();
        y *= getRealZoom();

        //Translate this
        x += getScaledPanX();
        y += getScaledPanY();

        return new PointF(x, y);
    }

    public int GetTileZoom() {
        //Returns the current target zoom level on the tile
        float z = getZoom() / ZOOM_SCALE_FACTOR;
        return (int)Math.floor(Math.min(config.maxNativeZoom, Math.max(0, z)));
    }

    public PointF GetCenterNormalized() {
        return ConvertScreenPosToNormalizedPos(new Point(getWidth() / 2, getHeight() / 2));
    }

    public PointF SetCenterNormalized(float x, float y, float zoom) {
        //Zoom
        zoomTo(zoom, false);

        //Un-Normalize this
        x *= GetMaxCanvasPixels();
        y *= GetMaxCanvasPixels();

        //Offset
        float z = getRealZoom();
        x -= (getWidth() / 2) / z;
        y -= (getHeight() / 2) / z;

        //Pan
        panTo(-x, -y, false);
        return new PointF(-x, -y);
    }

    public void ManageTiles() {
        //If we have no config, abort
        if(config == null)
            return;

        //Get the target zoom level based upon our zoom, then only use it if it is useful
        int targetZoom = Math.min(maxUsefulZoom, GetTileZoom());

        //Calulate some more
        int tilesPerAxis = (int)Math.pow(2, targetZoom);

        //Convert the top left and bottom right of the display so we can find tiles that are in range
        PointF min = ConvertScreenPosToTilePos(new Point(0, 0), targetZoom);
        PointF max = ConvertScreenPosToTilePos(new Point(getWidth(), getHeight()), targetZoom);

        //Add new tiles
        AddNewTiles(targetZoom, tilesPerAxis, min, max);

        //Clean old tiles
        ClearOldTiles(targetZoom, tilesPerAxis, min, max);

        //Clear tiles for zoom levels greater than this
        ClearTilesFarAway(targetZoom);
    }

    private void AddNewTiles(int targetZoom, int tilesPerAxis, PointF min, PointF max) {
        //Generate all tiles within these bounds
        int tilesGenerated = 0;
        for(int x = (int)Math.floor(min.x); x < (int)Math.ceil(max.x); x++) {
            for(int y = (int)Math.floor(min.y); y < (int)Math.ceil(max.y); y++) {
                //Ensure this is within bounds
                if(x < 0 || x >= tilesPerAxis || y < 0 || y >= tilesPerAxis)
                    continue;

                //Make sure we don't have a tile here already
                if(GetTileAtPos(x, y, targetZoom) != null)
                    continue;

                //Create
                holder.AddTile(context, x, y, targetZoom);

                //Log
                Log.d("CREATE-TILES", "Created @ "+x+", "+y);
                tilesGenerated++;
            }
        }

        //Log
        if(tilesGenerated != 0)
            Log.d("CREATE-TILES-DONE", "Created " + tilesGenerated + " tiles");
    }

    private void ClearOldTiles(int targetZoom, int tilesPerAxis, PointF min, PointF max) {
        //Calculate bounds
        int boundXMin = (int)Math.floor(min.x - 1);
        int boundXMax = (int)Math.ceil(max.x + 1);
        int boundYMin = (int)Math.floor(min.y - 1);
        int boundYMax = (int)Math.ceil(max.y + 1);

        //Loop through active tiles and remove old ones
        LinkedList<DeltaMapTile> oldTiles = new LinkedList<>();
        for(DeltaMapTile t : tiles) {
            if(t.zoom != targetZoom)
                continue;
            if(t.x > boundXMin && t.x < boundXMax)
                continue;
            if(t.y > boundYMin && t.y < boundYMax)
                continue;
            oldTiles.add(t);
        }

        //Log
        if(oldTiles.size() != 0)
            Log.d("CLEAN-TILES-DONE", "Cleaned "+oldTiles.size()+" old tiles");

        //Remove these tiles
        for(DeltaMapTile t : oldTiles) {
            holder.removeView(t);
            tiles.remove(t);
        }
    }

    private void ClearTilesFarAway(int targetZoom) {
        //Clears tiles that are a few zoom levels ahead of us

        //Find those tiles
        LinkedList<DeltaMapTile> oldTiles = new LinkedList<>();
        for(DeltaMapTile t : tiles) {
            if(t.zoom <= targetZoom + 1)
                continue;
            oldTiles.add(t);
        }

        //Remove these tiles
        for(DeltaMapTile t : oldTiles) {
            holder.removeView(t);
            tiles.remove(t);
        }
    }

    public DeltaMapTile GetTileAtPos(int x, int y, int zoom) {
        for(DeltaMapTile t : tiles) {
            if(t.x == x && t.y == y && t.zoom == zoom)
                return t;
        }
        return null;
    }

    public int GetMaxCanvasPixels() {
        return (int)Math.pow(2, maxUsefulZoom) * 256;
    }

    public interface DeltaMapUserInterface {
        void OnMapMove(PointF centerPos, float zoom);
    }
}
