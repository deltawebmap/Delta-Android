package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StructureData implements Serializable {

    @SerializedName(value = "i")
    public Integer imageIndex;
    @SerializedName(value = "r")
    public Float rotation;
    @SerializedName(value = "x")
    public Float x;
    @SerializedName(value = "y")
    public Float y;
    @SerializedName(value = "s")
    public Float size;
    @SerializedName(value = "id")
    public Integer id;

}
