package com.example.a21__void.afroturf.pkgSalon;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.a21__void.Modules.AfroFragmentAdapter;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.fragments.ServicesFragment;
import com.example.a21__void.afroturf.pkgCommon.ReviewsFragment;
import com.example.a21__void.afroturf.fragments.StylistsFragment;
import com.example.a21__void.afroturf.pkgStylist.pakages.BookingActivity;

public class SalonActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_SALON = "eSalon";

    private SalonObject salonObject;
    private ServicesFragment servicesFragment;
    private StylistsFragment stylistsFragment;
    private ReviewsFragment reviewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        if(false){
            if(!this.getIntent().hasExtra(EXTRA_SALON))
                return;

            this.salonObject = (SalonObject)this.getIntent().getSerializableExtra(EXTRA_SALON);
        }

        this.servicesFragment = new ServicesFragment();
        this.stylistsFragment = new StylistsFragment();
        this.reviewsFragment = new ReviewsFragment();


        Toolbar toolbar = this.findViewById(R.id.toolbar);
        TabLayout tabLayout = this.findViewById(R.id.tabLayout);
        ViewPager vpgSalon = this.findViewById(R.id.vpg_salon);
        FloatingActionButton fabBooking = this.findViewById(R.id.fab_booking);
        final FloatingActionButton fabReviews = this.findViewById(R.id.fab_reviews);
        fabBooking.setOnClickListener(this);
        fabReviews.setOnClickListener(this);

        vpgSalon.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2)
                    fabReviews.setVisibility(View.VISIBLE);
                else
                    fabReviews.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Salon Name");

        AfroFragmentAdapter afroAdapter = new AfroFragmentAdapter(this.getSupportFragmentManager());
        afroAdapter.add(servicesFragment, stylistsFragment, reviewsFragment);

        vpgSalon.setAdapter(afroAdapter);
        tabLayout.setupWithViewPager(vpgSalon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_user:
                break;
            case R.id.nav_search:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab_booking:
                this.openBooking();
                break;
            case R.id.fab_reviews:
                this.showReviewDialog(view);
                break;
        }

    }

    private void showReviewDialog(View v) {
        ViewGroup parent = (ViewGroup)getLayoutInflater().inflate(R.layout.create_review_layout, null, false);
        final RatingBar ratingBar = parent.findViewById(R.id.rat_rating);
        final EditText edtMsg = parent.findViewById(R.id.edt_message);
        Button btnSubmit = parent.findViewById(R.id.btn_submit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog reviewDialog = builder.setView(parent)
                .setCancelable(true)
                .show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String msg = edtMsg.getText().toString();
                Toast.makeText(SalonActivity.this, "Rating:" + rating + "|Msg:" + msg , Toast.LENGTH_SHORT).show();
                reviewDialog.dismiss();
            }
        });




    }

    private void openBooking() {
        Intent intent = new Intent(this, BookingActivity.class);
        this.startActivity(intent);
    }
}
