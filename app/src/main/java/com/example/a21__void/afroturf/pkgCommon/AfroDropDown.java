package com.example.a21__void.afroturf.pkgCommon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.a21__void.afroturf.R;

/**
 * Created by ASANDA on 2018/12/20.
 * for Pandaphic
 */
public class AfroDropDown extends RelativeLayout implements View.OnClickListener, DialogInterface.OnClickListener {
    private static final String DEFAULT_HINT = "Select Option";

    private String[] dataset = null;
    private int selection = 0;
    private String hint = DEFAULT_HINT;

    private TextView txtSelection;
    private AlertDialog.Builder alertDialogBuilder;
    private Listener listener;

    public AfroDropDown(Context context) {
        super(context);
        this.init(null, 0, 0);

    }

    public AfroDropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0, 0);
    }

    public AfroDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AfroDropDown(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defRes) {
        inflate(this.getContext(), R.layout.template_dropdown, this);

        this.txtSelection = this.findViewById(R.id.txt_selection);

        if(attrs == null){
            this.setHint(this.DEFAULT_HINT);
            this.txtSelection.setText(this.selection);
        }else{
            TypedArray arr = this.getContext().obtainStyledAttributes(attrs, R.styleable.AfroDropDown, defStyle, defRes);
            this.setSelection(arr.getInt(R.styleable.AfroDropDown_selection, 0));
            this.setHint(arr.getString(R.styleable.AfroDropDown_hinto));
            arr.recycle();
        }

        this.alertDialogBuilder = new AlertDialog.Builder(this.getContext());

        this.alertDialogBuilder.setTitle(this.hint)
                .setItems(this.dataset, this)
                .setCancelable(true);

        this.setOnClickListener(this);

    }


    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
        if(this.dataset != null && ( this.selection >= 0 && this.selection < this.dataset.length))
            this.txtSelection.setText(this.dataset[this.selection]);
    }

    public void setHint(String hint) {
        if(hint != null)
            this.hint = hint;
        else
            this.hint = DEFAULT_HINT;

        if(this.dataset == null || !( this.selection >= 0 && this.selection < this.dataset.length))
            this.txtSelection.setText(hint);
    }

    public void setDataset(String[] dataset) {
        this.dataset = dataset;
        this.alertDialogBuilder.setItems(this.dataset, this);
        if(selection >= 0 && selection < dataset.length){
            this.txtSelection.setText(dataset[this.selection]);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        this.alertDialogBuilder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        this.setSelection(which);
        if(this.listener != null)
            this.listener.onClick(which);
    }

    public interface Listener{
        void onClick(int index);
    }
}
