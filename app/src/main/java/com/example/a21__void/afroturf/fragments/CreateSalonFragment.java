package com.example.a21__void.afroturf.fragments;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.a21__void.afroturf.Callback;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.ServiceActivity;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.AfroDropDown;
import com.example.a21__void.afroturf.pkgCommon.AfroEditText;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ixcoda.Time;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateSalonFragment extends SequenceFragment implements View.OnClickListener {

    private static final int REQ_CODE_LOCATION = 32;
    private static final int REQ_CODE_SERVICES = 33;
    private AfroEditText edtName;
    private AfroDropDown drpTimeStart, drpTimeClose, drpLocation, drpServices;
    private Time timeStart, timeClose;
    private SalonAfroObject.Location location;


    public CreateSalonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_create_salon, container, false);

        this.edtName = parentView.findViewById(R.id.edt_name);
        this.drpTimeStart = parentView.findViewById(R.id.drp_time_start);
        this.drpTimeClose = parentView.findViewById(R.id.drp_time_close);
        this.drpLocation = parentView.findViewById(R.id.drp_location);
        this.drpServices = parentView.findViewById(R.id.drp_services);

        this.drpTimeStart.setOnClickListener(this);
        this.drpTimeClose.setOnClickListener(this);
        this.drpLocation.setOnClickListener(this);
        this.drpServices.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void shouldProceed(Callback<Boolean> callback) {
        callback.onRespond(true);
    }

    @Override
    public String getTitle() {
        return "Create Salon";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drp_time_start:
                this.showTimePicker(new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        CreateSalonFragment.this.timeStart = new Time(hourOfDay, minute, 0);
                       //todo CreateSalonFragment.this.drpTimeStart.setSelection(timeStart.toString());
                    }
                });
                break;
            case R.id.drp_time_close:
                this.showTimePicker(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        CreateSalonFragment.this.timeClose = new Time(hourOfDay, minute, 0);
                       //todo CreateSalonFragment.this.drpTimeClose.setSelection(timeClose.toString());
                    }
                });
                break;
            case R.id.drp_location:
                try {
                    this.startActivityForResult(new PlacePicker.IntentBuilder().build(this.getActivity()), REQ_CODE_LOCATION);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.drp_services:
                Intent intent = new Intent(this.getContext(), ServiceActivity.class);

                this.startActivityForResult(intent, REQ_CODE_SERVICES);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_LOCATION){
            if(requestCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this.getContext(), data);
                if(place != null){
                    this.location = new SalonAfroObject.Location();
                    location.address = place.getAddress().toString();
                    location.latitude = (float)place.getLatLng().latitude;
                    location.longitude = (float)place.getLatLng().longitude;

                    //todo this.drpLocation.setSelection(this.location.address);
                }
            }
        }
    }

    private void showTimePicker(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, onTimeSetListener,7,0, true);
        timePickerDialog.setTitle("Select Opening Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }
}
