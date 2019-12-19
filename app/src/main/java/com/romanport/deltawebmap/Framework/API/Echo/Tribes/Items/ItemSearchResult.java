package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items;

import java.io.Serializable;
import java.util.List;

public class ItemSearchResult implements Serializable {

    public String item_classname;
    public String item_displayname;
    public String item_icon;
    public Integer total_count;
    public List<ItemSearchResultInventory> owner_inventories;

}
