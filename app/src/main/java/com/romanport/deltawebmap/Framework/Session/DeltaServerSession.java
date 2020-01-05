package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.romanport.deltawebmap.Framework.API.Echo.CreateSession.CreateSessionResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Dino.DinoResponseData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.IconResponseData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructuresResponse;
import com.romanport.deltawebmap.Framework.HTTPErrorHandler;
import com.romanport.deltawebmap.Framework.HTTPTool;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Queue;

public class DeltaServerSession {

    //Open
    public String id;
    public Boolean active;

    //Inner vars
    private SessionActivityConnection conn;
    private HTTPTool http;
    private DeltaServerSessionQueue queue;
    private DeltaSessionPersistentData persist;

    public DeltaServerSession(Context c, SessionActivityConnection conn, String id, HTTPErrorHandler handler, DeltaSessionPersistentData persistentData) {
        this.id = id;
        this.conn = conn;
        this.active = true;
        this.persist = persistentData;
        http = new HTTPTool(HTTPTool.GetAccessToken(c), handler);
        queue = new DeltaServerSessionQueue(this);
    }

    public void SaveBundleState(Bundle b) {
        b.putSerializable("SESSION_PERSIST", persist);
    }

    public DeltaServerSessionQueue GetQueue() {
        return queue;
    }

    public CreateSessionResponse GetSession() throws Exception {
        if(persist._session == null)
            persist._session = http.GET("https://echo-content.deltamap.net/"+id+"/create_session", CreateSessionResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return persist._session;
    }

    public TribeOverviewResponse GetOverview() throws Exception {
        if(persist._overview == null)
            persist._overview = http.GET(GetSession().endpoint_tribes_overview, TribeOverviewResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return persist._overview;
    }

    public IconResponseData GetIcons() throws Exception {
        if(persist._icons == null)
            persist._icons = http.GET(GetSession().endpoint_tribes_icons, IconResponseData.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return persist._icons;
    }

    public DinoResponseData GetDinoData(String id) throws Exception {
        return http.GET(GetSession().endpoint_tribes_dino.replace("{dino}", id), DinoResponseData.class, HTTPTool.HTTP_CALL_FOREGROUND);
    }

    public StructuresResponse GetStructures() throws Exception {
        if(persist._structures == null)
            persist._structures = http.GET(GetSession().endpoint_tribes_structures, StructuresResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return persist._structures;
    }

    public ItemSearchResponse SearchInventories(String query) throws Exception {
        String url = GetSession().endpoint_tribes_itemsearch.replace("{query}", URLEncoder.encode(query, "utf-8"));
        ItemSearchResponse response = http.GET(url, ItemSearchResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return response;
    }

    //Use this to call actions from the main thread. Should never be called inside of an action, as that will cause a deadlock!
    public <A extends Activity> void QueueAction(A a, DeltaServerCallback<A> action) {
        DeltaServerSessionAction<A> data = new DeltaServerSessionAction<A>();
        data.action = action;
        data.a = a;
        GetQueue().QueueAction(data);
    }
}
