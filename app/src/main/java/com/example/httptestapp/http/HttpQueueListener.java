package com.example.httptestapp.http;

/**
 * Created by JongHunLee on 2015-04-24.
 */
public interface HttpQueueListener {

    public void request_finished(int returnCode, String result);

    public void request_failed(String result);

}
