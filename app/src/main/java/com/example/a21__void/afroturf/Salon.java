package com.example.a21__void.afroturf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.Modules.SalonsPreviewFragment;
import com.example.a21__void.Modules.StylistPreviewFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Salon extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_PATH = 1;
    private Button btn_path;

    private BottomNavigationView mMainNav;
    private ViewPager mViewPager;
    private SalonsFragementAdapter salonsFragementAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_activity);
        findViewById(R.id.btn_find_path).setOnClickListener(this);

        mViewPager = findViewById(R.id.stylist_container);
        mMainNav =  findViewById(R.id.nav_temp);
        salonsFragementAdapter = new SalonsFragementAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(salonsFragementAdapter);
        mMainNav.setOnNavigationItemSelectedListener(this);
        setmViewPager(mViewPager);



    }


    private void generateStylist(){
        Random rand = new Random();


        for(int i = 0; i < generateFakeNames().size(); i++){
            int  n = rand.nextInt(10) + 1;
            Stylist stylist = new Stylist(generateFakeNames().get(i), n);
            StylistPreviewFragment previewFragment = new StylistPreviewFragment();
            previewFragment.setStylist(stylist);

            ///to be continued
        }


    }

    private ArrayList<String> generateFakeNames(){
        

        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Karen");
        nameList.add("Kylie");
        nameList.add("Jeff");
        nameList.add("Lauren");

        return nameList;
    }


    public void setmViewPager(ViewPager viewPager){

        SalonsFragementAdapter adapter = new SalonsFragementAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.nav_explore:
                //change color once agreed on UI

                return true;
            case R.id.nav_services:
                //change color once agreed on UI
                return true;
            case R.id.nav_stylist:
                //change color once agreed on UI
                mViewPager.setCurrentItem(0);
                return true;
                default:
                    return false;
        }

    }
}
