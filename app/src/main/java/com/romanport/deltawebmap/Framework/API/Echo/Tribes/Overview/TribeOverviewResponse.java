package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview;

import java.io.Serializable;
import java.util.List;

public class TribeOverviewResponse implements Serializable {

    public List<TribeOverviewTribemates> tribemates;
    public List<TribeOverviewDino> dinos;
    public String tribeName;

}
