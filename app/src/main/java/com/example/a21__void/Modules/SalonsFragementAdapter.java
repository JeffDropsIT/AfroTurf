package com.example.a21__void.Modules;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class SalonsFragementAdapter extends PagerAdapter implements CacheManager.CachePointer.CursorSwapListener {
    private final Context context;
    private final CacheManager.CachePointer cachePointer;
    private List<SalonAfroObject.UIHandler>  salonAfroObjects;


    public SalonsFragementAdapter(Context pContext, CacheManager.CachePointer pCachePointer) {
        this.context = pContext;
        this.salonAfroObjects = new ArrayList<>();
        this.cachePointer = pCachePointer;
        this.cachePointer.addListener(this);

        this.onSwapCursor(cachePointer.getCursor());
        Log.i("TGIFk", this.salonAfroObjects.size() + "|");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SalonAfroObject.UIHandler salonObl = this.salonAfroObjects.get(position);
        container.addView(salonObl.itemView);
        return salonObl.itemView;
    }

    @Override
    public int getCount() {
        return salonAfroObjects.size();
    }


    public View getCardViewAt(int currentItem) {
            return (currentItem >= 0 && currentItem < this.getCount()) ? this.salonAfroObjects.get(currentItem).itemView : null;// this.salonAfroObjects.get(currentItem).getCard() : null;
    }

    @Override
    public void onSwapCursor(Cursor cursor) {
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
            this.salonAfroObjects.add(uiHandler);
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
}
