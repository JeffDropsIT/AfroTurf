package com.example.a21__void.afroturf;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a21__void.afroturf.pkgCommon.HolderTemplate;
import com.example.a21__void.afroturf.pkgStylist.StylistActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASANDA on 2018/07/23.
 * for Pandaphic
 */
public class StylistObject {
    public final String Name, Url;
    public final int Posts, Followers, Reviews, Rating;

    public StylistObject(String name, String url, int posts, int followers, int reviews, int rating) {
        Name = name;
        Url = url;
        Posts = posts;
        Followers = followers;
        Reviews = reviews;
        Rating = rating;
    }

    public static class StylistObjectTemplate extends HolderTemplate<StylistObject> implements View.OnClickListener {
        private final CircleImageView ImgIcon;
        private final TextView txtPosts, txtFollowers, txtReviews, txtName;
        private final RatingBar ratRating;

        public StylistObjectTemplate(ViewGroup itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Log.i("sdc", "StylistObjectTemplate: ");
            this.ImgIcon = itemView.findViewById(R.id.img_profile);

            this.txtPosts = itemView.findViewById(R.id.txt_post);
            this.txtFollowers = itemView.findViewById(R.id.txt_followers);
            this.txtReviews = itemView.findViewById(R.id.txt_reviewa);
            this.txtName = itemView.findViewById(R.id.txt_name);

            this.ratRating = itemView.findViewById(R.id.rat_rating);

        }

        @Override
        public void bind(StylistObject data) {
            this.txtPosts.setText("" + data.Posts);
            this.txtFollowers.setText("" + data.Followers);
            this.txtReviews.setText("" + data.Reviews);

            this.txtName.setText(data.Name);

            this.ratRating.setRating(data.Rating);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), StylistActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
