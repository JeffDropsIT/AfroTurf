package com.example.a21__void.afroturf.libIX.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgCommon.AfroTextView;

public class ProcessErrorFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_REQUEST_ID = "arg.request_ID";
    private static final String ARG_PROCESS_ID = "arg.process_ID";
    private static final String ARG_TITLE = "arg.title";
    private static final String ARG_MESSAGE = "arg.message";
    private static final String ARG_RES_ICON = "arg.res_icon";


    private int requestID
            , processID;
    private String title
            , message;
    private int resIcon;
    private InteractionListener interactionListener;

    public ProcessErrorFragment() {
        // Required empty public constructor
    }

    public static ProcessErrorFragment newInstance(int requestID, int processID, String title, String message, int resIcon) {

        ProcessErrorFragment fragment = new ProcessErrorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_ID, requestID);
        args.putInt(ARG_PROCESS_ID, processID);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_RES_ICON, resIcon);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.requestID = this.getArguments().getInt(ProcessErrorFragment.ARG_REQUEST_ID);
            this.processID = this.getArguments().getInt(ProcessErrorFragment.ARG_PROCESS_ID);
            this.title = this.getArguments().getString(ProcessErrorFragment.ARG_TITLE);
            this.message = this.getArguments().getString(ProcessErrorFragment.ARG_MESSAGE);
            this.resIcon = this.getArguments().getInt(ProcessErrorFragment.ARG_RES_ICON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout parent =  (LinearLayout)inflater.inflate(R.layout.fragment_process_error, container, false);

        ((ImageView)parent.findViewById(R.id.img_icon)).setImageResource(this.resIcon);
        ((AfroTextView)parent.findViewById(R.id.txt_title)).setText(this.title);
        ((TextView)parent.findViewById(R.id.txt_message)).setText(this.message);

        parent.findViewById(R.id.txt_cancel).setOnClickListener(this);
        parent.findViewById(R.id.btn_retry).setOnClickListener(this);

        return parent;
    }


    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            interactionListener = (InteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancel:
                if(this.interactionListener != null)
                    this.interactionListener.onRequestCancel(this.requestID, this.processID);
                break;
            case R.id.btn_retry:
                if(this.interactionListener != null)
                    this.interactionListener.onRequestRetry(this.requestID, this.processID);
        }
    }


    public interface InteractionListener {
        void onRequestRetry(int requestID, int processID);
        void onRequestCancel(int requestID, int processID);
    }
}
