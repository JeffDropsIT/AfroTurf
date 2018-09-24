package com.example.a21__void.afroturf.pkgSalon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.SalonsManager;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Salon;
import com.example.a21__void.afroturf.StylistObject;
import com.example.a21__void.afroturf.pkgStylist.StylistsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends AfroFragment implements SalonsManager.SalonsManagerCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private GeneralRecyclerAdapter.GeneralAdapterListener<SalonObject>  listener;
    private String mParam2;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StylistsFragment.
     */
    // TODO: Rename and change types and number of parameters

    private GeneralRecyclerAdapter<SalonObject> stylistAdapter;

    public static BlankFragment newInstance(GeneralRecyclerAdapter.GeneralAdapterListener<SalonObject>  listener
            , String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.listener = listener;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);

        this.stylistAdapter = new GeneralRecyclerAdapter(SalonObject.SalonObjectTemplate.class, R.layout.salon_layout, listener);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.stylistAdapter);

        return parent;
    }


    @Override
    public void onResume() {
        super.onResume();
        this.stylistAdapter.clear();
        SalonsManager.getInstance(this.getContext()).fetchSalons(this);
    }

    @Override
    public String getTitle() {
        return "Salons";
    }

    @Override
    public void onFetchSalons(SalonObject[] salonObjects) {
        this.stylistAdapter.add(salonObjects);

    }
}
