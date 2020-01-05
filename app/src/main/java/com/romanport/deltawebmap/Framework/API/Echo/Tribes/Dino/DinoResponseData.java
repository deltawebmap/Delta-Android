package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Dino;

import com.romanport.deltawebmap.Framework.API.Entities.Entries.DinoEntry;
import com.romanport.deltawebmap.Framework.API.Entities.Inventory.WebInventory;
import com.romanport.deltawebmap.Framework.API.Entities.SavedDinoPrefs;

import java.io.Serializable;

public class DinoResponseData implements Serializable {

    public DinoData dino;
    public WebInventory inventory;
    public DinoEntry dino_entry;
    public SavedDinoPrefs prefs;
    public String dino_id;

}
