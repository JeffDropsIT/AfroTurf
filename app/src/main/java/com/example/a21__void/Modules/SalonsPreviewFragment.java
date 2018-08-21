package com.example.a21__void.Modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Salon;
import com.example.a21__void.afroturf.SalonObject;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class SalonsPreviewFragment extends Fragment implements View.OnClickListener {
    public static final String PARAM_SALON_OBJ = "paramsSalonObject";
    private SalonObject salonObject;
    private CardView crdSalon;

    public SalonsPreviewFragment(){

    }

    public SalonObject getSalonObject(){
        return this.salonObject;
    }

    public void setSalonObject(SalonObject pSalonObject){
        this.salonObject = pSalonObject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout parentView = (RelativeLayout)inflater.inflate(R.layout.salon_layout, container, false);
        TextView txtName = parentView.findViewById(R.id.txtName);
        this.crdSalon = parentView.findViewById(R.id.crd_salon);
        if(this.getArguments() != null){
            SalonObject salonObl = this.getSalonObject();
            txtName.setText(salonObl.getTitle());
            parentView.setOnClickListener(this);
        }
        return parentView;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getContext(),Salon.class);
        startActivityForResult(intent, Salon.REQUEST_PATH);
    }

    public static SalonsPreviewFragment newInstance(SalonObject salonObject) {
        SalonsPreviewFragment previewFragment = new SalonsPreviewFragment();
        previewFragment.setSalonObject(salonObject);
        Bundle args = new Bundle();
        previewFragment.setArguments(args);

        return previewFragment;
    }

    public CardView getCard() {
        return this.crdSalon;
    }
}
