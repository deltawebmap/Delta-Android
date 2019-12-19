package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;

public abstract class DeltaServerCallback<T, O, A extends Activity> {

    public O context;

    public abstract T Run(DeltaServerSession session, O input) throws Exception;
    public abstract void OnResponse(T response, A activity);

}
