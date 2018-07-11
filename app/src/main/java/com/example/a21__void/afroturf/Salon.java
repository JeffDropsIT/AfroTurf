package com.example.a21__void.afroturf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Salon extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_PATH = 1;
    private Button btn_path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_activity);
        findViewById(R.id.btn_find_path).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_find_path:
                Log.i("ZAQ", "onClick: clicked");
                finish();
                break;
        }
    }


}
