package com.example.a21__void.Modules;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.wang.avi.AVLoadingIndicatorView;


public class ProgressFragment extends Fragment {
    public static final String TAG = "ProgressFragment";
    private static final String ARG_MSG = "param1";
    private String progressMsg;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance(String msg) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    public Animator hide(){
        if(getView() != null){
            int width = getView().getWidth(), height = this.getView().getHeight();
            int cx = width,
                    cy = 0,
                    radius = (int)Math.sqrt(width * width + height*height);
            Animator anim = null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                anim = ViewAnimationUtils.createCircularReveal(getView(), cx, cy, radius, 0);
                anim.setDuration(1000);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ProgressFragment.this.getView().setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                Log.i(TAG, "hide: ");
            }

            return anim;
        }
        return  null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.progressMsg = getArguments().getString(ARG_MSG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.progress_layout, container, false);
        TextView txtMsg = parent.findViewById(R.id.txt_msg);
        txtMsg.setText(this.progressMsg);
        return parent;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AVLoadingIndicatorView)this.getView().findViewById(R.id.av_progress)).smoothToShow();
    }


    @Override
    public void onPause() {
        ((AVLoadingIndicatorView)this.getView().findViewById(R.id.av_progress)).smoothToHide();
        super.onPause();
    }
}
