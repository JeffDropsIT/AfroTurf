package com.example.a21__void.afroturf;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.a21__void.Modules.ListPagesAdapter;
import com.example.a21__void.Modules.PageTransformer;
import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.Modules.SalonsPreviewFragment;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;

import java.util.ArrayList;


public class PagesFragment extends Fragment implements View.OnClickListener {
    private static final int STATE_HORIZONTAL = 0, STATE_VERTICAL = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int currentState = STATE_HORIZONTAL;

    private SalonsFragementAdapter pagesAdapter;
    private ListPagesAdapter listPagesAdapter;

    private ViewPager vpgPages;
    private ListView lstPages;
    private FloatingActionButton fabSwitch;


    public PagesFragment() {
        // Required empty public constructor
    }
    public static PagesFragment newInstance(String param1, String param2) {
        PagesFragment fragment = new PagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.pagesAdapter = new SalonsFragementAdapter(this.getActivity().getSupportFragmentManager());
        this.listPagesAdapter = new ListPagesAdapter(this.getContext(), R.layout.salon_layout, new ArrayList<SalonObject>());

        //Toast.makeText(this.getContext(), "Pages Created...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent  = (RelativeLayout)inflater.inflate(R.layout.fragment_pages, container, false);
        this.vpgPages = parent.findViewById(R.id.vpg_pages);
        this.lstPages = parent.findViewById(R.id.lst_pages);

        this.fabSwitch = parent.findViewById(R.id.fab_switch);

        fabSwitch.setOnClickListener(this);
        PageTransformer fragmentCardShadowTransformer = new PageTransformer(vpgPages, pagesAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        this.vpgPages.setAdapter(this.pagesAdapter);
        this.vpgPages.setPageTransformer(false, fragmentCardShadowTransformer);
        this.vpgPages.setOffscreenPageLimit(3);

        this.lstPages.setAdapter(this.listPagesAdapter);
        //this.listPagesAdapter.addAll(SalonsManager.getInstance(getContext()).getSalons());

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        PageTransformer fragmentCardShadowTransformer = new PageTransformer(vpgPages, pagesAdapter);
        fragmentCardShadowTransformer.enableScaling(true);



        SalonObject[] salonObjects = new SalonObject[0];//SalonsManager.getInstance(getContext()).getSalons();

        this.pagesAdapter.clear();
        Log.i("pages", salonObjects.length + "");
        for (int pos = 0; pos < salonObjects.length; pos++) {
            this.pagesAdapter.Add(SalonsPreviewFragment.newInstance(salonObjects[pos]));
        }
        this.pagesAdapter.notifyDataSetChanged();

        this.vpgPages.setAdapter(this.pagesAdapter);
        this.vpgPages.setPageTransformer(false, fragmentCardShadowTransformer);
        this.vpgPages.setOffscreenPageLimit(3);
    }

    public void addPage(SalonObject salonObject){
        SalonsPreviewFragment previewFragment = SalonsPreviewFragment.newInstance(salonObject);
        this.pagesAdapter.Add(previewFragment);
        this.pagesAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if(currentState == STATE_HORIZONTAL){

            this.vpgPages.setVisibility(View.INVISIBLE);
            this.lstPages.setVisibility(View.VISIBLE);
            this.fabSwitch.setImageResource(R.drawable.ic_view_horizontal);
            currentState = STATE_VERTICAL;
        }else if(currentState == STATE_VERTICAL){

            this.vpgPages.setVisibility(View.VISIBLE);
            this.lstPages.setVisibility(View.INVISIBLE);
            this.fabSwitch.setImageResource(R.drawable.ic_view_vertical);
            currentState = STATE_HORIZONTAL;
        }
    }
}
