package com.romanport.deltawebmap.Framework.API.Echo.CreateSession;

import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapDisplayData;

import java.io.Serializable;

public class CreateSessionResponse implements Serializable {

    public Float dayTime;

    public String mapName;
    public ArkMapData mapData;
    public String mapBackgroundColor;
    public ArkMapDisplayData[] maps; //Displable maps

    public String href; //URL of this file. Depending on how this was loaded, this might be different from what was actually requested.

    public String endpoint_tribes_icons; //Endpoint for viewing tribes
    public String endpoint_tribes_itemsearch; //Item search endpoint
    public String endpoint_tribes_dino; //Dino endpoint
    public String endpoint_tribes_overview; //Tribe properties list
    public String endpoint_tribes_dino_stats; //Tribe dino stats
    public String endpoint_tribes_log; //Tribe log
    public String endpoint_put_dino_prefs; //Puts dino prefs
    public String endpoint_canvases; //Gets canvas list
    public String endpoint_tribes_structures; //Structures
    public String endpoint_tribes_structures_metadata; //Structure metadata
    public String endpoint_tribes_younglings; //Baby dinos

}
