package com.example.a21__void.Modules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.a21__void.afroturf.R;

import java.util.ArrayList;

/**
 * Created by ASANDA on 2018/07/11.
 * for Pandaphic
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

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

        ImageView imgBack = parent.findViewById(R.id.img_back),
                imgSearch = parent.findViewById(R.id.img_search);
        EditText edtInput = parent.findViewById(R.id.edt_input);
        ListView lstResults = parent.findViewById(R.id.lst_result);

        lstResults.setAdapter(this.searchResultAdapter);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);

        return parent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                this.getFragmentManager().popBackStack();
                break;
            case R.id.img_search:
                break;
        }
    }
}
