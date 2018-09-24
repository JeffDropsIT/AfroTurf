package com.example.a21__void.afroturf.pkgCommon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class HolderTemplate<T> extends RecyclerView.ViewHolder {

    public HolderTemplate(ViewGroup itemView) {
        super(itemView);
    }

    public abstract void bind(T data, int pos);
}
