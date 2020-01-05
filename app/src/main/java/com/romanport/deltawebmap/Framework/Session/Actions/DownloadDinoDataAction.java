package com.romanport.deltawebmap.Framework.Session.Actions;

import android.widget.Toast;

import com.romanport.deltawebmap.Activities.DinoDataDialogFragment;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Dino.DinoResponseData;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.MainActivity;
import com.romanport.deltawebmap.R;

public class DownloadDinoDataAction extends DeltaServerCallback<MainActivity> {

    public String id;
    public DinoResponseData dino;
    public DinoDataDialogFragment fragment;

    @Override
    public void Run(DeltaServerSession session) throws Exception {
        try {
            dino = session.GetDinoData(id);
        } catch (Exception ex) {
            dino = null;
        }
    }

    @Override
    public void RunMainThread(final MainActivity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dino == null) {
                    Toast.makeText(activity, R.string.dino_data_dialog_error, Toast.LENGTH_LONG).show();
                    fragment.dismiss();
                } else {
                    fragment.OnFullDataDownloaded(dino);
                }
            }
        });
    }

}
