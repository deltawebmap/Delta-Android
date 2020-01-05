package com.romanport.deltawebmap.Framework.Views.MapStructures;

import android.content.Context;
import android.view.View;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructuresResponse;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.MapTileLoadCallback;

public class ARKStructureLayer extends DeltaMapLayer {

    private StructuresResponse structures;
    private ArkMapData mapData;
    private ARKStructureImageCache cache;

    public ARKStructureLayer(StructuresResponse structures, ArkMapData mapData, ARKStructureImageCache cache) {
        this.structures = structures;
        this.mapData = mapData;
        this.cache = cache;
    }

    @Override
    public View GetView(Context context, MapTileLoadCallback callback, DeltaMapConfig config, int zoom, int x, int y) {
        ARKStructureView v = new ARKStructureView(context, cache, structures, mapData, GetMaxZoom(config), zoom, x, y);
        return v;
    }

    @Override
    public int GetMaxZoom(DeltaMapConfig config) {
        return 6;
    }
}
