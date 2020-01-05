package com.romanport.deltawebmap.Framework.API.Entities.Inventory;

import com.romanport.deltawebmap.Framework.API.Entities.Entries.ItemEntry;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;

public class WebInventory implements Serializable {

    public LinkedList<WebInventoryItem> inventory_items;
    public HashMap<String, ItemEntry> item_class_data;

}
