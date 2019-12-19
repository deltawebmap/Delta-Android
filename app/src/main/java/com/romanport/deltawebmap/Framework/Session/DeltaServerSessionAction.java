package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;
import android.util.Log;

public class DeltaServerSessionAction<T, O, A extends Activity> {

    public DeltaServerCallback<T, O, A> action;
    public O context;
    public A a;

    protected void RunActionAndReturn(DeltaServerSession session) {
        //Run
        final T response;
        try {
            response = action.Run(session, context);
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
                    action.OnResponse(response, a);
                } catch (Exception ex) {
                    Log.d("RunActionAndReturn", "Action response threw an error!");
                    ex.printStackTrace();
                    return;
                }
            }
        });
    }
}
