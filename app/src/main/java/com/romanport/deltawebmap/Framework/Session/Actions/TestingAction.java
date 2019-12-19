package com.romanport.deltawebmap.Framework.Session.Actions;

import android.util.Log;

import com.romanport.deltawebmap.Framework.HTTPTool;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSessionAction;
import com.romanport.deltawebmap.MainActivity;

public class TestingAction extends DeltaServerCallback<HTTPTool, HTTPTool, MainActivity> {

    @Override
    public HTTPTool Run(DeltaServerSession session, HTTPTool input) throws Exception {
        Log.d("TestingAction", "Ran Run");
        session.GetSession();
        return null;
    }

    @Override
    public void OnResponse(HTTPTool response, MainActivity a) {
        Log.d("TestingAction", "Ran OnResponse");
        a.OpenSearchDrawer();
    }
}
