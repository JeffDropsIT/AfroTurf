package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.a21__void.afroturf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountTypeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public static final int TYPE_CUSTOMER = R.id.rbtn_customer
            , TYPE_STYLIST = R.id.rbtn_stylist
            , TYPE_SALON_MANAGER = R.id.rbtn_salon_manager;

    private int accountType = TYPE_CUSTOMER;

    public AccountTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout parentView = (RelativeLayout)inflater.inflate(R.layout.fragment_account_type_fragent, container, false);
        RadioGroup rgpTypes = parentView.findViewById(R.id.rgp_types);

        rgpTypes.setOnCheckedChangeListener(this);
        return parentView;
    }

    public int getAccountType(){ return this.accountType; }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.accountType = checkedId;
    }
}
