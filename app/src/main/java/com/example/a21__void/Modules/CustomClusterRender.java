package com.example.a21__void.Modules;

import android.content.Context;

import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.R;
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
public class CustomClusterRender extends DefaultClusterRenderer<MySalon> {

    private final BitmapDescriptor locationIcon;

    public CustomClusterRender(Context context, GoogleMap map, ClusterManager<MySalon> clusterManager) {
        super(context, map, clusterManager);
        this.locationIcon  = BitmapDescriptorFactory.fromResource(R.drawable.ic_locationpin);

    }

    @Override
    protected void onBeforeClusterItemRendered(MySalon item, MarkerOptions markerOptions) {
        markerOptions.icon(locationIcon);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
