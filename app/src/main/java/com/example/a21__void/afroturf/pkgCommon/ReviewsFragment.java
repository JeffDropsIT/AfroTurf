package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.StylistObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.pkgSalon.GeneralRecyclerAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends AfroFragment implements Response.ErrorListener, Response.Listener<DevDesignRequest.DevDesignResponse> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ReviewObject[] reviewObjects = new ReviewObject[]{
            new ReviewObject("Asanda IX", "Online calculators for everything. Some solve problems, some satisfy curiosity.", "10/04/2017 15:56", 4),
            new ReviewObject("Samu", "Use simple and easy free online calculator at work, at school or at home. With our calculator you can perform simple and trigonometric calculations.\n", "10/04/2017 15:56", 2),
            new ReviewObject("Sizwe", "Online scientific calculator for quick calculations, along with a large collection of free online calculators, each with related information to gain in-depth knowledge ...\n", "10/04/2017 15:56", 3),
            new ReviewObject("Jeff", "Use simple and easy free online calculator at work, at school or at home. With our calculator you can perform simple and trigonometric calculations.\n", "10/04/2017 15:56", 1),
            new ReviewObject("eggs", "Online scientific calculator for quick calculations, along with a large collection of free online calculators, each with related information to gain in-depth knowledge ...\n", "10/04/2017 15:56", 5),
    };
    private GeneralRecyclerAdapter reviewsAdapter;


    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
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

        this.reviewsAdapter = new GeneralRecyclerAdapter(ReviewObject.ReviewObjectTemplate.class, R.layout.reviews_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.reviewsAdapter);

        return parent;    }

    @Override
    public String getTitle() {
        return "Reviews";
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = ServerCon.BASE_URL + "/afroturf/reviews?userId=" + ServerCon.DEBUG_SALON_OID + "&reviewId=" + ServerCon.DEBUG_SALON_REVIEW_ID;
        ServerCon.getInstance(this.getContext()).HTTP(0, url, 0,  this, this);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(DevDesignRequest.DevDesignResponse response) {
        ReviewObject[] data = this.parse(response.data);
        this.reviewsAdapter.clear();
        this.reviewsAdapter.add(data);
    }

    private ReviewObject[] parse(String rawData) {
        JsonParser parser = new JsonParser();
        JsonArray reviews = parser.parse(rawData).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject().getAsJsonArray("reviewsIn");

        ReviewObject[] reviewObjects = new ReviewObject[reviews.size()];
        for(int pos  = 0; pos < reviews.size(); pos++){
            JsonObject review = reviews.get(pos).getAsJsonObject();

            String reviewerName = review.get("reviewerName").getAsString(),
                    msg = review.get("payload").getAsString(),
                    date = review.get("created").getAsString();
            int rating = review.get("rating").getAsInt();

            reviewObjects[pos] = new ReviewObject(reviewerName, msg, date, rating);
        }
        return reviewObjects;
    }
}
