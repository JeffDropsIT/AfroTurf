package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {


    private static final String PARAM_MSG = "param_msg";
    private String msg = "Loading";

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getArguments() != null){
            this.msg = this.getArguments().getString(PARAM_MSG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView =  inflater.inflate(R.layout.fragment_progress, container, false);
        ((TextView)parentView.findViewById(R.id.txt_msg)).setText(this.msg);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return parentView;
    }

    public static ProgressFragment newInstance(String msg) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }
}
