package com.example.a21__void.afroturf.pkgConnection;

import android.net.http.HttpResponseCache;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

/**
 * Created by ASANDA on 2018/09/08.
 * for Pandaphic
 */
public class DevDesignRequest extends Request<DevDesignRequest.DevDesignResponse> {
    private final Response.Listener<DevDesignResponse> responseListener;

    public DevDesignRequest(int method, String url, @NonNull Response.Listener<DevDesignResponse> pResponseListener, @Nullable Response.ErrorListener  errorListener) {
        super(method, url, errorListener);
        this.responseListener = pResponseListener;
    }

    @Override
    protected Response<DevDesignResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            DevDesignResponse devDesignResponse = new DevDesignResponse(response.statusCode, data);
            return Response.success(devDesignResponse, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(DevDesignResponse response) {
        if(responseListener != null){
            responseListener.onResponse(response);
        }
    }

    public class DevDesignResponse{
        public final int statusCode;
        public final String data;

        public DevDesignResponse(int pStatusCode, String pData){
            this.statusCode = pStatusCode;
            this.data = pData;
        }
    }
}
