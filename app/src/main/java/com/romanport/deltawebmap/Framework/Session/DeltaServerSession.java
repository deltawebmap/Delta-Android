package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;

import com.romanport.deltawebmap.Framework.API.Echo.CreateSession.CreateSessionResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Items.ItemSearchResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Overview.TribeOverviewResponse;
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

    public DeltaServerSession(SessionActivityConnection conn, String id) {
        this.id = id;
        this.conn = conn;
        this.active = true;
        http = new HTTPTool("A0CC6A9D0A14AEAE9B496BA30D5B7A9415E2E566B7568774EC4B1AB56A75AC24"); //TODO: Remove my own token from this
        queue = new DeltaServerSessionQueue(this);
    }

    public DeltaServerSessionQueue GetQueue() {
        return queue;
    }

    public CreateSessionResponse GetSession() throws Exception {
        if(_session == null)
            _session = http.GET("https://echo-content.deltamap.net/"+id+"/create_session", CreateSessionResponse.class);
        return _session;
    }

    public TribeOverviewResponse GetOverview() throws Exception {
        if(_overview == null)
            _overview = http.GET(GetSession().endpoint_tribes_overview, TribeOverviewResponse.class);
        return _overview;
    }

    public ItemSearchResponse SearchInventories(String query) throws Exception {
        String url = GetSession().endpoint_tribes_itemsearch.replace("{query}", URLEncoder.encode(query, "utf-8"));
        ItemSearchResponse response = http.GET(url, ItemSearchResponse.class);
        return response;
    }

    //Use this to call actions from the main thread. Should never be called inside of an action, as that will cause a deadlock!
    public <T, O, A extends Activity> void QueueAction(A a, O context, DeltaServerCallback<T, O, A> action) {
        DeltaServerSessionAction<T, O, A> data = new DeltaServerSessionAction<T, O, A>();
        data.context = context;
        data.action = action;
        data.a = a;
        data.action.context = context;
        GetQueue().QueueAction(data);
    }
}
