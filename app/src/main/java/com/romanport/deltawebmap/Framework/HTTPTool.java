package com.romanport.deltawebmap.Framework;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPTool {

    private OkHttpClient _client;
    private String _token;

    public HTTPTool(String token) {
        this._token = token;
        this._client = new OkHttpClient();
    }

    public <T> T GET(String url, Class<T> type) throws Exception {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer "+_token)
                .url(url)
                .build();

        //Request
        String responseString;
        try {
            Response response = _client.newCall(request).execute();
            if(!response.isSuccessful())
                throw new Exception("Server did not send back a successful status code! "+response.code());
            responseString = response.body().string();
        } catch (Exception ex) {
            Log.d("HTTPGet", "Got error while requesting server data;");
            ex.printStackTrace();
            throw ex;
        }

        //Decode
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T ee = gson.fromJson(responseString, type);
        return ee;
    }

}
