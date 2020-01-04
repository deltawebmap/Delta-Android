package com.romanport.deltawebmap.Framework.Views.Maps.Data;

import com.romanport.deltawebmap.Framework.API.Entities.Vector3;

import java.util.LinkedList;

public class DeltaMapConfig {

    public DeltaMapLayer[] layers;
    public int maxNativeZoom;
    public Vector3 initialPos;
    public int iconSize = 180;
    public LinkedList<DeltaMapIconData> icons;

}
