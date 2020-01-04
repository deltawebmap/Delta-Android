package com.romanport.deltawebmap.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.romanport.deltawebmap.Framework.API.Entities.Vector3;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Layers.DeltaMapNetworkImageLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.DeltaMapContainer;
import com.romanport.deltawebmap.R;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set up the map view
        DeltaMapContainer map = (DeltaMapContainer)findViewById(R.id.map);
        DeltaMapConfig cfg = new DeltaMapConfig();
        cfg.layers = new DeltaMapLayer[] {
                new DeltaMapNetworkImageLayer() {
                    @Override
                    public String GetImageURL(DeltaMapConfig config, int zoom, int x, int y) {
                        return "https://tile-assets.deltamap.net/extinction/v1/0/"+zoom+"/"+x+"/"+y+".png";
                    }

                    @Override
                    public int GetMaxZoom(DeltaMapConfig config) {
                        return 6;
                    }
                },
        };
        cfg.maxNativeZoom = 150;
        cfg.initialPos = new Vector3(0f, 0f, 4f);
        map.LoadConfig(cfg);
    }

    public void OnLoginBtnPress(View v) {
        //Generate our nonce to use
        Random r = new Random();
        long nonce = r.nextLong();
        while(nonce <= 0)
            nonce = r.nextLong();

        //Store nonce
        SharedPreferences sharedPref = getSharedPreferences("com.romanport.deltawebmap.WEB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("com.romanport.deltawebmap.NONCE_TOKEN", nonce);
        editor.commit();

        //Called when the user presses the login button. Open the login prompt
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://deltamap.net/api/auth/steam_auth/android_secure?nonce="+nonce));
        startActivity(browserIntent);
        finish();
    }

    public void OnPrivacyBtnPress(View v) {
        //Called when the user presses the login button. Open the login prompt
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(browserIntent);
    }
}
