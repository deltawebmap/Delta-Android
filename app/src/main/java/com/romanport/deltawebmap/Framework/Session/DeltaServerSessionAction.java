package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;
import android.util.Log;

public class DeltaServerSessionAction<A extends Activity> {

    public DeltaServerCallback<A> action;
    public A a;

    protected void RunActionAndReturn(DeltaServerSession session) {
        //Run
        try {
            action.Run(session);
        } catch (Exception ex) {
            Log.d("RunActionAndReturn", "Action threw an error!");
            ex.printStackTrace();
            return;
        }

        //Call back
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    action.RunMainThread(a);
                } catch (Exception ex) {
                    Log.d("RunActionAndReturn", "Action response threw an error!");
                    ex.printStackTrace();
                    return;
                }
            }
        });
    }
}
