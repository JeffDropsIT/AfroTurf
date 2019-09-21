package com.example.a21__void.afroturf;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.a21__void.afroturf.fragments.CreateSalonFragment;
import com.example.a21__void.afroturf.fragments.CreateUserFragment;
import com.example.a21__void.afroturf.fragments.ProgressFragment;
import com.example.a21__void.afroturf.fragments.SalonPickerFragment;
import com.example.a21__void.afroturf.fragments.SalonsFragment;
import com.example.a21__void.afroturf.libIX.activity.AfroActivity;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.fragment.ProcessErrorFragment;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.user.UserGeneral;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class SignUpActivity extends AfroActivity  implements CreateUserFragment.EventListener, SalonPickerFragment.InteractionListener {

    private static final String TAG_CREATE_USER = "create_user"
            , TAG_ACCOUNT_TYPE = "account_type"
            , TAG_SELECT_SALON = "select_salon"
            , TAG_CREATE_SALON = "create_salon";
    private static final String TAG_PROGRESS = "tag_progress"
            , TAG_PROCESS_ERROR = "tag_error";
    private static final int REQ_UNKNOWN = 0
            , REQ_CREATE_USER = 1
            , REQ_REGISTER_WITH_SALON = 2
            , REQ_CREATE_SALON = 3;

    private MaterialProgressBar backgroundProgressBar;

    private CreateUserFragment createUserFragment;
    private SalonsFragment salonsFragment;
    private CreateSalonFragment createSalonFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        this.backgroundProgressBar = this.findViewById(R.id.prog_background_work);

        this.createUserFragment = new CreateUserFragment();
        this.salonsFragment = new SalonsFragment();
        this.createSalonFragment = new CreateSalonFragment();

        this.createUserFragment.setEventListener(this);

        this.nextFragment(this.createUserFragment, TAG_CREATE_USER, false);
    }

    private void nextFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_left, R.anim.anim_slide_left_offscreen, android.R.anim.fade_in, R.anim.anim_slide_right)
                .add(R.id.rel_main_container, fragment, tag)
                .addToBackStack(tag);

        if(addToBackStack)
            transaction.addToBackStack("stack:" + tag);

        transaction.commit();
    }


    @Override
    public void onCreateUser(CreateUserFragment creator, UserGeneral user) {
        switch(user.getUserType()){
            case UserGeneral.USER_TYPE_GENERAL:
                setResult(RESULT_OK);
                finish();
                break;
            case UserGeneral.USER_TYPE_STYLIST:
                this.nextFragment(new SalonPickerFragment(), TAG_SELECT_SALON, false);
                break;
            case UserGeneral.USER_TYPE_OWNER:
        }
    }


    @Override
    public void onProcessError(AfroFragment parent, int processId, int resIcon, String title, String message) {
        int requestID = REQ_UNKNOWN;
        if(parent instanceof CreateUserFragment) requestID = REQ_CREATE_USER;
        //else if(parent instanceof SalonsFragment) requestID = REQ_REGISTER_WITH_SALON;

        ProcessErrorFragment processErrorFragment = ProcessErrorFragment.newInstance(requestID, processId, title, message, resIcon);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.rel_secondary_container, processErrorFragment, TAG_PROCESS_ERROR)
                .commitNow();
    }

    @Override
    public void onDoBackgroundWork() {
        this.backgroundProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishBackgroundWork() {
        this.backgroundProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress(String message) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        ProgressFragment progressFragment = ProgressFragment.newInstance(message);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rel_secondary_container, progressFragment, TAG_PROGRESS)
                .commitNow();
    }

    @Override
    public void hideProgress() {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS);
        if(fragment == null)
            return;

        this.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onRequestRetry(int requestID, int processID) {
        Fragment processErrorFragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROCESS_ERROR);
        if(processErrorFragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(processErrorFragment)
                    .commitNow();

        switch (requestID){
            case REQ_CREATE_USER:
                this.createUserFragment.retryProcess(processID);
                break;
        }
    }

    @Override
    public void onRequestCancel(int requestID, int processID) {
        Fragment processErrorFragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROCESS_ERROR);
        if(processErrorFragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(processErrorFragment)
                    .commitNow();

        switch (requestID){
            case REQ_CREATE_USER:
                this.createUserFragment.cancelProcess(processID);
                break;
        }
    }

    @Override
    public void onSignUp(UserGeneral user, SalonAfroObject salon) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onWorkModeChange(int mode) {

    }
}
