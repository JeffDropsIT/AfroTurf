package com.example.a21__void.afroturf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class UserSettingRecyclerViewAdapter extends RecyclerView.Adapter<UserSettingRecyclerViewAdapter.ViewHolder> {

    private final List<UserPathContent.UserPath> mValues;

    public UserSettingRecyclerViewAdapter(List<UserPathContent.UserPath> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_usersetting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imgIcon.setImageResource(mValues.get(position).ResIcon);
        holder.mContentView.setText(mValues.get(position).Name);
        holder.txtDesc.setText(mValues.get(position).Description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imgIcon;
        public final TextView mContentView, txtDesc;
        public UserPathContent.UserPath mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgIcon = (ImageView)view.findViewById(R.id.img_icon);
            mContentView = (TextView) view.findViewById(R.id.content);
            this.txtDesc = (TextView)view.findViewById(R.id.txt_desc);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
