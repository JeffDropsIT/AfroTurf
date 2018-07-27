package com.example.a21__void.Modules;

import android.os.Handler;

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

    public static void GET(final String url, final int timeout, final ConCallback conCallback){
        final Handler handler = new Handler();
        Thread runner = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL connectionUrl = new URL(url);
                    final HttpURLConnection connection = (HttpURLConnection)connectionUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    final StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    final int statusCode = connection.getResponseCode();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            conCallback.onRespond(statusCode, connection.getContentType(), buffer.toString());
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        runner.start();
    }

    public interface ConCallback{
        void onRespond(int statusCode, String bodyType, String body);
    }
}
