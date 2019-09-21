package com.example.a21__void.afroturf.object;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/09/30.
 * for Pandaphic
 */
public abstract class AfroObject {
    public static final int TYPE_USER = 0, TYPE_STYLIST = 1, TYPE_MANAGER = 2;

    private String token;

    public abstract String getName();
    public abstract String getUID();
    public abstract void set(JsonParser parser, JsonElement jsonElement);
    public abstract JsonElement asJson();

    public void set(JsonParser parser, String json){ this.set(parser, parser.parse(json)); }

    public static abstract class UIHandler extends RecyclerView.ViewHolder{

        public UIHandler(View itemView) {
            super(itemView);
        }

        public abstract void bind(AfroObject afroObject, int position);
        public abstract Class<? extends AfroObject> getObjectClass();
    }

    public int getType(){
        return TYPE_USER;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String pToken){
        this.token = pToken;
    }

    @Override
    public String toString() {
        return this.asJson().toString();
    }
}
