package com.example.a21__void;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;

import java.util.List;

/**
 * Created by ASANDA on 2018/12/01.
 * for Pandaphic
 */
public class BookmarkAdapter extends ArrayAdapter<BookmarkAfroObject> {

    public BookmarkAdapter(@NonNull Context context, int resource, @NonNull List<BookmarkAfroObject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.bookmark_layout, parent, false);
        }

        BookmarkAfroObject bookmark = this.getItem(position);

        ImageView imgIcon  = convertView.findViewById(R.id.img_icon);
        TextView txtTitle = convertView.findViewById(R.id.txt_title),
                txtSubTitle = convertView.findViewById(R.id.txt_subtitle);
        ImageView imgDelete = convertView.findViewById(R.id.img_delete);

        txtTitle.setText(bookmark.getName());
        txtSubTitle.setText(bookmark.getInfo());

        return convertView;
    }
}
