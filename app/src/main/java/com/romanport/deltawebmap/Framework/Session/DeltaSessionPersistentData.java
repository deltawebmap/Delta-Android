package com.romanport.deltawebmap.Framework.Session;

import com.romanport.deltawebmap.Framework.API.Echo.CreateSession.CreateSessionResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.IconResponseData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructuresResponse;

import java.io.Serializable;

public class DeltaSessionPersistentData implements Serializable {

    //These should never be directly accessed
    public CreateSessionResponse _session;
    public TribeOverviewResponse _overview;
    public IconResponseData _icons;
    public StructuresResponse _structures;

}
