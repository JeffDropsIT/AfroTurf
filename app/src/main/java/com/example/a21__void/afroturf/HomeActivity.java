package com.example.a21__void.afroturf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.example.a21__void.Modules.CustomClusterRender;
import com.example.a21__void.afroturf.fragments.ProgressFragment;
import com.example.a21__void.afroturf.fragments.SmallPreviewFragment;
import com.example.a21__void.afroturf.libIX.activity.AfroActivity;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.fragment.ProcessErrorFragment;
import com.example.a21__void.afroturf.libIX.ui.RichTextView;
import com.example.a21__void.afroturf.manager.BookmarkManager;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ConnectionManager;
import com.example.a21__void.afroturf.manager.LocationManager;
import com.example.a21__void.afroturf.manager.ReviewsManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.Modules.SearchFragment;
import com.example.a21__void.afroturf.manager.ServicesManager;
import com.example.a21__void.afroturf.manager.StylistsManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.SalonActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class HomeActivity extends AfroActivity implements  BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener, View.OnClickListener {
    private static final int SEARCH_RADIUS = 5;
    private static final int REQ_LOCATION_PERMISSIONS = 132
                                , REQ_PROCESS_ERROR_BOOKMARKS = 214;

    private static final int NUM_SETUP_TASKS = 2;

    private int setupTasksRemaining = NUM_SETUP_TASKS;

    private static final String STACK_NAME_USER_PATH = "stack_user",
            STACK_NAME_SEARCH = "stack_search",
            STACK_NAME_SMALL_PREVIEW = "stack_small_preview",
            STACK_NAME_PAGES = "stack_pages",
            STACK_NAME_FAVOURITE = "stack_favourite";

    private static final String TAG_PROCESS_ERROR = "TAG_PROCESS_ERROR"
                                , TAG_PROGRESS = "TAG_PROGRESS";

    private SalonAfroObject selectedSalon = null;


    private BottomNavigationView bnvNav;
    private Toolbar toolbar;
    private RichTextView txtOffline;
    private LinearLayout linNoLocation;

    //FRAGMENTS
    private final SearchFragment searchFragment = new SearchFragment();
    private final UserSettingFragment userSettingFragment = new UserSettingFragment();
    private final SmallPreviewFragment smallPreviewFragment = new SmallPreviewFragment();
    private final PagesFragment pagesFragment = new PagesFragment();
    private final FavouriteFragment favouriteFragment = new FavouriteFragment();

    //MAP TAB



    //DEVICE
    private SalonAfroObject.Location currentLocation = null;

    //GOOGLE
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private ClusterManager<SalonAfroObject.SalonClusterItem> clusterManager;

    //FLAGS
    public static final String FLAG_FIRST_RUN = "flag_first_run";
    //MANAGERS
    SalonsManager salonsManager;
    ServicesManager servicesManager;
    StylistsManager stylistsManager;
    ReviewsManager reviewsManager;
    BookmarkManager bookmarkManager;


    private MaterialProgressBar progBackgroundWork;
    private Menu topNavigation;
    private String selectedMenuItem;
    private int lastFragmentBackStackSize = 0;


    private boolean isSettingUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.salonsManager = (SalonsManager)CacheManager.getManager(this, SalonsManager.class);
        this.servicesManager = (ServicesManager)CacheManager.getManager(this, ServicesManager.class);
        this.stylistsManager = (StylistsManager)CacheManager.getManager(this, StylistsManager.class);
        this.reviewsManager = (ReviewsManager)CacheManager.getManager(this, ReviewsManager.class);
        this.bookmarkManager = (BookmarkManager)CacheManager.getManager(this, BookmarkManager.class);

        this.getSupportFragmentManager().addOnBackStackChangedListener(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);

        boolean isFirstRun = sharedPreferences.getBoolean(FLAG_FIRST_RUN, true);
        if(isFirstRun){
            this.salonsManager.init(this.getApplicationContext());
            this.stylistsManager.init(this.getApplicationContext());
            this.servicesManager.init(this.getApplicationContext());
            this.reviewsManager.init(this.getApplicationContext());
            this.bookmarkManager.init(this.getApplicationContext());
        }

        this.salonsManager.beginManagement();
        this.stylistsManager.beginManagement();
        this.servicesManager.beginManagement();
        this.reviewsManager.beginManagement();
        this.bookmarkManager.beginManagement();


        this.toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.bnvNav = this.findViewById(R.id.bnv_nav);
        bnvNav.setItemIconTintList(null);
        bnvNav.setOnNavigationItemSelectedListener(this);

        this.linNoLocation = this.findViewById(R.id.lin_no_location);

        this.txtOffline = this.findViewById(R.id.txt_offline);
        this.txtOffline.setOnClickListener(this);

        this.progBackgroundWork = this.findViewById(R.id.prog_background_work);
        this.progBackgroundWork.setVisibility(View.INVISIBLE);

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        this.setup();
    }

    private void setup(){
        this.isSettingUp = true;
        this.showProgress("Setting up");
        this.taskSetUpMap();

        LocationManager locationManager = LocationManager.getInstance(this.getApplicationContext());
        locationManager.getDeviceLocation(new LocationManager.LocationListener() {
            @Override
            public void onGetLocation(SalonAfroObject.Location location, boolean current) {
                HomeActivity.this.linNoLocation.setVisibility(null != null ? View.INVISIBLE : View.VISIBLE);
                onTaskFinished();
            }

           @Override
            public void onError() {
                onTaskFinished();
            }
        });
    }

    private void taskSetUpMap() {
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
                HomeActivity.this.onTaskFinished();
            }
        });
    }

    private void onTaskFinished() {
        this.setupTasksRemaining--;
        if(this.setupTasksRemaining <= 0){
            this.onSetupTasksFinished();
        }
    }

    private void onSetupTasksFinished(){

        SalonAfroObject.Location userLocation = LocationManager.getInstance(this.getApplicationContext()).getCurrentLocation();
        if(userLocation != null){
            final LatLng customerLocation = new LatLng(userLocation.latitude, userLocation.longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(customerLocation)
                    .zoom(13f)
                    .bearing(50)
                    .tilt(30)
                    .build();

            this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        this.hideProgress();
        this.onDoBackgroundWork();

        HomeActivity.this.salonsManager.updateLocation(userLocation, new CacheManager.ManagerRequestListener<ArrayList<SalonAfroObject>>() {
            @Override
            public void onRespond(ArrayList<SalonAfroObject> result) {
                clusterManager.clearItems();
                for(int pos = 0; pos < result.size(); pos++){
                    SalonAfroObject salonAfroObject = result.get(pos);
                    clusterManager.addItem(new SalonAfroObject.SalonClusterItem(salonAfroObject));
                    if(pos == 0)
                        HomeActivity.this.selectedSalon = salonAfroObject;
                }
                isSettingUp = false;
                HomeActivity.this.onFinishBackgroundWork();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {

            }
        });

        HomeActivity.this.selectedMenuItem = null;
        bnvNav.setSelectedItemId(R.id.nav_pages);
    }

    private void setUpClusterManager() {
            this.clusterManager = new ClusterManager<SalonAfroObject.SalonClusterItem>(this, this.googleMap);

            this.googleMap.setOnCameraIdleListener(this.clusterManager);
            this.googleMap.setOnMarkerClickListener(clusterManager);
            this.clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SalonAfroObject.SalonClusterItem>() {
                @Override
                public boolean onClusterItemClick(SalonAfroObject.SalonClusterItem salonObject) {
                    HomeActivity.this.selectedSalon = salonObject.getSalonAfroObject();
                    if(HomeActivity.this.selectedMenuItem == null)
                        return false;

                    if(HomeActivity.this.selectedMenuItem == STACK_NAME_SMALL_PREVIEW){
                        SmallPreviewFragment smallPreviewFragment = (SmallPreviewFragment) HomeActivity.this.getSupportFragmentManager().findFragmentByTag(HomeActivity.this.selectedMenuItem);
                        smallPreviewFragment.setSalonObject(HomeActivity.this.selectedSalon);
                    }else if(selectedMenuItem == STACK_NAME_PAGES){
                        PagesFragment pagesFragment = (PagesFragment)HomeActivity.this.getSupportFragmentManager().findFragmentByTag(selectedMenuItem);
                        pagesFragment.focusOnPage(selectedSalon);
                    }
                    return false;
                }
            });

           this.clusterManager.setRenderer(new CustomClusterRender(this, this.googleMap, this.clusterManager));
    }

    private void setMenuItemSelectedIcon(String currentStackName) {
        if(currentStackName == null)
            return;

        MenuItem item;
        switch (currentStackName){
            case STACK_NAME_SMALL_PREVIEW:
                item = this.bnvNav.getMenu().findItem(R.id.nav_map);
                item.setIcon(R.drawable.ic_map_selected);
                break;
            case STACK_NAME_PAGES:
                item = this.bnvNav.getMenu().findItem(R.id.nav_pages);
                item.setIcon(R.drawable.ic_pages_selected);
                break;
            case STACK_NAME_FAVOURITE:
                item = this.bnvNav.getMenu().findItem(R.id.nav_bookmark);
                item.setIcon(R.drawable.ic_bookmark_selected);
                break;
            case STACK_NAME_USER_PATH:
                item = this.topNavigation.findItem(R.id.nav_user);
                item.setIcon(R.drawable.ic_user);
                break;
            case STACK_NAME_SEARCH:
                item = this.topNavigation.findItem(R.id.nav_search);
                item.setIcon(R.drawable.ic_search);
                Log.i("1245", "setMenuItemSelectedIcon: ");
                break;
            default:
        }
    }

    private void setMenuItemDeselectedIcon(String stackNameNavBottom) {
        if(stackNameNavBottom == null)
            return;

        MenuItem item;
        switch (stackNameNavBottom){
            case STACK_NAME_SMALL_PREVIEW:
                item = this.bnvNav.getMenu().findItem(R.id.nav_map);
                item.setIcon(R.drawable.ic_map_deselected);
                break;
            case STACK_NAME_PAGES:
                item = this.bnvNav.getMenu().findItem(R.id.nav_pages);
                item.setIcon(R.drawable.ic_pages_deslected);
                break;
            case STACK_NAME_FAVOURITE:
                item = this.bnvNav.getMenu().findItem(R.id.nav_bookmark);
                item.setIcon(R.drawable.ic_bookmark_deselected);
                break;
            case STACK_NAME_USER_PATH:
                item = this.topNavigation.findItem(R.id.nav_user);
                item.setIcon(R.drawable.user_icon);
                break;
            case STACK_NAME_SEARCH:
                item = this.topNavigation.findItem(R.id.nav_search);
                item.setIcon(R.drawable.search_icon);
                break;
            default:
        }
    }


    private String convertTOStackName(int itemId) {
        switch (itemId){
            case R.id.nav_map:
                return STACK_NAME_SMALL_PREVIEW;
            case R.id.nav_pages:
                return  STACK_NAME_PAGES;
            case R.id.nav_bookmark:
                return  STACK_NAME_FAVOURITE;
            case R.id.nav_user:
                return STACK_NAME_USER_PATH;
            case R.id.nav_search:
                return STACK_NAME_SEARCH;
            default:
                return "";
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.smallPreviewFragment.setClickListener(new SmallPreviewFragment.InteractionListener() {
            @Override
            public void onClick(SalonAfroObject salon) {
                Intent intent = new Intent(HomeActivity.this, SalonActivity.class);
                intent.putExtra(SalonActivity.EXTRA_SALON, HomeActivity.this.selectedSalon);
                HomeActivity.this.startActivity(intent);
            }
        });
        this.pagesFragment.setOnClickListener(new PagesFragment.OnClickListener() {
            @Override
            public void onItemClick(SalonAfroObject salon, int position) {
                Intent intent = new Intent(HomeActivity.this, SalonActivity.class);
                intent.putExtra(SalonActivity.EXTRA_SALON, salon);
                HomeActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_nav_menu, menu);
        this.topNavigation = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(this.isSettingUp)
            return false;

        String stackName = this.convertTOStackName(item.getItemId());

        if(this.selectedMenuItem != stackName){
            this.setMenuItemDeselectedIcon(this.selectedMenuItem);

            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            boolean currentNavTop = (selectedMenuItem == STACK_NAME_SEARCH || selectedMenuItem == STACK_NAME_USER_PATH);
            int animExit = currentNavTop ? R.anim.anim_slide_up_offscreen : R.anim.anim_slide_down
                    , animBackStackEnter = currentNavTop ? R.anim.anim_slide_onscreen_down : R.anim.anim_slide_up ;
            transaction.setCustomAnimations(R.anim.anim_slide_onscreen_down, animExit, animBackStackEnter, R.anim.anim_slide_up_offscreen);

            switch (stackName){
                case STACK_NAME_SEARCH:
                    transaction.replace(R.id.rel_secondary_container, this.searchFragment, stackName);
                    break;
                case STACK_NAME_USER_PATH:
                    transaction.replace(R.id.rel_secondary_container, this.userSettingFragment, stackName);
                    break;
            }

            this.selectedMenuItem = stackName;
            this.setMenuItemSelectedIcon(this.selectedMenuItem);
            transaction.addToBackStack(this.selectedMenuItem);
            this.lastFragmentBackStackSize++;
            transaction.commit();

        }else{
            this.getSupportFragmentManager().popBackStack();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        if(isSettingUp && item.getItemId() != R.id.nav_pages)
            return false;

        String nextStackName = convertTOStackName(item.getItemId());
        if(this.selectedMenuItem == nextStackName)
            return true;


        String lastSelectedItem  = this.selectedMenuItem;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        this.setMenuItemDeselectedIcon(this.selectedMenuItem);

        boolean currentNavTop = (selectedMenuItem == STACK_NAME_SEARCH || selectedMenuItem == STACK_NAME_USER_PATH);
        int animExit = currentNavTop ? R.anim.anim_slide_up_offscreen : R.anim.anim_slide_down
                , animBackStackEnter = currentNavTop ? R.anim.anim_slide_onscreen_down : R.anim.anim_slide_up ;

        switch (item.getItemId()){
            case R.id.nav_map:
             item.setIcon(R.drawable.ic_map_selected);
             this.selectedMenuItem = STACK_NAME_SMALL_PREVIEW;

             transaction.setCustomAnimations(R.anim.anim_slide_up, animExit, animBackStackEnter, R.anim.anim_slide_down);
             transaction.replace(R.id.rel_secondary_container, this.smallPreviewFragment, STACK_NAME_SMALL_PREVIEW);
             this.smallPreviewFragment.setLoading(this.isSettingUp);
             this.smallPreviewFragment.setSalonObject(this.selectedSalon);
             break;
         case R.id.nav_pages:
             item.setIcon(R.drawable.ic_pages_selected);
             this.selectedMenuItem = STACK_NAME_PAGES;
             transaction.setCustomAnimations(R.anim.anim_slide_up, animExit,  animBackStackEnter, R.anim.anim_slide_down);
             transaction.replace(R.id.rel_secondary_container, this.pagesFragment, STACK_NAME_PAGES);
             break;
         case R.id.nav_bookmark:
             item.setIcon(R.drawable.ic_bookmark_selected);
             this.selectedMenuItem = STACK_NAME_FAVOURITE;
             transaction.setCustomAnimations(R.anim.anim_slide_up, animExit,  animBackStackEnter, R.anim.anim_slide_down);
             transaction.replace(R.id.rel_secondary_container, this.favouriteFragment, STACK_NAME_FAVOURITE);
             break;
         default:
     }

     if(lastSelectedItem != null)
        transaction.addToBackStack(this.selectedMenuItem);

     this.lastFragmentBackStackSize++;
     transaction.commit();
        return true;
    }

    @Override
    public void onProcessError(AfroFragment parent, int processId, int resIcon, String title, String message) {
        if(parent instanceof FavouriteFragment){
            ProcessErrorFragment processErrorFragment = ProcessErrorFragment.newInstance(REQ_PROCESS_ERROR_BOOKMARKS, processId, title, message, resIcon);

            this.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(processErrorFragment, TAG_PROCESS_ERROR)
                    .commit();
        }
    }

    @Override
    public void onDoBackgroundWork() {
        super.onDoBackgroundWork();
        if(this.progBackgroundWork != null && this.progBackgroundWork.getVisibility() != View.VISIBLE)
            this.progBackgroundWork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishBackgroundWork() {
        super.onFinishBackgroundWork();
        if(this.getActiveBackgroundWorkCount() <= 0)
            if(this.progBackgroundWork != null && this.progBackgroundWork.getVisibility() != View.INVISIBLE)
                this.progBackgroundWork.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress(String message) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        ProgressFragment progressFragment = ProgressFragment.newInstance(message);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rel_secondary_container, progressFragment, TAG_PROGRESS)
                .commitNow();
    }

    @Override
    public void hideProgress() {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS);
        if(fragment == null)
            return;

        this.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.getSupportFragmentManager().popBackStack();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {

        Log.i("1245", "onBackStackChanged: " + lastFragmentBackStackSize + "|" + this.getSupportFragmentManager().getBackStackEntryCount());
        if(this.lastFragmentBackStackSize > this.getSupportFragmentManager().getBackStackEntryCount()){
            this.lastFragmentBackStackSize--;
            this.setMenuItemDeselectedIcon(this.selectedMenuItem);
            Log.i("1245", "onBackStackChanged: " + this.selectedMenuItem);

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            String currentStackName;
            if(fragmentManager.getBackStackEntryCount() > 0){
                currentStackName = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                Log.i("1245", "onBackStackChangedt: " + currentStackName);
            }else{
                currentStackName = STACK_NAME_PAGES;
            }

            this.setMenuItemSelectedIcon(currentStackName);
            this.selectedMenuItem = currentStackName;

            switch (this.selectedMenuItem){
                case STACK_NAME_SMALL_PREVIEW:
                    this.bnvNav.setSelectedItemId(R.id.nav_map);
                    break;
                case STACK_NAME_PAGES:
                    this.bnvNav.setSelectedItemId(R.id.nav_pages);
                    break;
                case STACK_NAME_FAVOURITE:
                    this.bnvNav.setSelectedItemId(R.id.nav_bookmark);
                    break;
            }
        }

    }

    @Override
    public void onRequestRetry(int requestID, int processID) {

    }

    @Override
    public void onRequestCancel(int requestID, int processID) {

    }

    @Override
    protected void onWorkModeChange(int mode) {
        if(mode == ConnectionManager.WORKING_MODE_OFFLINE){
            this.txtOffline.setVisibility(View.VISIBLE);
        }else{
            this.txtOffline.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_offline:
                txtOffline.setVisibility(View.INVISIBLE);
                this.requestOnlineWorkingMode();
                break;
        }
    }

}
