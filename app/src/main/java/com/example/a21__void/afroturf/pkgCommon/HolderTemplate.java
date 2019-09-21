package com.example.a21__void.afroturf.pkgCommon;

import android.database.Cursor;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public abstract class HolderTemplate<T> extends RecyclerView.ViewHolder {

    public HolderTemplate(ViewGroup itemView) {
        super(itemView);
    }

    public abstract T bind(Cursor cursor, int pos);
}
