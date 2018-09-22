package com.example.a21__void.Modules;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgCommon.FilterFragment;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASANDA on 2018/07/11.
 * for Pandaphic
 */
public class SearchFragment extends AfroFragment implements View.OnClickListener, TextWatcher {
    private String currentQuery = null;
    private SearchResultAdapter searchResultAdapter;
    private ServerCon serverCon;
    private Map<String, SearchResult> cacheResults = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.searchResultAdapter = new SearchResultAdapter(getContext(), R.layout.search_result_layout, new ArrayList<SearchResultAdapter.SearchResult>());
        this.serverCon = new ServerCon(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.search_layout, container, false);

        Button btnFilters = parent.findViewById(R.id.btn_filters);
        EditText edtInput = parent.findViewById(R.id.edt_input);
        ImageButton ibtnSearch  = parent.findViewById(R.id.ibtn_search);
        ListView lstResults = parent.findViewById(R.id.lst_result);

        edtInput.addTextChangedListener(this);

        lstResults.setAdapter(this.searchResultAdapter);
        btnFilters.setOnClickListener(this);
        ibtnSearch.setOnClickListener(this);

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getTitle() {
        return "Search";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_filters:
                FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
                transaction.add(R.id.rel_secondary_container, new FilterFragment(), "");
                transaction.commit();
                break;
            case R.id.ibtn_search:
                Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(currentQuery == null){
            //initSearch
            this.search(s.toString());
        }else{
            String query = s.toString();
            if(query.contains(currentQuery)) {
                this.serverCon.release();
                this.search(query);
            }
        }
    }

    private void search(String query){
        this.currentQuery = query;
        this.serverCon.HTTP(Request.Method.GET, "", 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                SearchFragment.this.currentQuery = null;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SearchFragment.this.currentQuery = null;
            }
        });
    }

    private class SearchResult{

    }
}
