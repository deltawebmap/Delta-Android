package com.romanport.deltawebmap.Framework.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.R;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterHolder> {

    private List<SearchResponse> responses;
    private List<Integer> startSectionIndexes; //Used to define places where labels should be (the first position index in each section)

    public static class SearchAdapterHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public ImageView img;
        public TextView title;
        public TextView subtitle;
        public ListView children;
        public Context context;

        public SearchAdapterHolder(View v) {
            super(v);
            context = v.getContext();
            label = (TextView)v.findViewById(R.id.searchItemLabel);
            img = (ImageView)v.findViewById(R.id.resultImage);
            title = (TextView) v.findViewById(R.id.resultTitle);
            subtitle = (TextView)v.findViewById(R.id.resultSub);
            children = (ListView)v.findViewById(R.id.resultChildren);
        }

        public void SetChildren(List<SearchResponseChild> responses) {
            SearchChildrenAdapter h = new SearchChildrenAdapter(context, responses);
            children.setAdapter(h);
        }
    }

    public SearchAdapter() {
        responses = new LinkedList<>();
        startSectionIndexes = new LinkedList<>();
    }

    @Override
    public SearchAdapter.SearchAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_drawer_result, parent, false);
        SearchAdapterHolder holder = new SearchAdapterHolder(v);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchAdapterHolder holder, int position) {
        //Clear
        ImageTool.SetImageInverted(false, holder.img);
        //TODO: Clear more

        //Get the source
        SearchResponse source = responses.get(position);

        //Set the label if it is needed
        if(startSectionIndexes.contains(position)) {
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setText(source.source.GetDisplayName(holder.context));
        } else {
            holder.label.setVisibility(View.GONE);
        }

        //Update
        source.BindView(holder.context, holder);
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    //Adds new items to the list of data
    public void AddItems(List<SearchResponse> responses) {
        startSectionIndexes.add(this.responses.size());
        this.responses.addAll(responses);
        notifyDataSetChanged();
    }

    public void ClearData() {
        startSectionIndexes.clear();
        responses.clear();
        notifyDataSetChanged();
    }

}
