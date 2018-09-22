package com.example.a21__void.Modules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;

import java.util.List;

/**
 * Created by ASANDA on 2018/08/10.
 * for Pandaphic
 */
public class ListPagesAdapter extends ArrayAdapter<SalonObject> {
    public ListPagesAdapter(@NonNull Context context, int resource, @NonNull List<SalonObject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView  = LayoutInflater.from(getContext()).inflate(R.layout.salon_layout, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName),
                txtLocation = convertView.findViewById(R.id.txtLocation);

        SalonObject salonObj = this.getItem(position);
        txtName.setText(salonObj.getTitle());
       //s txtLocation.setText(salonObj.getPosition().toString());
        return convertView;
    }
}
