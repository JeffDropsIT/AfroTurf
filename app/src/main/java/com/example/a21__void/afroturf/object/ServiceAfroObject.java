package com.example.a21__void.afroturf.object;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/10/01.
 * for Pandaphic
 */
public class ServiceAfroObject extends AfroObject {
    public String category;
    private String type, code, imgUrl, description;
    private int price;

    @Override
    public String getName() {
        return type;
    }

    @Override
    public String getUID() {
        return code;
    }

    @Override
    public void set(JsonParser parser, String json) {
        this.set(parser, parser.parse(json));
    }

    @Override
    public void set(JsonParser parser, JsonElement jsonElement) {
        JsonObject service = jsonElement.getAsJsonObject();

        this.category = service.get("category").getAsString();
        this.type = service.get("type").getAsString();
        this.code = service.get("code").getAsString();
        this.description = service.get("description").getAsString();
        this.price = service.get("price").getAsInt();

    }

    @Override
    public JsonElement asJson() {
        JsonObject service = new JsonObject();

        service.addProperty("category", this.category);
        service.addProperty("type", this.type);
        service.addProperty("code", this.code);
        service.addProperty("description", this.description);
        service.addProperty("price", this.price);

        return  service;
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
            txtName.setText(serviceAfroObject.type);
            txtCat.setText(serviceAfroObject.category);
            txtPrice.setText("R" + serviceAfroObject.price);
        }

        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return ServiceAfroObject.class;
        }
    }
}
