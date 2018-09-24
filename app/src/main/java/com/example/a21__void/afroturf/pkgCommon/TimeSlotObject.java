package com.example.a21__void.afroturf.pkgCommon;

import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;

public class TimeSlotObject {
    public final int startHour, startMin, endHour, endMin;
    public TimeSlotObject(int startHour, int startMin, int endHour, int endMin) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    public static class TimeSlotTemplate extends HolderTemplate<TimeSlotObject>{
        TextView txtStart, txtEnd;
        SectionProgess sc;
        public TimeSlotTemplate(ViewGroup itemView) {
            super(itemView);
            this.sc = itemView.findViewById(R.id.sc);
            this.txtStart =itemView.findViewById(R.id.txt_start_time);
            this.txtEnd =itemView.findViewById(R.id.txt_end_time);
        }

        @Override
        public void bind(TimeSlotObject data, int pos) {
            txtStart.setText(data.startHour + ":0" + data.startMin);
            txtEnd.setText(data.endHour + ":0" + data.endMin);
            if(pos % 3 ==0){
                sc.section(true);
            }else{
                sc.section(false);
            }
        }
    }
}
