package com.romanport.deltawebmap.Framework.Search;

import android.content.Context;
import android.view.View;

import java.util.List;

public abstract class SearchResponse {

    public SearchSource source;

    public abstract void BindView(Context c, SearchAdapter.SearchAdapterHolder v);

    public SearchResponse(SearchSource source) {
        this.source = source;
    }

}
