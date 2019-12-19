package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;

public class DeltaServerSessionQueue extends Thread {

    public DeltaServerSession session;

    public void run() {
        DeltaServerSessionAction<?, ?, ? extends Activity> action;
        while(session.active) {
            //Try and pop an action
            if(session.actionQueue.isEmpty()) {
                try {Thread.sleep(100);}catch (Exception ex){}
                continue;
            }
            action = session.actionQueue.remove();

            //Run
            action.RunActionAndReturn(session);
        }
    }

}
