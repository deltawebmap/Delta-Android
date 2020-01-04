package com.romanport.deltawebmap.Framework.Session.Actions;

import android.util.Log;

import com.romanport.deltawebmap.Framework.HTTPTool;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSessionAction;
import com.romanport.deltawebmap.MainActivity;

public class TestingAction extends DeltaServerCallback<MainActivity> {

    @Override
    public void Run(DeltaServerSession session) throws Exception {
        Log.d("TestingAction", "Ran Run");
        session.GetSession();
    }

    @Override
    public void RunMainThread(MainActivity a) {
        Log.d("TestingAction", "Ran RunMainThread");
        a.OpenSearchDrawer();
    }
}
