package com.example.a21__void.afroturf;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.BookmarkAdapter;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.manager.BookmarkManager;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;

import java.util.ArrayList;

public class FavouriteFragment extends AfroFragment implements View.OnClickListener {
    private static final int PID_REFRESH = 231;
    private BookmarkAdapter bookmarkAdapter;
    private BookmarkManager bookmarkManager;
    private LinearLayout relEmptyBookmarks;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.bookmarkManager = (BookmarkManager)CacheManager.getManager(this.getContext(), BookmarkManager.class);
        this.bookmarkAdapter = new BookmarkAdapter(this.getContext(), R.layout.bookmark_layout, bookmarkManager.getBookmarks());

        String emptyBookmarks = "Oops looks like you don't have any Bookmarks yet.\nSalons and Stylists you like will appear hare.";

        RelativeLayout parent =  (RelativeLayout) inflater.inflate(R.layout.fragment_favourite, container, false);
        this.relEmptyBookmarks = parent.findViewById(R.id.rel_no_bookmarks);
        this.relEmptyBookmarks.setVisibility(View.INVISIBLE);
        ListView lstBookmarks = parent.findViewById(R.id.lst_bookmarks);
        TextView txtEmptyBookmarks = parent.findViewById(R.id.txt_empty);
        txtEmptyBookmarks.setText(emptyBookmarks);
        parent.findViewById(R.id.txt_refresh).setOnClickListener(this);

        lstBookmarks.setAdapter(this.bookmarkAdapter);
        return parent;
    }

    private void refreshBookmarks(){
        this.notifyBackgroundWorkStarted();
        this.bookmarkManager.getBookmarks(new CacheManager.ManagerRequestListener<ArrayList<BookmarkAfroObject>>() {
            @Override
            public void onRespond(ArrayList<BookmarkAfroObject> result) {
                if(result.size() > 0){
                    FavouriteFragment.this.relEmptyBookmarks.setVisibility(View.INVISIBLE);
                    FavouriteFragment.this.bookmarkAdapter.setBookmarks(result);
                }else{
                    FavouriteFragment.this.relEmptyBookmarks.setVisibility(View.VISIBLE);
                }
                FavouriteFragment.this.notifyBackgroundWorkFinished();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                if(apiError.errorCode == APIConstants.NETWORK_ERROR){
                    FavouriteFragment.this.showProcessError(PID_REFRESH, R.drawable.ic_no_connection, APIConstants.TITLE_NO_CONNECTION, APIConstants.MSG_NO_CONNECTION);
                }else{
                    FavouriteFragment.this.showProcessError(PID_REFRESH, R.drawable.search_icon, APIConstants.TITLE_COMMUNICATION_ERROR, APIConstants.MSG_COMMUNICATION_ERROR);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.refreshBookmarks();
    }

    @Override
    public void retryProcess(int processId) {
        if(processId == PID_REFRESH){
            this.refreshBookmarks();
        }
    }

    @Override
    public void cancelProcess(int processId) {
        this.relEmptyBookmarks.setVisibility(View.VISIBLE);
    }

    @Override
    public void refresh() {
        this.refreshBookmarks();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_refresh:
                this.relEmptyBookmarks.setVisibility(View.INVISIBLE);
                this.refreshBookmarks();
                break;
        }
    }
}
