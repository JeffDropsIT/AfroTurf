package com.example.a21__void.afroturf.object;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/10/01.
 * for Pandaphic
 */
public class ServiceAfroObject extends AfroObject {
    public String category;
    private String name, code, imgUrl, descp;
    private int price;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUID() {
        return code;
    }

    @Override
    public void set(JsonParser parser, String json) {

    }

    public static class UIHandler extends AfroObject.UIHandler{
        public final ImageView imgService;
        public final TextView txtName, txtCat, txtPrice;

        public UIHandler(View itemView) {
            super(itemView);
            this.imgService = itemView.findViewById(R.id.img_icon);
            this.txtName = itemView.findViewById(R.id.txtName);
            this.txtCat = itemView.findViewById(R.id.txtCat);
            this.txtPrice = itemView.findViewById(R.id.txtPrice);
        }

        @Override
        public void bind(AfroObject afroObject, int position) {
            ServiceAfroObject serviceAfroObject = (ServiceAfroObject)afroObject;
            txtName.setText(serviceAfroObject.name);
            txtCat.setText(serviceAfroObject.category);
            txtPrice.setText("R" + serviceAfroObject.price);
        }

        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return ServiceAfroObject.class;
        }
    }
}
