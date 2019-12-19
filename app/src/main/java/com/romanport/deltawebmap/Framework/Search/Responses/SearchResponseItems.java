package com.romanport.deltawebmap.Framework.Search.Responses;

import android.content.Context;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchInventory;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResult;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResultInventory;
import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Search.SearchAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchChildrenAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchResponse;
import com.romanport.deltawebmap.Framework.Search.SearchResponseChild;
import com.romanport.deltawebmap.Framework.Search.SearchSource;
import com.romanport.deltawebmap.R;

import java.util.LinkedList;

public class SearchResponseItems extends SearchResponse {

    public ItemSearchResponse response;
    public ItemSearchResult data;

    public SearchResponseItems(SearchSource source) {
        super(source);
    }

    @Override
    public void BindView(Context c, SearchAdapter.SearchAdapterHolder v) {
        //Set basics
        v.title.setText(data.item_displayname);
        v.subtitle.setText(c.getString(R.string.item_search_count, data.total_count, data.owner_inventories.size()));
        ImageTool.SetImageOnView(data.item_icon, v.img);
        ImageTool.SetImageInverted(false, v.img);

        //Set items
        LinkedList<SearchResponseChild> children = new LinkedList<>();
        for(ItemSearchResultInventory inventory : data.owner_inventories) {
            SearchResponseItemEntry e = new SearchResponseItemEntry();
            e.inventory = inventory;
            e.context = c;
            children.add(e);
        }
        v.SetChildren(children);
    }

    class SearchResponseItemEntry extends SearchResponseChild {

        public ItemSearchResultInventory inventory;
        public Context context;

        @Override
        public void BindView(SearchChildrenAdapter.SearchChildrenAdapterHolder h) {
            //Get inventory holder
            ItemSearchInventory holder;
            if(inventory.type == 0)
                holder = response.inventories.dinos.get(inventory.id);
            else if(inventory.type == 1)
                holder = response.inventories.structures.get(inventory.id);
            else {
                h.text.setText(R.string.item_search_error);
                return;
            }

            //Update
            h.text.setText(context.getString(R.string.item_search_result, inventory.count, holder.displayName));
            ImageTool.SetImageInverted(inventory.type == 0, h.img);
            ImageTool.SetImageOnView(holder.img, h.img);
        }
    }
}
