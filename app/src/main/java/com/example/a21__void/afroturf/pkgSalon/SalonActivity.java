package com.example.a21__void.afroturf.pkgSalon;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.a21__void.Modules.AfroFragmentAdapter;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgCommon.ReviewsFragment;
import com.example.a21__void.afroturf.pkgStylist.StylistsFragment;

public class SalonActivity extends AppCompatActivity {
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
        getMenuInflater().inflate(R.menu.toolbar_nav_menu, menu);
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
}
