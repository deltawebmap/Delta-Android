package com.romanport.deltawebmap.Framework.API.Entities.Entries;

import com.romanport.deltawebmap.Framework.API.Entities.CharlieAsset;

import java.io.Serializable;
import java.util.LinkedList;

public class DinoEntry implements Serializable {

    public String screen_name;
    public Float colorizationIntensity;
    public Float babyGestationSpeed;
    public Float extraBabyGestationSpeedMultiplier;
    public Float babyAgeSpeed;
    public Float extraBabyAgeSpeedMultiplier;
    public Boolean useBabyGestation;
    public Float extraBabyAgeMultiplier;
    public DinosaurEntryStatusComponent statusComponent;
    public LinkedList<DinosaurEntryFood> adultFoods;
    public LinkedList<DinosaurEntryFood> childFoods;
    public String classname;
    public CharlieAsset icon;
    public Float[] baseLevel;
    public Float[] increasePerWildLevel;
    public Float[] increasePerTamedLevel;
    public Float[] additiveTamingBonus;
    public Float[] multiplicativeTamingBonus;
    public Float[] statImprintMult;

    public class DinosaurEntryStatusComponent implements Serializable {

        public Float baseFoodConsumptionRate;
        public Float babyDinoConsumingFoodRateMultiplier;
        public Float extraBabyDinoConsumingFoodRateMultiplier;
        public Float foodConsumptionMultiplier;
        public Float tamedBaseHealthMultiplier;

    }

    public class DinosaurEntryFood implements Serializable {

        public String classname;
        public Float foodEffectivenessMultiplier;
        public Float affinityOverride;
        public Float affinityEffectivenessMultiplier;
        public Integer foodCategory;
        public Float priority;

    }

}
