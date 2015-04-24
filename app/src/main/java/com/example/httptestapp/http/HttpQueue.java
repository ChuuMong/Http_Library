package com.example.httptestapp.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PutBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

/**
 * Created by JongHunLee on 2015-04-24.
 */
public class HttpQueue implements Response.Listener<String>, Response.ErrorListener {

    //Constants
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    public final String APPLICATION_JSON = "application/json";

    //Request Parameters
    private final Context context;
    private final int returnCode;
    private final int methodType;
    private final String url;
    private final Map<String, String> paramsMap;
    private final Map<String, File> fileMap;
    private HttpQueueListener listener;

    private RequestQueue requestQueue;

    public HttpQueue(Builder builder) {
        this.context = builder.context;
        this.returnCode = builder.returnCode;
        this.methodType = builder.methodType;
        this.url = builder.url;
        this.paramsMap = builder.paramsMap;
        this.fileMap = builder.fileMap;
        this.listener = builder.listener;

        requestQueue = DefaultRequestQueueFactory.create(context);
        requestQueue.start();

        volleyer(requestQueue).settings().setAsDefault().done();
    }

    public static class Builder {

        private Context context;
        private int returnCode;
        private int methodType;
        private String url;
        private Map<String, String> paramsMap;
        private Map<String, File> fileMap;
        private HttpQueueListener listener;

        public Builder() {
            paramsMap = new HashMap<>();
            fileMap = new HashMap<>();
        }

        public Builder contxt(Context value) {
            context = value;
            return this;
        }

        public Builder returnCode(int returnCode) {
            this.returnCode = returnCode;
            return this;
        }

        public Builder methodType(int value) {
            methodType = value;
            return this;
        }

        public Builder url(String value) {
            url = value;
            return this;
        }

        public Builder addParameter(String key, String value) {
            paramsMap.put(key, value);
            return this;
        }

        public Builder setParameter(Map<String, String> value) {
            paramsMap.clear();
            paramsMap.putAll(value);
            return this;
        }

        public Builder addFile(String key, File value) {
            fileMap.put(key, value);
            return this;
        }

        public Builder listener(HttpQueueListener value) {
            listener = value;
            return this;
        }

        public HttpQueue build() {
            return new HttpQueue(this);
        }
    }

    public HttpQueue execute() {
        if (methodType == GET) {
            get();
        }
        else if (methodType == POST) {
            post();
        }
        else if (methodType == PUT) {
            put();
        }
        else if (methodType == DELETE) {
            delete();
        }

        return this;
    }

    private final String STR_ACCEPT = "Accept";

    private void get() {
        volleyer().get(url).addHeader(STR_ACCEPT, APPLICATION_JSON).withListener(this).withErrorListener(this).execute();
    }

    private void post() {
        PostBuilder postBuilder = volleyer().post(url);
        postBuilder.addHeader(STR_ACCEPT, APPLICATION_JSON);

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                postBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                postBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        postBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void put() {
        PutBuilder putBuilder = volleyer().put(url);
        putBuilder.addHeader(STR_ACCEPT, APPLICATION_JSON);

        if (!paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                putBuilder.addStringPart(key, paramsMap.get(key));
            }
        }

        if (!fileMap.isEmpty()) {
            for (String key : fileMap.keySet()) {
                putBuilder.addFilePart(key, fileMap.get(key));
            }
        }

        putBuilder.withListener(this).withErrorListener(this).execute();
    }

    private void delete() {
        volleyer().delete(url).addHeader(STR_ACCEPT, APPLICATION_JSON).withListener(this).withErrorListener(this).execute();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("HttpQueue", "onErrorResponse");
        listener.request_failed(error.getMessage());
    }

    @Override
    public void onResponse(String response) {

        Log.i("HttpQueue", String.valueOf(returnCode) + " : " + response);
        listener.request_finished(this.returnCode, response);
    }
}
