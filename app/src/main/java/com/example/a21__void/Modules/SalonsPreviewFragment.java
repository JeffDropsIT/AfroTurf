package com.example.a21__void.Modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Salon;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class SalonsPreviewFragment extends Fragment implements View.OnClickListener {
    private MySalon salon;

    public SalonsPreviewFragment(){

    }

    public void setSalon(MySalon pSalon){
        this.salon = pSalon;
    }

    public MySalon getSalon(){
        return this.salon;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout parentView = (RelativeLayout)inflater.inflate(R.layout.salon_layout, container, false);
        TextView txtName = parentView.findViewById(R.id.txtName);
        if(salon != null)
            txtName.setText(salon.getTitle());
        parentView.setOnClickListener(this);
        return parentView;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getContext(),Salon.class);
        startActivityForResult(intent, Salon.REQUEST_PATH);
    }
}
