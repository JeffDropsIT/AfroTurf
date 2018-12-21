package com.example.a21__void.afroturf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ErrorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ErrorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RES_ICON = "res_icon";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";

    private int resIcon;
    private String title;
    private String description;

    private OnFragmentInteractionListener interactionListener;

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance(int resIcon, String title, String description) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RES_ICON, resIcon);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.resIcon = getArguments().getInt(ARG_RES_ICON);
            this.title = getArguments().getString(ARG_TITLE);
            this.description = getArguments().getString(ARG_DESCRIPTION);
        }else{
            this.resIcon = R.drawable.ic_no_connection;
            this.title = "Error";
            this.description = "An unknown error occurred";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView =  inflater.inflate(R.layout.fragment_error, container, false);

        ((ImageView)parentView.findViewById(R.id.img_icon)).setImageResource(this.resIcon);
        ((TextView)parentView.findViewById(R.id.txt_title)).setText(this.title);
        ((TextView)parentView.findViewById(R.id.txt_desc)).setText(this.description);
        parentView.findViewById(R.id.txt_exit).setOnClickListener(this);
        parentView.findViewById(R.id.txt_retry).setOnClickListener(this);


        return parentView;
    }

    public void setmListener(OnFragmentInteractionListener pInteractionListener){
        this.interactionListener = pInteractionListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_retry:
                if(this.interactionListener != null)
                    this.interactionListener.onRequestRetry();
                break;
            case R.id.txt_exit:
                if(this.interactionListener != null)
                    this.interactionListener.onRequestExit();
        }
    }

    public interface OnFragmentInteractionListener {
        void onRequestRetry();
        void onRequestExit();
    }

}
