package com.example.a21__void.afroturf;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.a21__void.Modules.PageTransformer;
import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.afroturf.libIX.adapter.PagesAdapter;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.ui.RichTextView;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;

import java.util.ArrayList;


public class PagesFragment extends AfroFragment implements PagesAdapter.InteractionListener {
    private static final int PID_REFRESH = 548;
    private PagesAdapter pagesAdapter;
    private OnClickListener onClickListener;
    private RichTextView txtNoSalons;
    private RecyclerView rcvPages;

    public PagesFragment() {
        // Required empty public constructor
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static PagesFragment newInstance(String param1, String param2) {
        PagesFragment fragment = new PagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SalonsManager salonsManager = SalonsManager.getInstance(this.getContext());
        this.pagesAdapter = new PagesAdapter(this.getContext(), salonsManager.getSalons());
        this.pagesAdapter.setInteractionListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent  = (RelativeLayout)inflater.inflate(R.layout.fragment_pages, container, false);
        this.rcvPages = parent.findViewById(R.id.rcv_pages);
        this.txtNoSalons = parent.findViewById(R.id.txt_no_salons);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvPages.setLayoutManager(linearLayoutManager);
        rcvPages.setAdapter(this.pagesAdapter);

        return parent;
    }


    @Override
    public void onResume() {
        super.onResume();
        this.refresh();
    }


    @Override
    public void retryProcess(int processId) {
        if(processId == PID_REFRESH)
            this.refresh();
    }

    @Override
    public void cancelProcess(int processId) {
        if(processId == PID_REFRESH)
            if(PagesFragment.this.pagesAdapter.getItemCount() == 0)
                PagesFragment.this.txtNoSalons.setVisibility(View.VISIBLE);
    }

    @Override
    public void refresh() {
        this.notifyBackgroundWorkStarted();
        if(this.pagesAdapter.getItemCount() == 0)
            this.pagesAdapter.setLoading(true);
        else
            this.txtNoSalons.setVisibility(View.INVISIBLE);

        SalonsManager.getInstance(this.getContext()).getSalons(new CacheManager.ManagerRequestListener<ArrayList<SalonAfroObject>>() {
            @Override
            public void onRespond(ArrayList<SalonAfroObject> result) {
                PagesFragment.this.pagesAdapter.setSalons(result);
                PagesFragment.this.pagesAdapter.setLoading(false);
                PagesFragment.this.notifyBackgroundWorkFinished();
                if(result.size() == 0)
                    PagesFragment.this.txtNoSalons.setVisibility(View.VISIBLE);
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                PagesFragment.this.pagesAdapter.setLoading(false);

                if(apiError.errorCode == -1){
                    PagesFragment.this.addToOnConnectionQueue(PID_REFRESH);

                    if(PagesFragment.this.pagesAdapter.getItemCount() == 0)
                        PagesFragment.this.txtNoSalons.setVisibility(View.VISIBLE);
                }else{
                    PagesFragment.this.showProcessError(PID_REFRESH, R.drawable.ic_warning, APIConstants.TITLE_COMMUNICATION_ERROR, APIConstants.MSG_NO_CONNECTION);
                }
                PagesFragment.this.notifyBackgroundWorkFinished();
            }
        });
    }

    @Override
    public void onPause() {
        this.removeAtOnConnectionQueue();
        super.onPause();
    }

    @Override
    public void onItemClick(SalonAfroObject item, int position) {
        if(this.onClickListener != null)
            this.onClickListener.onItemClick(item, position);
    }

    public void focusOnPage(SalonAfroObject selectedSalon) {
        int index = this.pagesAdapter.indexOf(selectedSalon);
        if(index >= 0)
            this.rcvPages.smoothScrollToPosition(index);
    }

    public interface OnClickListener{
        void onItemClick(SalonAfroObject salon, int position);
    }
}
