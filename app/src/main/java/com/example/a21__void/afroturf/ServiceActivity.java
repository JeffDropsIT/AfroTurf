package com.example.a21__void.afroturf;

import android.content.Intent;
import android.os.Bundle;

import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;

public class ServiceActivity extends AfroActivity {
    public static final String EXTRA_SALON_UID = "extra_salon_uid", EXTRA_PATH = "extra_path";
    public static final String PATH_HOME = "path_home", PATH_CREATE = "path_create";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        String salon_uid = this.getIntent().getStringExtra(EXTRA_SALON_UID);
        SalonsManager.getInstance(this.getApplicationContext()).get(salon_uid, new CacheManager.ManagerRequestListener<AfroObject>() {
            @Override
            public void onRespond(AfroObject result) {
                if(result == null)
                    ServiceActivity.this.init(savedInstanceState);
                else
                    ServiceActivity.this.finish();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                if(apiError.errorCode < 0)
                    ServiceActivity.this.showNetworkError("No connection",
                            ServerCon.NETWORK_ERROR_MSG,
                            new ErrorFragment.OnFragmentInteractionListener() {
                                @Override
                                public void onRequestRetry() {
                                    onCreate(savedInstanceState);
                                }

                                @Override
                                public void onRequestExit() {
                                    ServiceActivity.this.finish();
                                }
                            });
                else
                    ServiceActivity.this.finish();
            }
        });
    }

    private void init(Bundle savedInstanceState) {

    }

    @Override
    public void showIndeterminateProgress() {

    }

    @Override
    public void hideIndeterminateProgress() {

    }

    @Override
    protected int getErrorContainerId() {
        return R.id.rel_secondary_container;
    }
}
