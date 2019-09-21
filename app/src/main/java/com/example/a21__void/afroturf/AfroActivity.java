package com.example.a21__void.afroturf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by ASANDA on 2018/12/02.
 * for Pandaphic
 */
public abstract class AfroActivity extends AppCompatActivity {
    private static final String TAG_ERROR_FRAGMENT = "tag_error_fragment";

    public abstract void showIndeterminateProgress();
    public abstract void hideIndeterminateProgress();

    protected abstract int getErrorContainerId();
    public void showNetworkError(String title, String msg, final ErrorFragment.OnFragmentInteractionListener callback){
        ErrorFragment errorFragment = ErrorFragment.newInstance(R.drawable.ic_no_connection, title, msg);
        errorFragment.setmListener(new ErrorFragment.OnFragmentInteractionListener() {
            @Override
            public void onRequestRetry() {
                AfroActivity.this.hideNetworkError();
                callback.onRequestRetry();
            }

            @Override
            public void onRequestExit() {
                AfroActivity.this.hideNetworkError();
                callback.onRequestExit();
            }
        });

        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(this.getErrorContainerId(), errorFragment, TAG_ERROR_FRAGMENT)
                .commit();

        this.getSupportFragmentManager().executePendingTransactions();
    }

    public void hideNetworkError(){
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_ERROR_FRAGMENT);
        if(fragment != null){
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment)
                    .commit();

            this.getSupportFragmentManager().executePendingTransactions();
        }

    }
  
}
