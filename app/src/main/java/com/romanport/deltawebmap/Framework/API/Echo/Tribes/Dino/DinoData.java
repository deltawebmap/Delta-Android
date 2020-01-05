package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Dino;

import com.romanport.deltawebmap.Framework.API.Entities.LocationData;

import java.io.Serializable;

public class DinoData implements Serializable {

    public Long dino_id; //this is unsigned
    public Boolean is_tamed;
    public Boolean is_female;
    public String[] colors;
    public String tamed_name;
    public String tamer_name;
    public String classname;
    public Float[] current_stats;
    public Float[] max_stats;
    public Integer[] base_levelups_applied;
    public Integer[] tamed_levelups_applied;
    public Integer base_level;
    public Integer level;
    public Boolean is_baby;
    public Float baby_age;
    public Double next_imprint_time;
    public Float imprint_quality;
    public LocationData location;
    public String status;
    public Float taming_effectiveness;
    public Boolean is_cryo;
    public String cryo_inventory_id;
    public Integer cryo_inventory_type;
    public Long cryo_inventory_itemid; //A ulong
    public Float experience_points;

}
