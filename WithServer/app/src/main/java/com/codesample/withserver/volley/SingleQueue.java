package com.codesample.withserver.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingleQueue {
    private static SingleQueue instance;
    private RequestQueue requestQueue;

    private SingleQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static SingleQueue getInstance(Context context) {
        if(instance == null) instance = new SingleQueue(context);
        return instance;
    }
    public RequestQueue getRequestQueue() { return requestQueue; }
}
