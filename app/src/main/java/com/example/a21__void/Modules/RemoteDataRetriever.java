package com.example.a21__void.Modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class RemoteDataRetriever {
    private static RemoteDataRetriever instance;



    public void get(final String pUrl, final DataCallback callback){
        final android.os.Handler handler = new android.os.Handler();
        Thread runner = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(pUrl);
                    InputStream is = url.openConnection().getInputStream();
                    final StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onReceive(buffer.toString());

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
    public static RemoteDataRetriever getInstance(){
        if(instance == null)
            instance = new RemoteDataRetriever();

        return instance;
    }

    public interface DataCallback{
        void onReceive(String rawData);
    }
}
