package com.romanport.deltawebmap.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romanport.deltawebmap.Framework.API.Master.AuthPreflightResponse;
import com.romanport.deltawebmap.Framework.HTTPTool;
import com.romanport.deltawebmap.MainActivity;
import com.romanport.deltawebmap.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginReturnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_return);

        try {
            //Get the requested URI
            Uri uri = getIntent().getData();
            String[] parts = uri.getPath().split("/");

            //Get the target nonce
            SharedPreferences sharedPref = getSharedPreferences("com.romanport.deltawebmap.WEB", Context.MODE_PRIVATE);
            long targetNonce = sharedPref.getLong("com.romanport.deltawebmap.NONCE_TOKEN", -1);
            if(targetNonce == -1) {
                ShowWarningDialog(R.string.login_return_warning_sub);
                return;
            }

            //Compare the nonce to the nonce passed in with the URL
            long sentNonce = Long.parseLong(parts[1]);
            if(sentNonce != targetNonce) {
                ShowWarningDialog(R.string.login_return_warning_sub);
                return;
            }

            //Run through the preflight
            BackgroundThread th = new BackgroundThread();
            th.token = parts[2];
            th.start();
        } catch (Exception ex) {
            ShowWarningDialog(R.string.login_return_warning_sub);
        }
    }

    public void ShowWarningDialog(int subResrcId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_return_warning);
        builder.setMessage(subResrcId);
        builder.setPositiveButton(R.string.login_return_warning_try_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton(R.string.login_return_warning_ignore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog.show();
    }

    public void ShowWarningDialogBGThread(final int subResrcId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowWarningDialog(subResrcId);
            }
        });
    }

    class BackgroundThread extends Thread {

        public String token;

        public void run() {
            //Request
            Request request = new Request.Builder().url("https://deltamap.net/api/auth/validate_preflight_token?id="+token).build();

            //Request
            String responseString;
            try {
                Response response = HTTPTool.GetHTTPClient().newCall(request).execute();
                if(!response.isSuccessful())
                    throw new Exception("Server did not send back a successful status code! "+response.code());
                responseString = response.body().string();
            } catch (Exception ex) {
                Log.d("TEST", "IGNORE");
                ex.printStackTrace();
                ShowWarningDialogBGThread(R.string.login_return_error);
                return;
            }

            //Decode
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            final AuthPreflightResponse ee = gson.fromJson(responseString, AuthPreflightResponse.class);

            //Set the access token and return
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Save
                    SharedPreferences sharedPref = getSharedPreferences("com.romanport.deltawebmap.WEB", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("com.romanport.deltawebmap.ACCESS_TOKEN", ee.access_token);
                    editor.commit();

                    //Launch
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
