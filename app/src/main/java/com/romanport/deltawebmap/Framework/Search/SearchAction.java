package com.romanport.deltawebmap.Framework.Search;

import android.util.Log;

import com.romanport.deltawebmap.Framework.HTTPTool;
import com.romanport.deltawebmap.Framework.Search.Sources.SearchSourceItems;
import com.romanport.deltawebmap.Framework.Search.Sources.SearchSourceTribeOverview;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.MainActivity;

import java.util.LinkedList;
import java.util.List;

public class SearchAction extends DeltaServerCallback<Integer, SearchRequest, MainActivity>  {

    public List<SearchSource> sources;

    public SearchAction() {
        //Create sources
        sources = new LinkedList<>();
        sources.add(new SearchSourceTribeOverview());
        sources.add(new SearchSourceItems());
    }

    @Override
    public Integer Run(DeltaServerSession session, SearchRequest input) throws Exception {
        //Loop through sources and get results, firing them each time
        int resultCount = 0;
        for(SearchSource source : sources) {
            try {
                //Get results
                List<SearchResponse> results = source.FetchData(input.query, this, session);
                resultCount += results.size();

                //Update
                ApplyResponse(results, input);
            } catch (Exception ex) {
                Log.d("SearchAction", "Failed to process a source! Got error.");
                ex.printStackTrace();
            }
        }
        Log.d("SearchAction", "Got "+resultCount+" results.");
        return input.token;
    }

    //Checks if a token is valid and we can continue to update views
    private Boolean CheckIfTokenValid(SearchRequest input) {
        return input.activity.searchToken == input.token;
    }

    //Adds responses to the list of data displayed on-screen
    private void ApplyResponse(final List<SearchResponse> responses, final SearchRequest query) {
        query.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                query.activity.searchListViewAdapter.AddItems(responses);
            }
        });
    }

    @Override
    public void OnResponse(Integer result, MainActivity activity) {
        //TODO: Set
    }
}
