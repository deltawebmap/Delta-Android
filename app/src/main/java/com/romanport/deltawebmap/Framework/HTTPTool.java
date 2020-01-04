package com.romanport.deltawebmap.Framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPTool {

    private OkHttpClient _client;
    private String _token;
    private HTTPErrorHandler _handler;

    public static final int HTTP_CALL_FOREGROUND = 0;
    public static final int HTTP_CALL_BACKGROUND = 1;
    public static final int HTTP_CALL_NO_ALERT = 2;

    public HTTPTool(String token, HTTPErrorHandler handler) {
        this._token = token;
        this._client = GetHTTPClient();
        this._handler = handler;
    }

    public <T> T GET(String url, Class<T> type, int callType) throws Exception {
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer "+_token)
                .url(url)
                .build();

        //Request
        String responseString;
        Response response;
        try {
            response = _client.newCall(request).execute();
        } catch (Exception ex) {
            Log.d("HTTPGet", "Got error while requesting server data;");
            NotifyHTTPFailure(callType, -1);
            throw ex;
        }

        if(!response.isSuccessful()) {
            NotifyHTTPFailure(callType, response.code());
            throw new Exception("Server did not send back a successful status code! " + response.code());
        }

        try {
            responseString = response.body().string();
        } catch (Exception ex) {
            Log.d("HTTPGet", "Got error while requesting server data;");
            NotifyHTTPFailure(callType, -1);
            throw ex;
        }

        //Decode
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T ee = gson.fromJson(responseString, type);
        return ee;
    }

    private void NotifyHTTPFailure(int callType, int statusCode) {
        if(statusCode == 401 || statusCode == 403) {
            _handler.OnHTTPAuthFailure();
            return;
        }
        switch(callType) {
            case HTTP_CALL_FOREGROUND:
                _handler.OnHTTPErrorForeground(statusCode);
                break;
            case HTTP_CALL_BACKGROUND:
                _handler.OnHTTPErrorBackground(statusCode);
                break;
        }
    }

    public static OkHttpClient GetHTTPClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String GetAccessToken(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("com.romanport.deltawebmap.WEB", Context.MODE_PRIVATE);
        return sharedPref.getString("com.romanport.deltawebmap.ACCESS_TOKEN", null);
    }
}
