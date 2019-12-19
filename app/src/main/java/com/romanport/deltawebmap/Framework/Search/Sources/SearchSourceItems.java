package com.romanport.deltawebmap.Framework.Search.Sources;

import android.content.Context;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResult;
import com.romanport.deltawebmap.Framework.Search.Responses.SearchResponseItems;
import com.romanport.deltawebmap.Framework.Search.SearchAction;
import com.romanport.deltawebmap.Framework.Search.SearchResponse;
import com.romanport.deltawebmap.Framework.Search.SearchSource;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.R;

import java.util.LinkedList;
import java.util.List;

public class SearchSourceItems extends SearchSource {
    @Override
    public List<SearchResponse> FetchData(String query, SearchAction action, DeltaServerSession session) throws Exception {
        //Fetch
        ItemSearchResponse response = session.SearchInventories(query);

        //Create items
        LinkedList<SearchResponse> items = new LinkedList<>();
        for(ItemSearchResult r : response.items) {
            SearchResponseItems re = new SearchResponseItems(this);
            re.response = response;
            re.data = r;
            items.add(re);
        }

        return items;
    }

    @Override
    public String GetDisplayName(Context c) {
        return c.getString(R.string.search_label_inventory);
    }
}
