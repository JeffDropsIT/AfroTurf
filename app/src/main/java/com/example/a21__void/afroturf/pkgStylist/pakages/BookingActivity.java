package com.example.a21__void.afroturf.pkgStylist.pakages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.StylistObject;
import com.example.a21__void.afroturf.pkgCommon.DatePickerFragment;
import com.example.a21__void.afroturf.pkgCommon.GeneralPopupDialogHandler;
import com.example.a21__void.afroturf.pkgCommon.TimeSlotObject;
import com.example.a21__void.afroturf.pkgCommon.TimeslotFragment;
import com.example.a21__void.afroturf.pkgSalon.BlankFragment;
import com.example.a21__void.afroturf.pkgSalon.GeneralRecyclerAdapter;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;
import com.example.a21__void.afroturf.pkgSalon.ServicesFragment;
import com.example.a21__void.afroturf.pkgSalon.SubServiceObject;
import com.example.a21__void.afroturf.pkgStylist.StylistsFragment;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener, GeneralRecyclerAdapter.GeneralAdapterListener<SubServiceObject> {
    RelativeLayout relOverlay;
    private boolean selecting = false;

    private TextView txtService, txtSalon, txtStylist, txtDate, txtTime, txtTitle;
    private ProgressBar progressBar;

    private SalonObject selectedSalon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_holder).findViewById(R.id.toolbar);

        this.relOverlay = this.findViewById(R.id.gen_one);
        CardView crdStylist = this.findViewById(R.id.crd_stylist);
        crdStylist.setOnClickListener(this);

        this.findViewById(R.id.lin_stylist).setOnClickListener(this);
        this.findViewById(R.id.lin_salon).setOnClickListener(this);
        this.findViewById(R.id.crd_date).setOnClickListener(this);
        this.findViewById(R.id.crd_time).setOnClickListener(this);

        txtService= this.findViewById(R.id.txt_service);
        txtStylist = this.findViewById(R.id.txt_stylist);
        txtSalon = this.findViewById(R.id.txt_salon);
        txtTitle = this.findViewById(R.id.txt_title);
        txtDate = this.findViewById(R.id.txt_date);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Booking");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_bat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.crd_stylist){
            showOverlay(ServicesFragment.newInstance(this, null));
        }else if(v.getId() ==R.id.lin_stylist){
            showOverlay(StylistsFragment.newInstance(new GeneralRecyclerAdapter.GeneralAdapterListener<StylistObject>() {
                @Override
                public void onItemClick(int pos, StylistObject data) {
                    closeOverlay();
                    txtStylist.setText(data.Name);
                }
            },""));
        }else if(v.getId() ==R.id.lin_salon){
            this.showOverlay(BlankFragment.newInstance(new GeneralRecyclerAdapter.GeneralAdapterListener<SalonObject>() {
                @Override
                public void onItemClick(int pos, SalonObject data) {
                    closeOverlay();
                    selectedSalon = data;
                    txtSalon.setText(data.getTitle());
                }
            }, ""));
        }else if(v.getId() == R.id.crd_date){
            this.showOverlay(DatePickerFragment.newInstance(new DatePickerFragment.DateListener() {
                @Override
                public void onGetDate(int year, int month, int day) {
                    closeOverlay();
                    txtDate.setText(day + "/" + month + "/" + year);
                }
            }));
        }else if(v.getId() == R.id.crd_time){
            if(selectedSalon != null){
                showOverlay(TimeslotFragment.newInstance(selectedSalon, new GeneralRecyclerAdapter.GeneralAdapterListener<TimeSlotObject>() {
                    @Override
                    public void onItemClick(int pos, TimeSlotObject data) {

                    }
                }));
            }else{
                Toast.makeText(this, "Select Salon", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(selecting){
            closeOverlay();
        }else
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int pos, SubServiceObject data) {
        closeOverlay();
        txtService.setText(data.Name);
    }

    void closeOverlay(){
        relOverlay.setVisibility(View.INVISIBLE);
        selecting = false;
    }

    void showOverlay(AfroFragment fragment){
        txtTitle.setText("Select " + fragment.getTitle());
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.crd_general, fragment);
        transaction.commit();
        this.relOverlay.setVisibility(View.VISIBLE);
        selecting = true;
    }
}
