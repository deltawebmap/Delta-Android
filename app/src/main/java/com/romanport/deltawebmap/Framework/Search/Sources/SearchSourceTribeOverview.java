package com.romanport.deltawebmap.Framework.Search.Sources;

import android.content.Context;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewDino;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewResponse;
import com.romanport.deltawebmap.Framework.Search.Responses.SearchResponseTribeOverview;
import com.romanport.deltawebmap.Framework.Search.SearchAction;
import com.romanport.deltawebmap.Framework.Search.SearchResponse;
import com.romanport.deltawebmap.Framework.Search.SearchSource;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.R;

import java.util.LinkedList;
import java.util.List;

public class SearchSourceTribeOverview extends SearchSource {

    @Override
    public List<SearchResponse> FetchData(String query, SearchAction action, DeltaServerSession session) throws Exception {
        //Find all results
        LinkedList<SearchResponse> responses = new LinkedList<>();
        TribeOverviewResponse overview = session.GetOverview();
        for(TribeOverviewDino dino : overview.dinos) {
            //Check if query matches
            if(query.length() > 0 && !dino.displayName.toLowerCase().contains(query) && !dino.classDisplayName.toLowerCase().contains(query))
                continue;

            //Add
            SearchResponseTribeOverview r = new SearchResponseTribeOverview(this);
            r.data = dino;
            responses.add(r);
        }
        return responses;
    }

    @Override
    public String GetDisplayName(Context c) {
        return c.getString(R.string.search_label_dinos);
    }

}
