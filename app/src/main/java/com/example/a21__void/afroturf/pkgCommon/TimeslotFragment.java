package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;

import java.util.Random;


public class TimeslotFragment extends AfroFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAMS_SALON = "params_salon";
    private static final String PARAMS_LISTENER = "params_listener";

    // TODO: Rename and change types of parameters
    private AfroObjectCursorAdapter timeSlotObjectGeneralRecyclerAdapter;
    private AfroObjectCursorAdapter.ItemClickListener slotClickListener;
    private SalonAfroObject salonObject;
    private TimeSlotObject[] timeSlotObjects;

    public TimeslotFragment() {
        // Required empty public constructor
    }

    public static TimeslotFragment newInstance(SalonAfroObject pSalonObject, AfroObjectCursorAdapter.ItemClickListener listener) {
        TimeslotFragment fragment = new TimeslotFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS_SALON, pSalonObject);
        args.putSerializable(PARAMS_LISTENER, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.salonObject = (SalonAfroObject)this.getArguments().getSerializable(PARAMS_SALON);
            this.slotClickListener = (AfroObjectCursorAdapter.ItemClickListener)this.getArguments().getSerializable(PARAMS_LISTENER);

           // this.salonObject.startTime = "10:00:00";
//            this.salonObject.endTime = "12:00:00";

            String[] startTimeParts = this.salonObject.startTime.split(":"), endTimeParts = this.salonObject.endTime.split(":");
            int startHour = Integer.parseInt(startTimeParts[0]), endHour = Integer.parseInt(endTimeParts[0]);

            int openHours = Math.abs(endHour - startHour);
            this.timeSlotObjects = new TimeSlotObject[openHours];

            Random random  = new Random();
            for(int pos = 0; pos < timeSlotObjects.length; pos++){
                int  numSlots = 1 + random.nextInt(2);
                TimeSlotObject.Slot[] slots = new TimeSlotObject.Slot[numSlots];

                int remainingTime = 60;
                for(int i = 0; i < slots.length; i++){
                    int duration = random.nextInt(remainingTime / 2);
                    if(i == slots.length - 1)
                        duration = remainingTime;

                    remainingTime -= duration;

                    slots[i] = new TimeSlotObject.Slot(duration, random.nextBoolean());
                    Log.i("ixas", duration + "|" + slots[i].isAvailable + "|" + (startHour + pos) % 25 + "|" + openHours + "|" + startTimeParts[0]);
                }

                this.timeSlotObjects[pos] = new TimeSlotObject((startHour + pos) % 25, slots);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        this.timeSlotObjectGeneralRecyclerAdapter = new AfroObjectCursorAdapter<TimeSlotObject>(TimeSlotObject.TimeSlotTemplate.class, R.layout.timeslot_layout, listener);

        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.timer_pick_layout, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_time);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View child = getLayoutInflater().inflate(R.layout.timeslot_layout, parent, false);
                return new TimeSlotObject.TimeSlotTemplate(child);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TimeSlotObject timeSlotObject = timeSlotObjects[position];
                ((TimeSlotObject.TimeSlotTemplate)holder).bind(timeSlotObject, position);
            }

            @Override
            public int getItemCount() {
                return timeSlotObjects.length;
            }
        });

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getTitle() {
        return "Time";
    }
}
