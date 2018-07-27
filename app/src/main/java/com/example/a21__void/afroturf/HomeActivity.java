package com.example.a21__void.afroturf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a21__void.Modules.CustomClusterRender;
import com.example.a21__void.Modules.DirectionFinder;
import com.example.a21__void.Modules.DirectionFinderListener;
import com.example.a21__void.Modules.RemoteDataRetriever;
import com.example.a21__void.Modules.Route;
import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.Modules.SalonsPreviewFragment;
import com.example.a21__void.Modules.SearchFragment;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.LinkedListMultimap;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.JsonArray;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeActivity extends AppCompatActivity implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, View.OnClickListener, DirectionFinderListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager vpgSalons;
    private MySalon selectedSalon;
    private SalonsFragementAdapter salonsFragementAdapter;

    private RelativeLayout previewLayout;
    private FloatingActionButton fabHide;

    private boolean previewing = false;
    private boolean progressShown = true;

    private RelativeLayout relProgress, relSwitcher;
    private SmallPreview smallPreview;

    private BitmapDescriptor locationIcon;

    private Animation animRotation;
    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent intent = new Intent(getApplicationContext(),Salon.class);
        startActivityForResult(intent, Salon.REQUEST_PATH);
    }
    @Override
    public boolean onMarkerClick(final Marker marker){
        int pos = salonsFragementAdapter.get(marker.getPosition());
        if(pos >=0){
            vpgSalons.setCurrentItem(pos, true);
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {



        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }




        @Override
        public View getInfoWindow(Marker marker) {

            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
    LocationManager locationManager;
    MyLocationListiner locationListener;



    private GoogleMap mMap;
    private double longitude, latitude;
    private TextView ttvUpdate;
    private FusedLocationProviderClient mFusedLocationClient;

    private ClusterManager<MySalon> mClusterManager;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.animRotation = AnimationUtils.loadAnimation(this, R.anim.rortation);
        this.locationIcon  = BitmapDescriptorFactory.fromResource(R.drawable.ic_locationpin);


        //btn_path = findViewById(R.id.btn_path);
        //FloatingActionButton fabNav = findViewById(R.id.fab_nav);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);


        BottomNavigationView bnvNav = this.findViewById(R.id.bnv_nav);
        bnvNav.setSelectedItemId(R.id.nav_map);
        bnvNav.setOnNavigationItemSelectedListener(this);

        this.relProgress = this.findViewById(R.id.prog_loading);
        this.relSwitcher = this.findViewById(R.id.rel_switcher);
        this.relProgress.findViewById(R.id.tmp_prog).startAnimation(this.animRotation);

        //this.fabHide = findViewById(R.id.fab_hide);

        //fabNav.setOnClickListener(this);
        //fabHide.(this);

        this.previewLayout = this.findViewById(R.id.tmp_rel);

        this.smallPreview = new SmallPreview(this);

        this.vpgSalons = findViewById(R.id.vpgSalons);
        this.vpgSalons.startViewTransition(null);///;/.setPageMargin(20);

        this.salonsFragementAdapter = new SalonsFragementAdapter(getSupportFragmentManager());
        this.vpgSalons.setAdapter(salonsFragementAdapter);

       // btn_path.setOnClickListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Log.i("ZAQ", "onCreate: ");
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        locationListener = new MyLocationListiner(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        setUpMap();
        getDeviceLocation();

    }



    public void getDeviceLocation(final DataCallBack dataCallBack) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null && mMap != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("ZAQ", "onReceiveResult: " + latitude + "| " + longitude);

                    dataCallBack.onLocation(longitude, latitude);


                }

            }
        });
    }

    public void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null && mMap != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("ZAQ", "onReceiveResult: " + latitude + "| " + longitude);


                }

            }
        });
    }



    private void showProgress(int parentId){
        if(!this.progressShown && relProgress != null){
            ((ViewGroup)this.findViewById(parentId)).addView(this.relProgress);
            this.relProgress.findViewById(R.id.tmp_prog).startAnimation(this.animRotation);
            progressShown = true;
        }
    }

    private void hideProgress(int parentId){
        if(this.progressShown && relProgress != null){
            ((ViewGroup)this.findViewById(parentId)).removeView(relProgress);
            this.animRotation.cancel();
            progressShown = false;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.i("ZAQ", "onMapReady: " + "| " + 2);

        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        //mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.getUiSettings().setMapToolbarEnabled(false);


        getDeviceLocation(new DataCallBack() {
            @Override
            public void onLocation(final double lang, final double lat) {

                Log.i("ZAQ", "onMapReady: " + lang + "| " + lat);

                LatLng customerLoaction = new LatLng(lat, lang);
                setUpClusterer(lat+0.1, lang+0.1);

                HomeActivity.this.getSalons(lat, lang, 80, new RemoteDataRetriever.DataCallback() {
                    @Override
                    public void onReceive(String rawData) {
                        Log.i("RFC", "onReceive: " + rawData);
                        try {
                            Random random = new Random();
                            JSONArray rawSalons = new JSONArray(rawData);
                            for(int pos = 0; pos < rawSalons.length(); pos++){
                                JSONObject rawSalon = rawSalons.getJSONObject(pos);
                                String salonLocation = rawSalon.getJSONObject("salonData").getString("location"),
                                        salonName = rawSalon.getJSONObject("salonData").getString("name");

                                String[] locationParts = salonLocation.split(",");
                                double sLatitude = Double.parseDouble(locationParts[0]), sLongitude = Double.parseDouble(locationParts[1]);

                                MySalon salon = new MySalon(sLatitude,sLongitude, salonName, salonName, random.nextInt(5));
                                SalonsPreviewFragment previewFragment = new SalonsPreviewFragment();
                                previewFragment.setSalon(salon);

                                HomeActivity.this.salonsFragementAdapter.Add(previewFragment);
                                mClusterManager.addItem(salon);
                            }

                            for (int i = 0; i < 10; i++) {

                                double offset = i / 100d;
                                double mul1 = random.nextBoolean() ? -1 : 1, mul2 = random.nextBoolean() ? -1 : 1, mul3 = random.nextBoolean() ? -1:1;
                                double nextLati = (lat + offset * mul1);
                                double nextLong = (lang + offset * mul2);
                                MySalon salon = new MySalon(nextLati, nextLong, "Hard Coded Salon", "", random.nextInt(5));
                                SalonsPreviewFragment previewFragment = new SalonsPreviewFragment();
                                previewFragment.setSalon(salon);
                                if(i == 0)
                                    selectedSalon = salon;

                                HomeActivity.this.salonsFragementAdapter.Add(previewFragment);
                                mClusterManager.addItem(salon);
                            }


                            HomeActivity.this.hideProgress(R.id.frame_container);
                            if(selectedSalon != null){
                                HomeActivity.this.smallPreview.show(HomeActivity.this.relSwitcher, selectedSalon);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title("JeffTown")
                        .position(customerLoaction)));


                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(customerLoaction)      // Sets the center of the map to Mountain View
                        .zoom(12f)                   // Sets the zoom
                        .bearing(50)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                  // Creates a CameraPosition from the builder

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);


                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 50f));



            }
        });



    }


    private void setUpClusterer(double lat, double lang) {
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, getMap());


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MySalon>() {
            @Override
            public boolean onClusterItemClick(MySalon mySalon) {
                int pos = salonsFragementAdapter.get(mySalon.getPosition());
                if(pos >=0){
                    vpgSalons.setCurrentItem(pos, true);
                    //return true;
                }

                return false;
            }
        });
        mClusterManager.setRenderer(new CustomClusterRender(this, getMap(), mClusterManager));

        // Add cluster items (markers) to the cluster manager.
        //NB!! addItem replaces with getSalons...
        //addItems(lat, lang);
    }

    private void getSalons(double latitude, double longitude, double searchRadius, RemoteDataRetriever.DataCallback callback){
        String url = "https://us-central1-afroturf-d2c3a.cloudfunctions.net/search?location=" + latitude +"," + longitude + "&radius=" + searchRadius + "&searchPath=salons";
        RemoteDataRetriever.getInstance().get(url, callback);
    }

    private void addItems(double lat, double lng) {


        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 10d;
            lat = lat + offset;
            lng = lng + offset;
            MySalon offsetItem = new MySalon(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }
    private void setUpMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(HomeActivity.this);
        new OnMapAndViewReadyListener(mapFragment, this);


        Log.i("ZAQ", "setUpMap: " + latitude + "| " + longitude);

    }

    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.fab_nav:
//                sendRequest();
//                break;
//            case R.id.fab_hide:
//                //togglePreview();
//                getSupportFragmentManager().beginTransaction().add(R.id.rel_container, new SearchFragment()).addToBackStack("search").commit();
//                break;
//        }
    }

    private void togglePreview() {
        if(previewing){
            previewLayout.setVisibility(View.GONE);

        }else{
            previewLayout.setVisibility(View.VISIBLE);
        }
        this.previewing = !previewing;
    }

    private void sendRequest() {
        String origin = Double.toString(latitude) + "," + Double.toString(longitude);
        Log.i("RFC", "sendRequest: " + origin);
        String destination = Double.toString(latitude+.1) + "," + Double.toString(longitude+.1);
        try{
            new DirectionFinder(this, origin, destination).execute();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }


    public interface DataCallBack{


        void onLocation(double lang, double lat);

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.ttvDuration)).setText(route.duration.text);
            //((TextView) findViewById(R.id.ttvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)
            .icon(locationIcon)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)
            .icon(locationIcon)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.rgb( 0,179, 253))
                    .width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ZAQ", "startActivityForResult: clicked");

        if (requestCode == Salon.REQUEST_PATH) {
            // Make sure the request was successful
            Log.i("ZAQ", "startActivityForResult: in");

            sendRequest();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_user:
                Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                this.togglePreview();
                this.smallPreview.show(relSwitcher, selectedSalon);
                break;
            case R.id.nav_nearby:
                this.smallPreview.hide();
                togglePreview();
                break;
            case R.id.nav_list:
                Toast.makeText(this, "List", Toast.LENGTH_SHORT).show();
                break;
                default:
                    return false;
        }
        return true;
    }

    private static class SmallPreview{
        private final RelativeLayout parentView;
        private final TextView txtName;
        private final RatingBar ratRating;
        private final ImageView imgNext;

        private RelativeLayout currentContainer;

        public SmallPreview(Context context){
            this.parentView = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.small_preview, null, false);
            this.txtName = this.parentView.findViewById(R.id.txt_name);
            this.ratRating = this.parentView.findViewById(R.id.rat_rating);
            this.imgNext = this.parentView.findViewById(R.id.img_next);
        }

        public void show(RelativeLayout container, MySalon salon){
            this.hide();
            Animation  animFade = AnimationUtils.loadAnimation(container.getContext(), android.R.anim.fade_in);
            animFade.setDuration(1000);
            this.currentContainer = container;

            this.txtName.setText(salon.getTitle());
            this.ratRating.setRating(salon.getRating());
            RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.currentContainer.addView(this.parentView, params);
            this.parentView.startAnimation(animFade);
        }

        public void hide(){
            if(this.currentContainer != null){
                this.currentContainer.removeView(parentView);
                this.currentContainer = null;
            }
        }
    }

}
