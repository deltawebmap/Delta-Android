package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StructuresResponse implements Serializable {

    @SerializedName(value = "s")
    public StructureData[] structures;
    @SerializedName(value = "i")
    public String[] nameTable;
    @SerializedName(value = "upt")
    public Float unitsPerTile;

}
