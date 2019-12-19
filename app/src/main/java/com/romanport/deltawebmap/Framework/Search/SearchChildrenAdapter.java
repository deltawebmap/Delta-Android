package com.romanport.deltawebmap.Framework.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanport.deltawebmap.R;

import java.util.List;

public class SearchChildrenAdapter extends ArrayAdapter<SearchResponseChild> {

    public List<SearchResponseChild> items;

    public SearchChildrenAdapter(Context c, List<SearchResponseChild> items) {
        super(c, R.layout.search_drawer_result_child, items);
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_drawer_result_child, null,true);
        SearchChildrenAdapterHolder holder = new SearchChildrenAdapterHolder(rowView);
        items.get(position).BindView(holder);
        return rowView;

    }

    public class SearchChildrenAdapterHolder {

        public ImageView img;
        public TextView text;

        public SearchChildrenAdapterHolder(View v) {
            img = (ImageView)v.findViewById(R.id.resultImage);
            text = (TextView) v.findViewById(R.id.resultTitle);
        }

    }
}
