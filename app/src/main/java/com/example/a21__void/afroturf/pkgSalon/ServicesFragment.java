package com.example.a21__void.afroturf.pkgSalon;


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
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends AfroFragment implements Response.ErrorListener, Response.Listener<DevDesignRequest.DevDesignResponse> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String DATA = "data";
    public static final String NAME = "name";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GeneralRecyclerAdapter subServiceAdapter;

    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
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
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_services, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_services);

        this.subServiceAdapter = new GeneralRecyclerAdapter(SubServiceObject.SubServiceTemplate.class, R.layout.service_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.subServiceAdapter);

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = ServerCon.BASE_URL + "/afroturf/" + ServerCon.DEBUG_SALON_ID + "/service/-a";
        ServerCon.getInstance(this.getContext()).HTTP(Request.Method.GET, url, 0, this, this);
    }

    @Override
    public String getTitle() {
        return "Services";
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(DevDesignRequest.DevDesignResponse response) {
        SubServiceObject[] subServiceObjects = this.parser(response.data);
        this.subServiceAdapter.add(subServiceObjects);
    }

    SubServiceObject[] parser(String data){
        JsonParser parser = new JsonParser();
        JsonObject jsonRes = parser.parse(data).getAsJsonObject();

        JsonArray services = jsonRes.getAsJsonArray(DATA).get(0).getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("services");
        ArrayList<SubServiceObject> subServiceObjects = new ArrayList<>();
        for(int pos = 0; pos < services.size(); pos++){
            JsonObject service = services.get(pos).getAsJsonObject();
            String category = service.get(NAME).getAsString();

            JsonArray subServices = service.get("subservices").getAsJsonArray();

            for(int i = 0; i < subServices.size(); i++){
                JsonObject subService = subServices.get(i).getAsJsonObject();
                subServiceObjects.add(new SubServiceObject(
                        subService.get("type").getAsString(),
                        category,
                        subService.get("code").getAsString(),
                        subService.get("url").getAsString(),
                        subService.get("description").getAsString(),
                        subService.get("price").getAsInt()
                ));
            }
        }

        return subServiceObjects.toArray(new SubServiceObject[subServiceObjects.size()]);

    }
}
