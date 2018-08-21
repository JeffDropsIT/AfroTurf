package com.example.a21__void.Modules;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.a21__void.afroturf.R;

import java.util.ArrayList;

/**
 * Created by ASANDA on 2018/07/11.
 * for Pandaphic
 */
public class SearchFragment extends AfroFragment implements View.OnClickListener {

    private SearchResultAdapter searchResultAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.searchResultAdapter = new SearchResultAdapter(getContext(), R.layout.search_result_layout, new ArrayList<SearchResultAdapter.SearchResult>());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.search_layout, container, false);

        Button btnFilters = parent.findViewById(R.id.btn_filters);
        EditText edtInput = parent.findViewById(R.id.edt_input);
        ImageButton ibtnSearch  = parent.findViewById(R.id.ibtn_search);
        ListView lstResults = parent.findViewById(R.id.lst_result);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_filters:
                Toast.makeText(getContext(),"Filters", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibtn_search:
                Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
