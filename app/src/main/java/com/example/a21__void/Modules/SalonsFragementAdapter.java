package com.example.a21__void.Modules;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class SalonsFragementAdapter extends PagerAdapter implements CacheManager.CachePointer.CursorSwapListener {
    private final Context context;
    private final CacheManager.CachePointer cachePointer;
    private List<SalonAfroObject.UIHandler>  salonAfroObjectsUI;
    private final ArrayList<SalonAfroObject> salonAfroObjects;

    private OnItemClickListener clickListener;


    public SalonsFragementAdapter(Context pContext, CacheManager.CachePointer pCachePointer) {
        this.context = pContext;
        this.salonAfroObjectsUI = new ArrayList<>();
        this.salonAfroObjects = new ArrayList<>();
        this.cachePointer = pCachePointer;
        this.cachePointer.addListener(this);

        this.onSwapCursor(cachePointer.getCursor());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        SalonAfroObject.UIHandler salonObl = this.salonAfroObjectsUI.get(position);
        container.addView(salonObl.itemView);
        salonObl.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SalonsFragementAdapter.this.clickListener != null)
                    SalonsFragementAdapter.this.clickListener.onItemClick(salonAfroObjects.get(position), position);
            }
        });
        return salonObl.itemView;
    }

    @Override
    public int getCount() {
        return salonAfroObjectsUI.size();
    }


    public View getCardViewAt(int currentItem) {
            return (currentItem >= 0 && currentItem < this.getCount()) ? this.salonAfroObjectsUI.get(currentItem).itemView : null;// this.salonAfroObjects.get(currentItem).getCard() : null;
    }

    @Override
    public void onSwapCursor(Cursor cursor) {
        this.salonAfroObjectsUI.clear();
        this.salonAfroObjects.clear();

        JsonParser jsonParser = new JsonParser();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        for(int pos = 0; pos < cursor.getCount(); pos++){
            if(!cursor.moveToPosition(pos))
                continue;

            byte[] json = cursor.getBlob(cursor.getColumnIndex(AfroObjectDatabaseHelper.COLUMN_JSON));
            SalonAfroObject salonAfroObject = new SalonAfroObject();
            salonAfroObject.set(jsonParser, new String(json));


            SalonAfroObject.UIHandler uiHandler = new SalonAfroObject.UIHandler(inflater.inflate(R.layout.salon_layout, null, false));
            uiHandler.bind(salonAfroObject, pos);
            this.salonAfroObjectsUI.add(uiHandler);
            this.salonAfroObjects.add(salonAfroObject);
        }

        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        if(observer != null){
            super.unregisterDataSetObserver(observer);
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(SalonAfroObject salonAfroObject, int position);
    }

}
