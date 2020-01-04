package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;

public abstract class DeltaServerCallback<A extends Activity> {

    public abstract void Run(DeltaServerSession session) throws Exception;
    public abstract void RunMainThread(A activity);

}
