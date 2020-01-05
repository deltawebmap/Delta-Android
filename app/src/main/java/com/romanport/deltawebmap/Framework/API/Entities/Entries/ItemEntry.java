package com.romanport.deltawebmap.Framework.API.Entities.Entries;

import com.romanport.deltawebmap.Framework.API.Entities.CharlieAsset;

import java.io.Serializable;
import java.util.HashMap;

public class ItemEntry implements Serializable {

    public String classname;
    public CharlieAsset icon;
    public Boolean hideFromInventoryDisplay;
    public Boolean useItemDurability;
    public Boolean isTekItem;
    public Boolean allowUseWhileRiding;
    public String name;
    public String description;
    public Float spoilingTime;
    public Float baseItemWeight;
    public Float useCooldownTime;
    public Float baseCraftingXP;
    public Float baseRepairingXP;
    public Integer maxItemQuantity;
    public HashMap<String, ItemEntryAddStatusValue> addStatusValues;

    public class ItemEntryAddStatusValue implements Serializable {

        public Float baseAmountToAdd;
        public Boolean percentOfMaxStatusValue;
        public Boolean percentOfCurrentStatusValue;
        public Boolean useItemQuality;
        public Boolean addOverTime;
        public Boolean setValue;
        public Boolean setAdditionalValue;
        public Float addOverTimeSpeed;
        public Float itemQualityAddValueMultiplier;

        public String statusValueType;

    }

}
