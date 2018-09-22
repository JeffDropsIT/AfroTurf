package com.example.a21__void.afroturf.pkgStylist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.StylistObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.pkgSalon.GeneralRecyclerAdapter;
import com.example.a21__void.afroturf.pkgSalon.ServicesFragment;
import com.example.a21__void.afroturf.pkgSalon.SubServiceObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StylistsFragment extends AfroFragment implements Response.ErrorListener, Response.Listener<DevDesignRequest.DevDesignResponse> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GeneralRecyclerAdapter<StylistObject> stylistAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public StylistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StylistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StylistsFragment newInstance(String param1, String param2) {
        StylistsFragment fragment = new StylistsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);

        this.stylistAdapter = new GeneralRecyclerAdapter(StylistObject.StylistObjectTemplate.class, R.layout.stylist_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.stylistAdapter);

        return parent;
    }

    @Override
    public String getTitle() {
        return "Stylists";
    }

    @Override
    public void onResume() {
        super.onResume();
        String url =  ServerCon.BASE_URL + "/afroturf/filter/" + ServerCon.DEBUG_SALON_ID + "/stylist?query={}";
        ServerCon.getInstance(this.getContext()).HTTP(Request.Method.GET, url, 0, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(DevDesignRequest.DevDesignResponse response) {
        StylistObject[] stylistObjects = this.parse(response.data);
        this.stylistAdapter.clear();
        this.stylistAdapter.add(stylistObjects);
    }

    private StylistObject[] parse(String data) {
        JsonParser jsonParser = new JsonParser();
        JsonObject resultOrAnd = jsonParser.parse(data).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject();

        JsonArray stylists = resultOrAnd.getAsJsonArray("or").get(0).getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("stylists");

        List<StylistObject> stylistObjectLists = new ArrayList<>();
        for(int pos = 0; pos < stylists.size(); pos++){
            JsonObject stylist = stylists.get(pos).getAsJsonObject();
            int posts = stylist.get("posts").getAsInt(), followers = stylist.get("followers").getAsInt(), reviews = stylist.get("reviews").getAsInt();
            String name = stylist.get("name").getAsString();
            int rating  = stylist.get("rating").getAsInt();

            stylistObjectLists.add(new StylistObject(name, "", posts, followers, reviews, rating));
        }
        return stylistObjectLists.toArray(new StylistObject[stylistObjectLists.size()]);
    }
}
