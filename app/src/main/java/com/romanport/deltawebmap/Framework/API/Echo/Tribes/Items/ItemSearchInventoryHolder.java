package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items;

import com.google.gson.annotations.SerializedName;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.Inventories.ItemSearchInventoryDino;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.Inventories.ItemSearchInventoryStructure;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;

public class ItemSearchInventoryHolder implements Serializable {

    @SerializedName("0")
    public HashMap<String, ItemSearchInventoryDino> dinos;

    @SerializedName("1")
    public HashMap<String, ItemSearchInventoryStructure> structures;

}
