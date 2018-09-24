package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgSalon.GeneralRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends AfroFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DateListener listener;
    private String mParam2;


    public DatePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatePickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatePickerFragment newInstance(DateListener pListener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.listener = pListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup parent =  (ViewGroup)inflater.inflate(R.layout.fragment_date_picker, container, false);
        final DatePicker datePicker = parent.findViewById(R.id.dpk_date);
        parent.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onGetDate(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                }
            }
        });
        return parent;
    }

    @Override
    public String getTitle() {
        return "Date";
    }

    public static interface DateListener{
        void onGetDate(int year, int month, int day);
    }
}
