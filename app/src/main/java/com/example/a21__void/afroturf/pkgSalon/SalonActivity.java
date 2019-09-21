package com.example.a21__void.afroturf.pkgSalon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.a21__void.Modules.AfroFragmentAdapter;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.AfroActivity;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.fragments.ServicesFragment;
import com.example.a21__void.afroturf.manager.BookmarkManager;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.ReviewsFragment;
import com.example.a21__void.afroturf.fragments.StylistsFragment;
import com.example.a21__void.afroturf.pkgStylist.pakages.BookingActivity;
import com.example.a21__void.afroturf.user.UserGeneral;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SalonActivity extends AfroActivity implements View.OnClickListener {
    public static final String EXTRA_SALON = "eSalon";

    private final BookmarkManager bookmarkManager = (BookmarkManager)CacheManager.getManager(this, BookmarkManager.class);

    private SalonAfroObject salonObject;
    private ServicesFragment servicesFragment;
    private StylistsFragment stylistsFragment;
    private ReviewsFragment reviewsFragment;

    private MaterialProgressBar progBackgroundWork;
    private ImageView imgBookmark;

    private String bookmarkUID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        if(true){
            if(!this.getIntent().hasExtra(EXTRA_SALON))
                return;

            this.salonObject = (SalonAfroObject)this.getIntent().getSerializableExtra(EXTRA_SALON);
        }

        this.servicesFragment = new ServicesFragment();
        this.stylistsFragment = new StylistsFragment();
        this.reviewsFragment = new ReviewsFragment();


        this.progBackgroundWork = this.findViewById(R.id.prog_background_work);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        TabLayout tabLayout = this.findViewById(R.id.tabLayout);
        ViewPager vpgSalon = this.findViewById(R.id.vpg_salon);
        FloatingActionButton fabBooking = this.findViewById(R.id.fab_booking);
        TextView txtAddress = this.findViewById(R.id.txt_address);
        this.imgBookmark = this.findViewById(R.id.img_bookmark);
        final FloatingActionButton fabReviews = this.findViewById(R.id.fab_reviews);


        this.progBackgroundWork.setVisibility(View.INVISIBLE);
        txtAddress.setText(this.salonObject.location.address);
        fabBooking.setOnClickListener(this);
        fabReviews.setOnClickListener(this);
        imgBookmark.setOnClickListener(this);

        vpgSalon.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //TODO visibility
                //if(position == 2)
                   // fabReviews.setVisibility(View.VISIBLE);
                //else
                    //fabReviews.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(this.salonObject.getName());

        AfroFragmentAdapter afroAdapter = new AfroFragmentAdapter(this.getSupportFragmentManager());
        afroAdapter.add(servicesFragment, stylistsFragment, reviewsFragment);

        vpgSalon.setAdapter(afroAdapter);
        tabLayout.setupWithViewPager(vpgSalon);

        this.showIndeterminateProgress();
        this.bookmarkManager.isBookmarked(this.salonObject.getUID(), new CacheManager.ManagerRequestListener<String>() {
            @Override
            public void onRespond(String pBookmarkUID) {
                SalonActivity.this.bookmarkUID = pBookmarkUID;
                imgBookmark.setImageResource((pBookmarkUID != null) ? R.drawable.ic_bookmark_selected : R.drawable.ic_bookmark_deselected_light);
                SalonActivity.this.hideIndeterminateProgress();
                Log.i("TGIT 2", "onRespond: " + bookmarkUID);
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                //todo error
            }
        });
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
            case R.id.img_bookmark:
                boolean bookmarked = (this.bookmarkUID != null);
                imgBookmark.setImageResource(bookmarked ? R.drawable.ic_bookmark_deselected_light : R.drawable.ic_bookmark_selected);
                if(this.bookmarkUID == null)
                    this.bookmarkSalon(this.salonObject);
                else
                    this.removeSalonBookmark(this.bookmarkUID);
                break;
        }

    }

    private void removeSalonBookmark(final String bookmarkUID) {
        this.showIndeterminateProgress();
        this.bookmarkManager.removeBookmark(bookmarkUID, new CacheManager.ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean isRemoved) {
                if(isRemoved){
                    SalonActivity.this.bookmarkUID = null;
                    imgBookmark.setImageResource(R.drawable.ic_bookmark_deselected_light);
                }else{
                    imgBookmark.setImageResource(R.drawable.ic_bookmark_selected);
                }
                Log.i("TGIT 2", "onRespond: " + isRemoved + "|" + bookmarkUID);
                SalonActivity.this.hideIndeterminateProgress();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                //todo error
            }
        });
    }

    private void bookmarkSalon(SalonAfroObject salonObject) {
        this.showIndeterminateProgress();
        UserGeneral userGeneral = UserManager.getInstance(this.getApplicationContext()).getCurrentUser();
        Log.i("TGIF", userGeneral.getUUID());
        this.bookmarkManager.addBookmark(salonObject, userGeneral.getUUID(), new CacheManager.ManagerRequestListener<BookmarkAfroObject>() {
            @Override
            public void onRespond(BookmarkAfroObject result) {
                if(result != null){
                    imgBookmark.setImageResource(R.drawable.ic_bookmark_selected);// ?  : R.drawable.ic_bookmark_deselected_light);
                    SalonActivity.this.bookmarkUID = result.getUID();
                }else{
                    imgBookmark.setImageResource(R.drawable.ic_bookmark_deselected_light);// ?  : R.drawable.ic_bookmark_deselected_light);
                    SalonActivity.this.bookmarkUID = null;
                }
                SalonActivity.this.hideIndeterminateProgress();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {

            }
        });
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

    @Override
    public void showIndeterminateProgress() {
        if(this.progBackgroundWork != null && this.progBackgroundWork.getVisibility() != View.VISIBLE)
            this.progBackgroundWork.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideIndeterminateProgress() {
        if(this.progBackgroundWork != null && this.progBackgroundWork.getVisibility() != View.INVISIBLE)
            this.progBackgroundWork.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getErrorContainerId() {
        return R.id.rel_secondary_container;
    }


}
