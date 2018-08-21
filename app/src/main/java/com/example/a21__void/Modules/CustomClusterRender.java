package com.example.a21__void.Modules;

import android.content.Context;

import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.SalonObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by ASANDA on 2018/07/10.
 * for Pandaphic
 */
public class CustomClusterRender extends DefaultClusterRenderer<SalonObject> {

    private final BitmapDescriptor locationIcon;

    public CustomClusterRender(Context context, GoogleMap map, ClusterManager<SalonObject> clusterManager) {
        super(context, map, clusterManager);
        this.locationIcon  = BitmapDescriptorFactory.fromResource(R.drawable.ic_locationpin);
    }

    @Override
    protected void onBeforeClusterItemRendered(SalonObject item, MarkerOptions markerOptions) {
        markerOptions.icon(locationIcon);
        markerOptions.infoWindowAnchor(Float.MAX_VALUE, Float.MAX_VALUE);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }


}
