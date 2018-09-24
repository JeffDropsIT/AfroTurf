package com.example.a21__void.afroturf.pkgCommon;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

public class ReviewObject {
    private final String ReviewerName, Msg, CreationDate;
    private final int Rating;

    public ReviewObject(String reviewerName, String msg, String creationDate, int rating) {
        ReviewerName = reviewerName;
        Msg = msg;
        CreationDate = creationDate;
        Rating = rating;
    }

    public static class ReviewObjectTemplate extends HolderTemplate<ReviewObject>{
        private final TextView txtFrom, txtDate, txtMsg;
        private final RatingBar ratRating;

        public ReviewObjectTemplate(ViewGroup itemView) {
            super(itemView);
            this.txtFrom = itemView.findViewById(R.id.txt_from);
            this.txtDate = itemView.findViewById(R.id.txt_date);
            this.txtMsg = itemView.findViewById(R.id.txt_msg);

            this.ratRating = itemView.findViewById(R.id.rat_rating);
        }

        @Override
        public void bind(ReviewObject data, int pos) {
            this.txtFrom.setText(data.ReviewerName);
            this.txtDate.setText(data.CreationDate);
            this.txtMsg.setText(data.Msg);

            this.ratRating.setRating(data.Rating);
        }
    }
}
