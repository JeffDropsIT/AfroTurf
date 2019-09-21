package com.example.a21__void.afroturf.pkgConnection;

import android.net.http.HttpResponseCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by ASANDA on 2018/09/08.
 * for Pandaphic
 */
public class DevDesignRequest extends Request<DevDesignRequest.DevDesignResponse> {
    private static final int STATUS_CODE_SUCCESS = 200;

    private final Response.Listener<DevDesignResponse> responseListener;
    private byte[] requestBody;


    public DevDesignRequest(int method, String url, @NonNull Response.Listener<DevDesignResponse> pResponseListener, @Nullable Response.ErrorListener  errorListener) {
        super(method, url, errorListener);
        this.responseListener = pResponseListener;
    }



    @Override
    public byte[] getBody() throws AuthFailureError {
        return this.requestBody;
    }



    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    protected Response<DevDesignResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseBody = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            DevDesignResponse devDesignResponse = new DevDesignResponse(response.statusCode, responseBody);

            if(response.statusCode == STATUS_CODE_SUCCESS) {
                return Response.success(devDesignResponse, HttpHeaderParser.parseCacheHeaders(response));
            }else{
                return Response.error(new VolleyError(response));
            }
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

    public void setRequestBody(String body){
        try {
            this.requestBody = (body != null) ? body.getBytes("UTF-8") : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
