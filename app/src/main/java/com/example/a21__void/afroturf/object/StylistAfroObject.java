package com.example.a21__void.afroturf.object;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASANDA on 2018/09/30.
 * for Pandaphic
 */

public class StylistAfroObject extends AfroObject {
    private String name, stylistUID, url;
    private int posts, followers, reviews, rating;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUID() {
        return this.stylistUID;
    }

    @Override
    public void set(JsonParser parser, String json) {
            JsonObject stylist = parser.parse(json).getAsJsonObject();
            int posts = stylist.get("posts").getAsInt(), followers = stylist.get("followers").getAsInt(), reviews = stylist.get("reviews").getAsInt();
            String name = stylist.get("name").getAsString();
            int rating = stylist.get("rating").getAsInt();
            this.name = name;
            this.posts = posts;
            this.followers = followers;
            this.rating = rating;
    }
    public static class UIHandler extends AfroObject.UIHandler {
        private final CircleImageView ImgIcon;
        private final TextView txtFollowers, txtReviews, txtName;
        private final RatingBar ratRating;
        private final int colorPrimary, colorSecondary;

        public UIHandler(View itemView) {
            super(itemView);
            this.ImgIcon = itemView.findViewById(R.id.img_profile);
            this.txtFollowers = itemView.findViewById(R.id.txt_followers);
            this.txtReviews = itemView.findViewById(R.id.txt_reviewa);
            this.txtName = itemView.findViewById(R.id.txt_name);
            this.ratRating = itemView.findViewById(R.id.rat_rating);
            this.colorPrimary = Color.parseColor("#f2f2f2");
            this.colorSecondary = Color.parseColor("#fAfAfA");
        }

        @Override
        public void bind(AfroObject afroObject, int position) {
            StylistAfroObject stylistAfroObject = (StylistAfroObject)afroObject;
            if(position % 2 == 0){
                this.itemView.setBackgroundColor(this.colorPrimary);
            }else{
                this.itemView.setBackgroundColor(this.colorSecondary);
            }

            this.txtFollowers.setText(stylistAfroObject.followers + " Followers");
            this.txtReviews.setText(stylistAfroObject.reviews + " Reviews");
            this.txtName.setText(stylistAfroObject.name);
            this.ratRating.setRating(stylistAfroObject.rating);
        }

        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return StylistAfroObject.class;
        }
    }
}
