package com.example.a21__void.afroturf.object;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/10/01.
 * for Pandaphic
 */
public class ReviewAfroObject extends AfroObject {
    private String reviewerName, message, creationDate, reviewId;
    private int rating;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getUID() {
        return this.reviewId;
    }

    @Override
    public void set(JsonParser parser, String json) {
        JsonObject review = parser.parse(json).getAsJsonObject();
        String reviewerName = review.get("reviewerName").getAsString(),
                msg = review.get("payload").getAsString(),
                date = review.get("created").getAsString();
        int rating = review.get("rating").getAsInt();
        this.reviewerName = reviewerName;
        this.message = msg;
        this.creationDate = date;
        this.reviewId = "@null";
        this.rating = rating;
    }

    public static class UIHandler extends AfroObject.UIHandler{
        private final TextView txtFrom, txtDate, txtMsg;
        private final RatingBar ratRating;

        public UIHandler(View itemView) {
            super(itemView);
            this.txtFrom = itemView.findViewById(R.id.txt_from);
            this.txtDate = itemView.findViewById(R.id.txt_date);
            this.txtMsg = itemView.findViewById(R.id.txt_msg);
            this.ratRating = itemView.findViewById(R.id.rat_rating);
        }

        @Override
        public void bind(AfroObject afroObject, int position) {
            ReviewAfroObject reviewAfroObject = (ReviewAfroObject)afroObject;
            this.txtFrom.setText(reviewAfroObject.reviewerName);
            this.txtDate.setText(reviewAfroObject.creationDate);
            this.txtMsg.setText(reviewAfroObject.message);
            this.ratRating.setRating(reviewAfroObject.rating);
        }

        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return ReviewAfroObject.class;
        }
    }
}
