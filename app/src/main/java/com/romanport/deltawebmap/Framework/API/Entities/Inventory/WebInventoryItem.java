package com.romanport.deltawebmap.Framework.API.Entities.Inventory;

import java.io.Serializable;

public class WebInventoryItem implements Serializable {

    public String classname;
    public Integer stack_size;
    public Boolean is_blueprint;
    public Boolean is_engram;
    public String item_id;
    public Float saved_durability;
    public String type;
    //extras

}
