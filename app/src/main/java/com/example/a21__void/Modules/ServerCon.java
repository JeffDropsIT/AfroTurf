package com.example.a21__void.Modules;

import android.content.Context;
import android.os.Handler;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ASANDA on 2018/07/26.
 * for Pandaphic
 */
public class ServerCon {
    public final static String BASE_URL = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com";
    private static final String TAG = "ServerCon";
    public static final String DEBUG_SALON_ID = "1", DEBUG_SALON_OID = "5b95231903d3825174322a50";
    public static final String DEBUG_SALON_REVIEW_ID = "5b95231c03d3825174322a53";
    public static final String DEBUG_USER_ID = "5b9644aa6fb76e2ed83a25f6";
    public static final int TIMEOUT = 1000
            ;
    public static final int ERROR_NETWORK = -1;
    public static final String NETWORK_ERROR_MSG = "wedbnll";
    private static ServerCon instance;
    private final RequestQueue queue;

    public ServerCon(Context context){
        this.queue = Volley.newRequestQueue(context);
    }

    public void HTTP(int method, final String url, final int timeout, Response.Listener<DevDesignRequest.DevDesignResponse> requestListener, Response.ErrorListener errorListener){
        DevDesignRequest devDesignRequest = new DevDesignRequest(method, url, requestListener, errorListener);
        devDesignRequest.setTag(TAG);
        this.queue.add(devDesignRequest);
    }

    public void HTTP(int method, final String url, final int timeout, Response.Listener<DevDesignRequest.DevDesignResponse> requestListener, Response.ErrorListener errorListener, JsonElement body){
        DevDesignRequest devDesignRequest = new DevDesignRequest(method, url,requestListener, errorListener);
        devDesignRequest.setTag(TAG);
        devDesignRequest.setRequestBody(body.toString());
        this.queue.add(devDesignRequest);
    }

    public void release(){
        if(this.queue != null)
            this.queue.cancelAll(TAG);
    }

    public static ServerCon getInstance(Context context){
        if(instance == null)
            instance = new ServerCon(context);

        return instance;
    }
}
