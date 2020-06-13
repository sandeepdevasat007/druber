package com.xyz.druber.flight;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dji.ux.druber.R;
import com.xyz.druber.flight.adapter.Pager;
import com.xyz.druber.flight.fragment.MenuFragment;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.service.UserAPIService;
import com.xyz.druber.flight.utils.DataUtils;
import com.xyz.druber.flight.utils.NetworkUtils;
import com.xyz.druber.flight.utils.ScreenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SANDEEP
 **/


public class JobsActivity extends SideMenuBaseActivity implements TabLayout.OnTabSelectedListener {
    private UserAPIService userAPIService;
    @BindView(R.id.progress_bar)
    FrameLayout progress_bar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    /*    @BindView(R.id.toolbar)
        Toolbar toolbar;*/
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.error_Screen)
    RelativeLayout error_secreen;
    @BindView(R.id.err_Imgview)
    ImageView err_Imgview;
    @BindView(R.id.err_textView)
    TextView err_textView;
    @BindView(R.id.err_tagText)
    TextView err_tagText;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    private Unbinder unbinder;
    private Pager adapter;
    private boolean isFragmentLoaded;
    MenuFragment menuFragment;
    private TextView title;
    private ImageView menuButton;
    TextView logout_txt;
    private static boolean isAppStarted = false;
    private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
    private DJISDKManager.SDKManagerCallback registrationCallback = new DJISDKManager.SDKManagerCallback() {

        @Override
        public void onRegister(DJIError error) {
            isRegistrationInProgress.set(false);
            if (error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();

                Toast.makeText(getApplicationContext(), "SDK registration succeeded!", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(),
                        "SDK registration failed, check network and retry!",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProductDisconnect() {
            Toast.makeText(getApplicationContext(),
                    "product disconnect!",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProductConnect(BaseProduct product) {
            Toast.makeText(getApplicationContext(),
                    "product connect!",
                    Toast.LENGTH_LONG).show();


        }

        @Override
        public void onComponentChange(BaseProduct.ComponentKey key,
                                      BaseComponent oldComponent,
                                      BaseComponent newComponent) {
           /* Toast.makeText(getApplicationContext(),
                    key.toString() + " changed",
                    Toast.LENGTH_LONG).show();
*/
        }
    };
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int REQUEST_PERMISSION_CODE = 12345;
    private List<String> missingPermission = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAddlayout(R.layout.activity_missions);
        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        /*        setSupportActionBar(toolbar);*/
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorMaroon));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(JobsActivity.this);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        title = (TextView) findViewById(R.id.title_top);
        menuButton = (ImageView) findViewById(R.id.menu_icon);
        title.setText("Jobs List");
        checkAndRequestPermissions();
        ScreenUtils.createCubeProgressDialog(JobsActivity.this);
        ScreenUtils.showCubeDialogProgress();
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentLoaded) {
                    loadFragment();
                    title.setText("Profile");
                    viewPager.setAdapter(null);


                } else {
                    if (menuFragment != null) {
                        if (menuFragment.isAdded()) {
                            hideFragment();
                            load_JobsData();
                        }
                    }
                }
            }
        });
    }


    private void load_JobsData(){

        if (!NetworkUtils.isConnectingToInternet(this)) {
            progress_bar.setVisibility(View.GONE);
            ScreenUtils.getSnackbar(coordinatorLayout, "Please check internet connection!");
        } else {
            userAPIService.getMissionsList(DataUtils.getId(this)).enqueue(new Callback<ArrayList<Mission>>() {
                @Override
                public void onResponse(Response<ArrayList<Mission>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        ScreenUtils.dismissCubeDialogProgress();
                        ArrayList<Mission> missions = response.body();
                        Bundle missions_bundle = new Bundle();
                        missions_bundle.putParcelableArrayList("missions", missions);
                        adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(), missions);
                        if (missions.size() > 0) {
                            viewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(viewPager);
                        } else {
                            ScreenUtils.dismissCubeDialogProgress();
                            error_secreen.setVisibility(View.VISIBLE);
                            err_Imgview.setVisibility(View.INVISIBLE);
                            err_tagText.setVisibility(View.GONE);
                            err_textView.setText("NO JOBS FOUND....!");
                            err_textView.setTextSize(30);
                            viewPager.setAdapter(adapter);
                        }


                    } else {
                        try {
                            Log.i("Error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    ScreenUtils.dismissCubeDialogProgress();
                    viewPager.setAdapter(adapter);
                    error_secreen.setVisibility(View.VISIBLE);
                    Log.d("Failure:", "onFailure: " + t.getMessage());
                    //loadFragment();
                }
            });
        }

    }

    private void hideFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
        fragmentTransaction.remove(menuFragment);
        fragmentTransaction.commit();
        menuButton.setImageResource(R.drawable.ic_menu);
        isFragmentLoaded = false;
        title.setText("Jobs List");

    }

    private void loadFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        menuFragment = (MenuFragment) fm.findFragmentById(R.id.container);
        menuButton.setImageResource(R.drawable.ic_up_arrow);
        if (menuFragment == null) {
            menuFragment = new MenuFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            fragmentTransaction.add(R.id.container, menuFragment);
            fragmentTransaction.commit();
        }

        isFragmentLoaded = true;
    }


    public void onFragmentViewClick(View v) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null && fragment.isVisible()) {
            if (fragment instanceof MenuFragment) {
                ((MenuFragment) fragment).onViewClicked(v);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_JobsData();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        int position = tab.getPosition();
        if (position == 0)
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
        else if (position == 1) {
            viewPager.setCurrentItem(tab.getPosition());
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        } else
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public ViewPager getViewPager() {
        return viewPager;

    }


    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    private void checkAndRequestPermissions() {
        // Check for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {
            startSDKRegistration();
        } else {
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * Result of runtime permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check for granted permission and remove from missing list
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    missingPermission.remove(permissions[i]);
                }
            }
        }
        // If there is enough permission, we will start the registration
        if (missingPermission.isEmpty()) {
            startSDKRegistration();
        } else {
            Toast.makeText(getApplicationContext(), "Missing permissions! Will not register SDK to connect to aircraft.", Toast.LENGTH_LONG).show();
        }
    }

    private void startSDKRegistration() {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    DJISDKManager.getInstance().registerApp(JobsActivity.this, registrationCallback);
                }
            });
        }
    }


}