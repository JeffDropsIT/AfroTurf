package com.example.a21__void.afroturf.pkgCommon;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class HolderTemplate<T> extends RecyclerView.ViewHolder {

    public HolderTemplate(ViewGroup itemView) {
        super(itemView);
    }

    public abstract T bind(Cursor cursor, int pos);
}
