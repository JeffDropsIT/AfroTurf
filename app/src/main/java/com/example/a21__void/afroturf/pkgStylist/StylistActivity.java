package com.example.a21__void.afroturf.pkgStylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.fragments.Gallery;
import com.example.a21__void.afroturf.fragments.Posts;
import com.example.a21__void.afroturf.fragments.Reviews;
import com.example.a21__void.afroturf.pkgStylist.pakages.ChatActivity;
import com.example.a21__void.afroturf.pkgStylist.pakages.BookingActivity;
import com.example.a21__void.afroturf.pkgStylist.pakages.ViewPagerAdapter;
import com.example.a21__void.afroturf.pkgStylist.pakages.WriteReview;

public class StylistActivity extends AppCompatActivity implements View.OnClickListener{


        Gallery gallery;
        Posts posts;
        Reviews reviews;
        MenuItem prevMenuItem;
        private ViewPager viewPager;
        private  BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                setContentView(R.layout.user_profile);


                Toolbar toolbar=findViewById(R.id.user_bar);

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(R.string.userName);


                findViewById(R.id.reviews).setOnClickListener(this);
                findViewById(R.id.btn_review).setOnClickListener(this);
                findViewById(R.id.reviews_number).setOnClickListener(this);
                findViewById(R.id.fab_booking).setOnClickListener(this);
                findViewById(R.id.btn_message).setOnClickListener(this);
                //Initializing viewPager
                viewPager=findViewById(R.id.viewpager);

                //Initializing the bottomNavigationView
                bottomNavigationView=findViewById(R.id.nav_temp);

                bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){
                case R.id.gallery:
                viewPager.setCurrentItem(0);
                break;
                case R.id.post:
                viewPager.setCurrentItem(1);
                break;
                case R.id.reviews:
                viewPager.setCurrentItem(2);
                break;

                }
                return false;
                }
                });

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){

                }

        @Override
        public void onPageSelected(int position){
                if(prevMenuItem!=null){
                prevMenuItem.setChecked(false);
                }
                else
                {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page","onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem=bottomNavigationView.getMenu().getItem(position);

                }

        @Override
        public void onPageScrollStateChanged(int state){

                }
                });

               /*  //Disable ViewPager Swipe
               viewPager.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        return true;
                    }
                });
                */

                setupViewPager(viewPager);
                }


        private void setupViewPager(ViewPager viewPager){
                ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
                gallery=new Gallery();
                posts=new Posts();
                reviews=new Reviews();
                adapter.addFragment(gallery);
                adapter.addFragment(posts);
                adapter.addFragment(reviews);
                viewPager.setAdapter(adapter);
                }

        @Override
        public boolean onCreateOptionsMenu(Menu menu){
                MenuInflater inflater=getMenuInflater();
                inflater.inflate(R.menu.user_bat_menu,menu);
                return super.onCreateOptionsMenu(menu);
                }

        @Override
        public void onClick(View v){
                switch(v.getId()){
                case R.id.reviews:
                case R.id.reviews_number:
                case R.id.btn_review:
                Log.i("WSX","onClick: pressed reviews");
                Intent intent=new Intent(this,WriteReview.class);
                startActivity(intent);
                break;
                case R.id.fab_booking:
                Log.i("WSX","ONFAB PRESSED: ");
                Intent intentBooking=new Intent(this,BookingActivity.class);
                startActivity(intentBooking);
                break;
                case R.id.btn_message:
                Log.i("WSX","Message PRESSED: ");
                Intent intentMessage=new Intent(this,ChatActivity.class);
                startActivity(intentMessage);
                break;
                }
                }

        }
