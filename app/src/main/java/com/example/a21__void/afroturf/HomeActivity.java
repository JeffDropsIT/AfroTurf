package com.example.a21__void.afroturf;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.CustomClusterRender;
import com.example.a21__void.Modules.ProgressFragment;
import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.fragments.SmallPreviewFragment;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ReviewsManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.Modules.SearchFragment;
import com.example.a21__void.afroturf.manager.ServicesManager;
import com.example.a21__void.afroturf.manager.StylistsManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;
import com.example.a21__void.afroturf.pkgSalon.SalonActivity;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {
    private static final boolean debug = false;
    private static final int MODE_IDLE = 0, MODE_SEARCH = 1, MODE_USER_SETTINGS= 2;
    private static final int SEARCH_RADIUS = 5;
    private static final int REQ_LOCATION_PERMISSIONS = 132;

    private static final String STACK_NAME_USER_PATH = "stack_user",
            STACK_NAME_SEARCH = "stack_search",
            STACK_NAME_SMALL_PREVIEW = "stack_small_preview",
            STACK_NAME_PAGES = "stack_pages",
            STACK_NAME_FAVOURITE = "stack_favourite";

    private int currentMode = MODE_IDLE;
    private static String stackNameNavBottom = null, stackNameNavTop = null;
    private SalonAfroObject selectedSalon = null;

    private RelativeLayout previewLayout;
    private FloatingActionButton fabHide;

    private boolean previewing = false;
    private boolean progressShown = true;

    private RelativeLayout rel_secondary_container;
    private BottomNavigationView bnvNav;

    //FRAGMENTS
    private final SearchFragment searchFragment = new SearchFragment();
    private final UserSettingFragment userSettingFragment = new UserSettingFragment();
    private final SmallPreviewFragment smallPreviewFragment = new SmallPreviewFragment();
    private final PagesFragment pagesFragment = new PagesFragment();
    private final FavouriteFragment favouriteFragment = new FavouriteFragment();

    //MAP TAB
    private SmallPreview smallPreview;



    //DEVICE
    private Location currentLocation = null;

    //GOOGLE
    private boolean isMapReady = false;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private ClusterManager<SalonAfroObject.SalonClusterItem> clusterManager;


    LocationManager locationManager;
    MyLocationListiner locationListener;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    //FLAGS
    private static final String FLAG_FIRST_RUN = "flag_first_run";
    //MANAGERS
    SalonsManager salonsManager;
    ServicesManager servicesManager;
    StylistsManager stylistsManager;
    ReviewsManager reviewsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.salonsManager = (SalonsManager)CacheManager.getManager(this, SalonsManager.class);
        this.servicesManager = (ServicesManager)CacheManager.getManager(this, ServicesManager.class);
        this.stylistsManager = (StylistsManager)CacheManager.getManager(this, StylistsManager.class);
        this.reviewsManager = (ReviewsManager)CacheManager.getManager(this, ReviewsManager.class);


        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);


        boolean isFirstRun = sharedPreferences.getBoolean(FLAG_FIRST_RUN, true);
        if(isFirstRun){
            this.salonsManager.init(this.getApplicationContext());
            this.stylistsManager.init(this.getApplicationContext());
            this.servicesManager.init(this.getApplicationContext());
            this.reviewsManager.init(this.getApplicationContext());
        }

        this.salonsManager.beginManagement();
        this.stylistsManager.beginManagement();
        this.servicesManager.beginManagement();
        this.reviewsManager.beginManagement();

        if(debug){
            Intent intent = new Intent(this, SalonActivity.class);
            this.startActivity(intent);
            return;
        }

        this.smallPreview = new SmallPreview(this);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.bnvNav = this.findViewById(R.id.bnv_nav);
        bnvNav.setItemIconTintList(null);
        bnvNav.setOnNavigationItemSelectedListener(this);

        this.rel_secondary_container = this.findViewById(R.id.rel_secondary_container);


        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        this.init();
    }

    private void init(){
        this.showProgress();
        this.setUpMap();
        this.getDeviceLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                SalonsManager.getInstance(getApplicationContext()).requestRefresh(new CacheManager.ManagerRequestListener<CacheManager>() {
                    @Override
                    public void onRespond(CacheManager result) {
                        currentLocation = location;
                        HomeActivity.this.refreshMap();
                    }
                });
            }
        });
    }

    public void getDeviceLocation(OnSuccessListener<Location> locationCallback) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION_PERMISSIONS);
            return;
        }else{
            this.fusedLocationClient.getLastLocation().addOnSuccessListener(locationCallback);
        }
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMapManager.getInstance().registerMapCallback(mapFragment, new GoogleMapManager.MapCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                HomeActivity.this.googleMap = googleMap;

                HomeActivity.this.googleMap.setMyLocationEnabled(true);

                UiSettings mapUiSettings = HomeActivity.this.googleMap.getUiSettings();
                mapUiSettings.setZoomControlsEnabled(false);
                mapUiSettings.setMapToolbarEnabled(false);


                HomeActivity.this.setUpClusterManager();
                HomeActivity.this.isMapReady = true;
                HomeActivity.this.refreshMap();
            }
        });
    }

    private void setUpClusterManager() {
        if(isMapReady){
            this.clusterManager = new ClusterManager<SalonAfroObject.SalonClusterItem>(this, this.googleMap);

            this.googleMap.setOnCameraIdleListener(this.clusterManager);
            this.googleMap.setOnMarkerClickListener(clusterManager);
            this.clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SalonAfroObject.SalonClusterItem>() {
                @Override
                public boolean onClusterItemClick(SalonAfroObject.SalonClusterItem salonObject) {
                    HomeActivity.this.selectedSalon = salonObject.getSalonAfroObject();
                    HomeActivity.this.onMapPinClick(salonObject);
                    return false;
                }
            });

           this.clusterManager.setRenderer(new CustomClusterRender(this, this.googleMap, this.clusterManager));
        }
    }

    private void onMapPinClick(SalonAfroObject.SalonClusterItem salonObject) {
        if(this.stackNameNavBottom == null)
            return;

        if(stackNameNavBottom == STACK_NAME_SMALL_PREVIEW){
            SmallPreviewFragment smallPreviewFragment = (SmallPreviewFragment) this.getSupportFragmentManager().findFragmentByTag(stackNameNavBottom);
            smallPreviewFragment.setSalonObject(this.selectedSalon);
        }else if(stackNameNavBottom == STACK_NAME_PAGES){

        }else{

        }
    }

    private void refreshMap() {
        if(isMapReady && currentLocation != null){
            if(!debug){
                final LatLng customerLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(customerLocation)      // Sets the center of the map to Mountain View
                        .zoom(12f)                   // Sets the zoom
                        .bearing(50)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                  // Creates a CameraPosition from the builder
                this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                HomeActivity.this.salonsManager.requestRefresh(new CacheManager.ManagerRequestListener<CacheManager>() {
                    @Override
                    public void onRespond(CacheManager result) {
                        HomeActivity.this.setUpClusterManager();
                        clusterManager.clearItems();
                        JsonParser jsonParser = new JsonParser();
                        for(int pos = 0; pos < result.getCachePointer().getCursor().getCount(); pos++){
                            Cursor cursor = result.getCachePointer().getCursor();
                            if(!cursor.moveToPosition(pos))
                                continue;

                            byte[] json = cursor.getBlob(cursor.getColumnIndex(AfroObjectDatabaseHelper.COLUMN_JSON));
                            SalonAfroObject salonAfroObject = new SalonAfroObject();
                            salonAfroObject.set(jsonParser, new String(json));

                            clusterManager.addItem(new SalonAfroObject.SalonClusterItem(salonAfroObject));
                            if(pos == 0)
                                HomeActivity.this.selectedSalon = salonAfroObject;
                        }
                        Log.i("TGIF", customerLocation + ":");
                        HomeActivity.this.hideProgress();
                        bnvNav.setSelectedItemId(R.id.nav_pages);
                    }
                });
            }
        }
    }


    private void showProgress(){
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rel_secondary_container, ProgressFragment.newInstance("Loading Salons"), ProgressFragment.TAG);
        transaction.commit();
    }

    private void hideProgress(){
        final Fragment currentProgressTag = this.getSupportFragmentManager().findFragmentByTag(ProgressFragment.TAG);

        if(currentProgressTag != null){
            Animator animator = ((ProgressFragment)currentProgressTag).hide();

            if(animator != null){
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        FragmentTransaction transaction = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                        transaction.remove(currentProgressTag);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }else{
                FragmentTransaction transaction = HomeActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.remove(currentProgressTag);
                transaction.commit();
            }

        }
    }






//    private void getSalons(double latitude, double longitude, double searchRadius, RemoteDataRetriever.DataCallback callback){
//        String url = "https://us-central1-afroturf-d2c3a.cloudfunctions.net/search?location=" + latitude +"," + longitude + "&radius=" + searchRadius + "&searchPath=salons";
//        RemoteDataRetriever.getInstance().get(url, callback);
//    }


//    private void sendRequest() {
//        String origin = Double.toString(latitude) + "," + Double.toString(longitude);
//        Log.i("RFC", "sendRequest: " + origin);
//        String destination = Double.toString(latitude+.1) + "," + Double.toString(longitude+.1);
//        try{
//            new DirectionFinder(this, origin, destination).execute();
//        }catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//    }


//    @Override
//    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "Finding direction..!", true);
//
//        if (originMarkers != null) {
//            for (Marker marker : originMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (destinationMarkers != null) {
//            for (Marker marker : destinationMarkers) {
//                marker.remove();
//            }
//        }
//
//        if (polylinePaths != null) {
//            for (Polyline polyline:polylinePaths ) {
//                polyline.remove();
//            }
//        }
//    }

//    @Override
//    public void onDirectionFinderSuccess(List<Route> routes) {
//        progressDialog.dismiss();
//        polylinePaths = new ArrayList<>();
//        originMarkers = new ArrayList<>();
//        destinationMarkers = new ArrayList<>();
//
//        for (Route route : routes) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
//            //((TextView) findViewById(R.id.ttvDuration)).setText(route.duration.text);
//            //((TextView) findViewById(R.id.ttvDistance)).setText(route.distance.text);
//
//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .title(route.startAddress)
//                    .position(route.startLocation)
//            .icon(locationIcon)));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .title(route.endAddress)
//                    .position(route.endLocation)
//            .icon(locationIcon)));
//
//            PolylineOptions polylineOptions = new PolylineOptions().
//                    geodesic(true).
//                    color(Color.rgb( 0,179, 253))
//                    .width(10);
//
//            for (int i = 0; i < route.points.size(); i++)
//                polylineOptions.add(route.points.get(i));
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                this.onUserSettingsClick(item);
                break;
            case R.id.nav_search:
                this.onSearchClick(item);
                break;
        }
        return true;
    }

    private void onUserSettingsClick(final MenuItem item) {
        if(currentMode == MODE_USER_SETTINGS){
            this.getSupportFragmentManager().popBackStack(STACK_NAME_USER_PATH, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            currentMode = MODE_IDLE;
        }else{
            if(userSettingFragment.getAfroFragmentCallback() == null){
                userSettingFragment.setAfroFragmentCallback(new AfroFragment.AfroFragmentCallback() {
                    @Override
                    public void onShow() {
                        HomeActivity.this.currentMode = MODE_USER_SETTINGS;
                        item.setIcon(R.drawable.user_icon_selected);
                    }

                    @Override
                    public void onClose() {
                        item.setIcon(R.drawable.user_icon);
                    }
                });
            }
            getSupportFragmentManager().popBackStack(STACK_NAME_SEARCH, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rel_secondary_container, userSettingFragment);
            transaction.addToBackStack(STACK_NAME_USER_PATH);
            transaction.commit();
        }
    }

    private void onSearchClick(final MenuItem item) {
        if(currentMode == MODE_SEARCH){
            this.getSupportFragmentManager().popBackStack(STACK_NAME_SEARCH, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            HomeActivity.this.currentMode = MODE_IDLE;
        }else{
            if(searchFragment.getAfroFragmentCallback() == null){
                searchFragment.setAfroFragmentCallback(new AfroFragment.AfroFragmentCallback() {
                    @Override
                    public void onShow() {
                        HomeActivity.this.currentMode = MODE_SEARCH;
                        item.setIcon(R.drawable.search_icon_selected);
                    }

                    @Override
                    public void onClose() {
                        item.setIcon(R.drawable.search_icon);
                    }
                });
            }
            getSupportFragmentManager().popBackStack(STACK_NAME_USER_PATH, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rel_secondary_container, searchFragment);
            transaction.addToBackStack(STACK_NAME_SEARCH);
            transaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        if(this.stackNameNavBottom == convertTOStackName(item.getItemId()))
            return true;

        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(this.stackNameNavBottom != null){
            AfroFragment afroFragment = (AfroFragment)fragmentManager.findFragmentByTag(stackNameNavBottom);
            if(this.stackNameNavTop == null)
                afroFragment.requestClose(new AfroFragment.AfroFragmentCallback() {
                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onClose() {
                        fragmentManager.popBackStack(HomeActivity.this.stackNameNavBottom, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        switchBottomNavigation(item);
                    }
                });
            else{
                fragmentManager.popBackStack(this.stackNameNavBottom, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                switchBottomNavigation(item);
            }
        }else{
            switchBottomNavigation(item);
        }
        return true;
    }

    private String convertTOStackName(int itemId) {
        switch (itemId){
            case R.id.nav_map:
                return STACK_NAME_SMALL_PREVIEW;
            case R.id.nav_pages:
                return  STACK_NAME_PAGES;
            case R.id.nav_user:
                return  STACK_NAME_FAVOURITE;
                default:
                    return "";
        }
    }

    private void switchBottomNavigation(MenuItem item) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        this.deselectMenuItem(this.bnvNav.getMenu(), this.stackNameNavBottom);


        switch (item.getItemId()){
            case R.id.nav_map:
                item.setIcon(R.drawable.ic_map_selected);
                this.stackNameNavBottom = STACK_NAME_SMALL_PREVIEW;
                transaction.replace(R.id.rel_secondary_container, this.smallPreviewFragment, STACK_NAME_SMALL_PREVIEW);
                this.smallPreviewFragment.setSalonObject(this.selectedSalon);
                break;
            case R.id.nav_pages:
                item.setIcon(R.drawable.ic_pages_selected);
                this.stackNameNavBottom = STACK_NAME_PAGES;
                transaction.replace(R.id.rel_secondary_container, this.pagesFragment, STACK_NAME_PAGES);
                break;
            case R.id.nav_user:
                item.setIcon(R.drawable.ic_bookmark_selected);
                this.stackNameNavBottom = STACK_NAME_FAVOURITE;
                transaction.replace(R.id.rel_secondary_container, this.favouriteFragment, STACK_NAME_FAVOURITE);
                break;
            default:
        }
        transaction.commit();
    }

    private void deselectMenuItem(Menu menu, String stackNameNavBottom) {
        if(menu == null || stackNameNavBottom == null)
            return;

        MenuItem item;
        switch (stackNameNavBottom){
            case STACK_NAME_SMALL_PREVIEW:
                item = menu.findItem(R.id.nav_map);
                item.setIcon(R.drawable.ic_map_deselected);
            case STACK_NAME_PAGES:
                item = menu.findItem(R.id.nav_pages);
                item.setIcon(R.drawable.ic_pages_deslected);
            case STACK_NAME_FAVOURITE:
                item = menu.findItem(R.id.nav_user);
                item.setIcon(R.drawable.ic_bookmark_deselected);
                default:
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        this.onBackPressed();
        Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        this.currentMode = MODE_IDLE;
        super.onBackPressed();
    }

    private static class SmallPreview{
        private final RelativeLayout parentView;
        private final TextView txtName;
        private final RatingBar ratRating;
        private final ImageView imgNext;
        private RelativeLayout currentContainer;

        private boolean isShown;

        public SmallPreview(Context context){
            this.isShown = false;
            this.parentView = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.small_preview, null, false);
            this.txtName = this.parentView.findViewById(R.id.txt_name);
            this.ratRating = this.parentView.findViewById(R.id.rat_rating);
            this.imgNext = this.parentView.findViewById(R.id.img_next);
        }

        public void show(RelativeLayout container, SalonObject salonObject, View.OnClickListener onClickListener){
            this.hide();
            RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            this.currentContainer = container;
            this.imgNext.setOnClickListener(onClickListener);
            this.currentContainer.addView(this.parentView, params);
            //TODO animate

            this.update(salonObject);
            this.isShown = true;
        }

        public void update(SalonObject salonObject) {
            this.txtName.setText(salonObject.getTitle());
            this.ratRating.setRating(new Float(salonObject.getRating()));
        }

        public void hide(){
            if(this.currentContainer != null){
                this.currentContainer.removeView(parentView);
                this.currentContainer = null;
                this.isShown = false;
            }
        }

        public boolean isShown(){
            return this.isShown;
        }
    }

}
