package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;
import android.content.Context;

import com.romanport.deltawebmap.Framework.API.Echo.CreateSession.CreateSessionResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.IconResponseData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewResponse;
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

    //These should never be directly accessed
    private CreateSessionResponse _session;
    private TribeOverviewResponse _overview;

    public DeltaServerSession(Context c, SessionActivityConnection conn, String id, HTTPErrorHandler handler) {
        this.id = id;
        this.conn = conn;
        this.active = true;
        http = new HTTPTool(HTTPTool.GetAccessToken(c), handler);
        queue = new DeltaServerSessionQueue(this);
    }

    public DeltaServerSessionQueue GetQueue() {
        return queue;
    }

    public CreateSessionResponse GetSession() throws Exception {
        if(_session == null)
            _session = http.GET("https://echo-content.deltamap.net/"+id+"/create_session", CreateSessionResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return _session;
    }

    public TribeOverviewResponse GetOverview() throws Exception {
        if(_overview == null)
            _overview = http.GET(GetSession().endpoint_tribes_overview, TribeOverviewResponse.class, HTTPTool.HTTP_CALL_FOREGROUND);
        return _overview;
    }

    public IconResponseData GetIcons() throws Exception {
        return http.GET(GetSession().endpoint_tribes_icons, IconResponseData.class, HTTPTool.HTTP_CALL_FOREGROUND);
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
