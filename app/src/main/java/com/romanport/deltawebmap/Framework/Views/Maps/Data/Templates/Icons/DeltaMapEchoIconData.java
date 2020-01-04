package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Icons;

import android.graphics.PointF;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.EchoIconData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapDisplayData;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;

//Used to represent an echo icon
public class DeltaMapEchoIconData extends DeltaMapNetworkImageIconData {

    public EchoIconData data;
    public ArkMapData mapData;

    public DeltaMapEchoIconData(EchoIconData data, ArkMapData mapData) {
        this.data = data;
        this.mapData = mapData;
    }

    @Override
    public String GetImageURL(DeltaMapConfig config) {
        return data.img;
    }

    @Override
    public Boolean GetIsImageInverted(DeltaMapConfig config) {
        return false;
    }

    @Override
    public int GetBorderColor(DeltaMapConfig cfg) {
        return 0xFF000000;
    }

    @Override
    public PointF GetNormalizedPos(DeltaMapConfig cfg) {
        float x = data.location.x / mapData.captureSize;
        float y = data.location.y / mapData.captureSize;
        return new PointF(x + 0.5f, y + 0.5f);
    }
}
