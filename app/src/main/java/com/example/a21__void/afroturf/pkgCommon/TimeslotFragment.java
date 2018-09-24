package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgSalon.GeneralRecyclerAdapter;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeslotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeslotFragment extends AfroFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private GeneralRecyclerAdapter<TimeSlotObject> timeSlotObjectGeneralRecyclerAdapter;
    private GeneralRecyclerAdapter.GeneralAdapterListener<TimeSlotObject> listener;
    SalonObject salonObject;

    public TimeslotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeslotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeslotFragment newInstance(SalonObject pSalonObject, GeneralRecyclerAdapter.GeneralAdapterListener<TimeSlotObject> listener) {
        TimeslotFragment fragment = new TimeslotFragment();
        fragment.salonObject = pSalonObject;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.timeSlotObjectGeneralRecyclerAdapter = new GeneralRecyclerAdapter<TimeSlotObject>(TimeSlotObject.TimeSlotTemplate.class, R.layout.timeslot_layout, listener);

        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.timer_pick_layout, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_time);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.timeSlotObjectGeneralRecyclerAdapter);

        return parent;    }

    @Override
    public void onResume() {
        super.onResume();
        if(salonObject != null){
            timeSlotObjectGeneralRecyclerAdapter.clear();
            int count = Math.abs(salonObject.endHour - salonObject.startHour) ;
            TimeSlotObject[] timeSlotObjects = new TimeSlotObject[count];
            for(int pos= 0; pos < count; pos++){
                timeSlotObjects[pos] = new TimeSlotObject(salonObject.startHour + pos, salonObject.startMin, salonObject.endHour, salonObject.endMin);
            }
            this.timeSlotObjectGeneralRecyclerAdapter.add(timeSlotObjects);
        }
    }

    @Override
    public String getTitle() {
        return "Time";
    }
}
