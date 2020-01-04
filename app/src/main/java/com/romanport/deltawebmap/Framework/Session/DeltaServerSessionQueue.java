package com.romanport.deltawebmap.Framework.Session;

import android.app.Activity;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class DeltaServerSessionQueue {

    private DeltaServerSession session;
    private DeltaServerSessionQueueThread queueThread;

    public Boolean active;

    private Queue<DeltaServerSessionAction<? extends Activity>> actionQueue;

    public DeltaServerSessionQueue(DeltaServerSession session) {
        this.session = session;
        this.actionQueue = new LinkedList<>();
        this.active = false;
    }

    public void StartThread() {
        if(active)
            return;
        Log.d("DeltaServerSessionQueue", "Starting thread...");
        active = true;
        queueThread = new DeltaServerSessionQueueThread();
        queueThread.start();
    }

    public void EndThread() {
        Log.d("DeltaServerSessionQueue", "Ending thread...");
        active = false;
    }

    public class DeltaServerSessionQueueThread extends Thread {
        public void run() {
            Log.d("DeltaServerSessionQueue", "Thread started.");
            DeltaServerSessionAction<? extends Activity> action;
            while (active) {
                //Try and pop an action
                if (actionQueue.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                    continue;
                }
                action = actionQueue.remove();

                //Run
                action.RunActionAndReturn(session);
            }
            Log.d("DeltaServerSessionQueue", "Thread ended.");
        }
    }

    public <O, A extends Activity> void QueueAction(DeltaServerSessionAction<? extends Activity> action) {
        StartThread(); //Make sure it is started
        actionQueue.add(action);
    }

}
