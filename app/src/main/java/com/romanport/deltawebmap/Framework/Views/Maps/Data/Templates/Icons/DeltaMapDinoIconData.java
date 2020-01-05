package com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Icons;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.EchoIconData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;
import com.romanport.deltawebmap.MainActivity;

public class DeltaMapDinoIconData extends DeltaMapEchoIconData {

    private MainActivity activity;

    public DeltaMapDinoIconData(EchoIconData data, ArkMapData mapData, MainActivity activity) {
        super(data, mapData);
        this.activity = activity;
    }

    @Override
    public void OnTap() {
        activity.OpenDinoModal(data);
    }

}
