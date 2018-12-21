package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.object.SalonAfroObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmallPreviewFragment extends AfroFragment {
    private SalonAfroObject salonAfroObject = null;
    private boolean animating = false;
    private boolean isShown = false;
    private Animation animSlideIn, animSlideOut;
    private View preview = null;
    private View.OnClickListener clickListener;

    public SmallPreviewFragment() {
        // Required empty public constructor
    }

    private void showPreview(){
        if(this.preview != null && this.animSlideIn != null){
            this.animSlideIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    SmallPreviewFragment.this.animating = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.preview.setVisibility(View.VISIBLE);
            this.preview.startAnimation(this.animSlideIn);
            this.animating = true;
            this.isShown = true;
        }
    }

    private void hidePreview(final AfroFragmentCallback callback){
        if(this.preview != null && this.animSlideOut != null){
            this.animSlideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(callback != null)
                        callback.onClose();
                    SmallPreviewFragment.this.animating = false;
                    SmallPreviewFragment.this.preview.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.preview.startAnimation(this.animSlideOut);
            this.isShown = false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relParent = (RelativeLayout)inflater.inflate(R.layout.small_preview, container, false);
        this.preview = relParent.findViewById(R.id.crd_small_preview);

        this.animSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_up);
        this.animSlideOut = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_down);
        this.animSlideIn.setDuration(250);
        this.animSlideOut.setDuration(250);

        if(this.clickListener != null)
            preview.setOnClickListener(this.clickListener);

        return relParent;
    }

    @Override
    public void requestClose(final AfroFragmentCallback callback) {

        if(this.isShown){
            this.hidePreview(callback);
        }else{
            callback.onClose();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        if(this.salonAfroObject != null) {
            this.updateUI();
            this.showPreview();
        }
        else{
            if(this.isShown){
                this.hidePreview(null);
            }
        }
        super.onResume();
    }

    @Override
    public String getTitle() {
        return "Preview";
    }


    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        if(this.preview != null)
            this.preview.setOnClickListener(clickListener);
    }

    public void setSalonObject(SalonAfroObject pSalonObject){
        this.salonAfroObject = pSalonObject;
        this.updateUI();

        if(this.isShown){
            if(this.salonAfroObject == null)
                this.hidePreview(null);
        }else{
            if(this.salonAfroObject != null)
                this.showPreview();
        }
    }

    private void updateUI() {
        if(this.preview == null)
            return;

        if(this.salonAfroObject != null){
            TextView txtName = this.preview.findViewById(R.id.txt_name);
            RatingBar ratRating = this.preview.findViewById(R.id.rat_rating);

            txtName.setText(this.salonAfroObject.getName());
            ratRating.setRating(this.salonAfroObject.getRating());
        }
    }
}
