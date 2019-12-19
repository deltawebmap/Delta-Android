package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.Inventories;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchInventory;
import com.romanport.deltawebmap.Framework.API.Entities.LocationData;

import java.io.Serializable;

public class ItemSearchInventoryStructure extends ItemSearchInventory implements Serializable {

    public Integer item_count;
    public LocationData location;

}
