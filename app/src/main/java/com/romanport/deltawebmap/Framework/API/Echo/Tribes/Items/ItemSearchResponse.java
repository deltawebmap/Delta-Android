package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items;

import java.io.Serializable;
import java.util.List;

public class ItemSearchResponse implements Serializable {

    public List<ItemSearchResult> items;
    public Boolean more; //Do more exist?
    public String query;
    public Integer page_offset;
    public Integer total_item_count; //Total inventory count, even if it isn't sent on this page.
    public ItemSearchInventoryHolder inventories;

}
