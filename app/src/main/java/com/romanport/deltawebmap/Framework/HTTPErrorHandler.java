package com.romanport.deltawebmap.Framework;

public interface HTTPErrorHandler {

    void OnHTTPAuthFailure();
    void OnHTTPErrorBackground(int statusCode);
    void OnHTTPErrorForeground(int statusCode);

}
