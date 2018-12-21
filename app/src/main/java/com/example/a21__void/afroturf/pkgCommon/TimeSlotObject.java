package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.object.AfroObject;
import com.google.android.gms.common.util.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.internal.StringJoin;
import com.ixcoda.StringsUtils;

import java.util.jar.Attributes;

public class TimeSlotObject extends AfroObject {
    public final int startHour;
    public final Slot[] slots;

    public TimeSlotObject(int startHour, Slot... pSlots) {
        this.startHour = startHour;
        this.slots = pSlots;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getUID() {
        return "";
    }

    @Override
    public void set(JsonParser parser, JsonElement jsonElement) {

    }

    @Override
    public JsonElement asJson() {
        return new JsonObject();
    }

    @Override
    public void set(JsonParser parser, String json) {

    }

    public static class Slot{
        public final int Duration;
        public final boolean isAvailable;

        public Slot(int duration, boolean pIsAvailable){
            this.Duration = duration;
            this.isAvailable = pIsAvailable;
        }
    }

    public static class TimeSlotTemplate extends AfroObject.UIHandler{
        TextView txtStart;
        LinearLayout linTimeSlot;

        public TimeSlotTemplate(View itemView) {
            super(itemView);
            this.txtStart =itemView.findViewById(R.id.txt_start_time);
            this.linTimeSlot = itemView.findViewById(R.id.lin_time_slot);
        }

        @Override
        public void bind(AfroObject afroObject, int position) {
            TimeSlotObject timeSlotObject = (TimeSlotObject)afroObject;
            txtStart.setText(StringsUtils.padStart(String.valueOf(timeSlotObject.startHour), 2, '0') + ":00");
            Context context = itemView.getContext();

            float density = context.getResources().getDisplayMetrics().density;

            int sectionColor = Color.parseColor("#DDE4E4");
            linTimeSlot.removeAllViews();
            for(int pos = 0; pos < timeSlotObject.slots.length; pos++){
                Slot slot = timeSlotObject.slots[pos];

                SectionProgess sectionProgess = new SectionProgess(itemView.getContext());
                sectionProgess.setAngleDeg(30);
                sectionProgess.setSectionColor(sectionColor);
                sectionProgess.setSectionSpacing(20);
                sectionProgess.setSectionThickness(2);
                sectionProgess.setSection(slot.isAvailable);
                sectionProgess.setText(!slot.isAvailable ? "●" : String.valueOf(slot.Duration));
                sectionProgess.setSectionTextSize(15 * density);
                sectionProgess.setSectionTextColor(Color.parseColor("#5b5b5b"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                float weight = 1 - (slot.Duration / 60f);
                Log.i("ixs", "W:" + weight);
                params.weight = weight;
                params.setMargins(10,5,10,5);
                linTimeSlot.addView(sectionProgess, params);
            }
        }


        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return TimeSlotObject.class;
        }

    }
}
