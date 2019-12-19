package com.romanport.deltawebmap.Framework.API.Entities;

import java.io.Serializable;

public class ArkMapData implements Serializable {

    public String displayName; //The name displayed
    public Boolean isOfficial; //Is official map that ships with the game
    public Boolean isStoryArk; //Is a story ark
    public String backgroundColor; //Background color around the map. Null if there is no one complete color, such as Extinction

    public Float latLonMultiplier; //To convert the Lat/Long map coordinates to UE coordinates, simply subtract 50 and multiply by the value
    public WorldBounds2D bounds; //Bounds of the map in UE coords

    public Vector2 mapImageOffset; //Offset to move the Ark position by in order for it to fit in the center of the image.
    public Integer captureSize; //Size of the captured image, in game units

    public ArkMapDisplayData[] maps; //Maps we can display

}
