package com.example.a21__void.Modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a21__void.afroturf.R;

import java.util.List;

/**
 * Created by ASANDA on 2018/07/11.
 * for Pandaphic
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResultAdapter.SearchResult> {


    public SearchResultAdapter(@NonNull Context context, int resource, @NonNull List<SearchResult> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_layout, parent, false);
        }
        SearchResult searchResult = this.getItem(position);

        ImageView imgTpe = convertView.findViewById(R.id.img_type);
        TextView txtTitle = convertView.findViewById(R.id.txt_title),
                txtSubTitle = convertView.findViewById(R.id.txt_subtitle);

        imgTpe.setImageResource(searchResult.resType);
        txtTitle.setText(searchResult.Title);
        txtSubTitle.setText(searchResult.SubTitle);

        return convertView;
    }

    public class SearchResult{
        public final String Title, SubTitle;
        public final int resType;

        public SearchResult(String title, String subtitle, int restype){
            this.Title = title;
            this.SubTitle = subtitle;
            this.resType = restype;
        }
    }
}
