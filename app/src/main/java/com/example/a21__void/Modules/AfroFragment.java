package com.example.a21__void.Modules;


import android.support.v4.app.Fragment;

/**
 * Created by ASANDA on 2018/08/21.
 * for Pandaphic
 */
public abstract class AfroFragment extends Fragment {
    private AfroFragmentCallback afroFragmentCallback;


    @Override
    public void onResume() {
        super.onResume();
        if(this.afroFragmentCallback != null){
            this.afroFragmentCallback.onShow();
        }
    }

    @Override
    public void onPause() {
        if(this.afroFragmentCallback != null){
            this.afroFragmentCallback.onClose();
        }
        super.onPause();
    }

    public void setAfroFragmentCallback(AfroFragmentCallback pAfroFragmentCallback){
        this.afroFragmentCallback = pAfroFragmentCallback;
    }

    public AfroFragmentCallback getAfroFragmentCallback(){ return this.afroFragmentCallback; }

    public interface AfroFragmentCallback{
        void onShow();
        void onClose();
    }
}
