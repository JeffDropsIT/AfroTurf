package com.example.a21__void.afroturf.pkgSalon;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgCommon.HolderTemplate;

/**
 * Created by ASANDA on 2018/09/22.
 * for Pandaphic
 */
public class SubServiceObject {
    public final String Name, Category, Code, ImgUrl, Descp;
    public final int Price;

    public SubServiceObject(String name, String category, String code, String imgUrl, String descp, int price) {
        Name = name;
        Category = category;
        Code = code;
        ImgUrl = imgUrl;
        Descp = descp;
        Price = price;
    }

    public static class SubServiceTemplate extends HolderTemplate<SubServiceObject> {
        public final ImageView imgService;
        public final TextView txtName, txtCat, txtPrice;

        public SubServiceTemplate(ViewGroup parent){
            super(parent);
            this.imgService = parent.findViewById(R.id.img_icon);
            this.txtName = parent.findViewById(R.id.txtName);
            this.txtCat = parent.findViewById(R.id.txtCat);
            this.txtPrice = parent.findViewById(R.id.txtPrice);

        }

        @Override
        public void bind(SubServiceObject subServiceObject) {
            txtName.setText(subServiceObject.Name);
            txtCat.setText(subServiceObject.Category);
            txtPrice.setText("R" + subServiceObject.Price);
        }
    }
}
