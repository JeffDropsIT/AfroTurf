package com.example.a21__void.afroturf;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Salon extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_PATH = 1;
    private Button btn_path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_activity);
    }

    @Override
    public void onClick(View v) {

    }


}
