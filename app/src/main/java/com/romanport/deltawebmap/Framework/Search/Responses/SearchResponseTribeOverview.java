package com.romanport.deltawebmap.Framework.Search.Responses;

import android.content.Context;
import android.view.View;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewDino;
import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Search.SearchAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchChildrenAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchResponse;
import com.romanport.deltawebmap.Framework.Search.SearchResponseChild;
import com.romanport.deltawebmap.Framework.Search.SearchSource;

import java.util.LinkedList;
import java.util.List;

public class SearchResponseTribeOverview extends SearchResponse {

    public TribeOverviewDino data;

    public SearchResponseTribeOverview(SearchSource source) {
        super(source);
    }

    @Override
    public void BindView(Context c, SearchAdapter.SearchAdapterHolder v) {
        //Clear ListView
        v.children.setAdapter(null);

        //Set
        v.title.setText(data.displayName);
        v.subtitle.setText(data.classDisplayName+" - Lvl "+data.level);
        ImageTool.SetImageOnView(data.img, v.img);
        ImageTool.SetImageInverted(true, v.img);
    }
}
