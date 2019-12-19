package com.romanport.deltawebmap.Framework.Search;

import android.content.Context;

import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;

import java.util.List;

public abstract class SearchSource {

    //Called from inside of the network thread to download data for this part. Should hold data inside of this class
    public abstract List<SearchResponse> FetchData(String query, SearchAction action, DeltaServerSession session) throws Exception;

    public abstract String GetDisplayName(Context c);

}
