package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ServicesManager;
import com.example.a21__void.afroturf.object.ServiceAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends AfroFragment implements Response.ErrorListener, Response.Listener<DevDesignRequest.DevDesignResponse>{
    private static final String PARAMS_LISTENER = "listener";
    private ProgressBar progLoading;
    private AfroObjectCursorAdapter serviceAdapter;
    private AfroObjectCursorAdapter.ItemClickListener serviceClickListener;

    public static final String DATA = "data";
    public static final String NAME = "name";


    public ServicesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheManager.CachePointer cachePointer = ServicesManager.getManager(this.getContext(), ServicesManager.class).getCachePointer();
        this.serviceAdapter = new AfroObjectCursorAdapter(cachePointer, ServiceAfroObject.UIHandler.class, R.layout.service_layout);

        if (getArguments() != null) {
            this.serviceClickListener = (AfroObjectCursorAdapter.ItemClickListener)getArguments().getSerializable(PARAMS_LISTENER);
            this.serviceAdapter.setItemClickListener(serviceClickListener);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_services, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_services);
        this.progLoading = parent.findViewById(R.id.prog_loading);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.serviceAdapter);

        return parent;
    }

    @Override
    public void requestClose(AfroFragmentCallback callback) {
        callback.onClose();
    }

    @Override
    public void onResume() {
        super.onResume();
        ServicesManager servicesManager = (ServicesManager)CacheManager.getManager(this.getContext(), ServicesManager.class);
        CacheManager.ManagerRequestListener<CacheManager> refreshListener = null;
        if(servicesManager.getCount() == 0){
            progLoading.setVisibility(View.VISIBLE);
            refreshListener = new CacheManager.ManagerRequestListener<CacheManager>() {
                @Override
                public void onRespond(CacheManager result) {
                    if(result.getCount() == 0){
                        //no salons
                    }
                    progLoading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onApiError(CacheManager.ApiError apiError) {
                    //todo error
                }
            };
        }

        servicesManager.requestRefresh(refreshListener);
    }

    @Override
    public String getTitle() {
        return "Services";
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    public static ServicesFragment newInstance(AfroObjectCursorAdapter.ItemClickListener  serviceClickListener) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS_LISTENER, serviceClickListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResponse(DevDesignRequest.DevDesignResponse response) {

    }
}