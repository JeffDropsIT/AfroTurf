package com.example.a21__void.Modules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Stylist;

public class StylistPreviewFragment extends Fragment implements View.OnClickListener{

    private Stylist stylist;

    public StylistPreviewFragment(){


    }

    public void setStylist(Stylist pSalon){
        this.stylist = pSalon;
    }

    public Stylist getStylist(){
        return this.stylist;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stylist_layout, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}

