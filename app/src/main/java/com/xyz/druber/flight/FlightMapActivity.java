package com.xyz.druber.flight;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/*
  Created by SANDEEP
 */
/**
 * Activity that shows all the UI elements together
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FlightMapActivity extends Activity implements  View.OnClickListener, TextureView.SurfaceTextureListener {
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Marker marker;
    Polyline polylines;
    CalculateCircleDistance calculateCircleDistance;
    DjiActionperform djiActionperform = new DjiActionperform();
    WaypointConfiguaration waypointConfiguaration;
    private boolean cameraStarted = false;
    float imageDistance;
    private Handler handler;
    private MapWidget mapWidget;
    private static String TAG = "FlightMapActivity.this";
    private GoogleMap googleMap;
    private String latlng, flight_type;
    String aircraftHomeLatitude, aircraftHomeLongitude, aircraftHomeAltitude;
    private Button upload, start, stop, sattelite_Btn, street_Btn;
    private CameraKey sdCardAvailableSpace = CameraKey.create(CameraKey.SDCARD_REMAINING_SPACE_IN_MB);
    private CameraKey sdCardCaptureCountKey = CameraKey.create(CameraKey.SDCARD_AVAILABLE_CAPTURE_COUNT);
    private CameraKey sdCardInsertedKey = CameraKey.create(CameraKey.SDCARD_IS_INSERTED);
    private double droneLocationLat = 181, droneLocationLng = 181;
    //private double droneLocationLat = 181, droneLocationLng = 181;
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private Marker droneMarker = null;
    private float altitude = 50.0f;
    private float triggerSpeed;
    int gpslevel;
    double first_lat, first_long;
    private Waypoint mWaypoint;
    private UserAPIService userAPIService;
    private List<Waypoint> waypointList = new ArrayList<>();
    private MarkerOptions markerOptions = new MarkerOptions();
    private static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    private IntelligentHotpointMissionOperator hotspotinstance;
    private HotpointMissionOperator hotspotinstance1;
    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.TOWARD_POINT_OF_INTEREST;
    private List<LatLng> cameraPoints = new ArrayList<>();
    private ArrayList<LatLng> circle_points=new ArrayList<>();
    private Double aircraftLatitude;
    private Double aircraftLongitude;
    private Float aircraftYaw;
    TextView angle, overlap, speed_txt, sp_drone, sp_drones;
    SeekBar speed_skbar, angle_skbar;
    String mflighttpye;
    KmlLayer layer; //KML object.
    private FlightControllerKey aircraftLatitudeKey = FlightControllerKey.create(FlightControllerKey.AIRCRAFT_LOCATION_LATITUDE);
    private FlightControllerKey aircraftLongitudeKey = FlightControllerKey.create(FlightControllerKey.AIRCRAFT_LOCATION_LONGITUDE);
    private FlightControllerKey aircraftYawKey = FlightControllerKey.create(FlightControllerKey.ATTITUDE_YAW);
    private FlightControllerKey aircraftGpsSingalLevel = FlightControllerKey.create(FlightControllerKey.SATELLITE_COUNT);
    private FlightControllerKey aircraftHomePoint = FlightControllerKey.create(FlightControllerKey.IS_HOME_LOCATION_SET);
    private FlightControllerKey aircraftFlightModeString = FlightControllerKey.create(FlightControllerKey.FLIGHT_MODE_STRING);
    private FlightControllerKey aircraftCompassState = FlightControllerKey.create(FlightControllerKey.COMPASS_HAS_ERROR);
    private FlightControllerKey aircraftHomeLocationLatitude = FlightControllerKey.create(FlightControllerKey.HOME_LOCATION_LATITUDE);
    private FlightControllerKey aircraftHomeLocationLongitude = FlightControllerKey.create(FlightControllerKey.HOME_LOCATION_LONGITUDE);
    private FlightControllerKey aircraftHomeLocationAltitude = FlightControllerKey.create(FlightControllerKey.HOME_POINT_ALTITUDE);
    private CameraKey isShootingPhotoKey = CameraKey.create(CameraKey.IS_SHOOTING_SINGLE_PHOTO);
    private BatteryKey remainingBatteryPercentage = BatteryKey.create(BatteryKey.CHARGE_REMAINING_IN_PERCENT);
    private AirLinkKey airLinkKey = AirLinkKey.createWiFiLinkKey(AirLinkKey.WIFI_FREQUENCY_BAND);
    private static final int SDCARD_PERMISSION = 1,
            FOLDER_PICKER_CODE = 2,
            FILE_PICKER_CODE = 3;
    private ArrayList<LatLng> coordinates;
    private Mission mission;
    private FinalPath finalPath;
    Bitmap icon;
    NumberPicker numberPicker;
    int EARTH_RADIUS = 6371000;
    MissionControl.Listener listener;
    LinearLayout reset_layout;
    int radius = 100;
    double latitude = 0;
    double longitude = 0;
    BaseProduct product;
    int n;
    private float altitudeValue;
    private String provider;
    TextView gps_text, sdcard_text, altitude_text, drone_battery_percentage;
    private float zoom = 16;
    CheckBox connected_to_drone, camera_is_ready, home_point, missoin_uploading, switch_position, calibration, drone_storage, drone_gps;
    Button cancel_btn, resolve_issues_btn;
    int memory_Space, memory;
    String hieght;
    int bat_percentage;
    TextView home_point_tv;
    AlertDialog alertDialog;
    double last_lat, last_long;
    ArrayList<LatLng> updated_Latlng = new ArrayList<>();
    ArrayList<LatLng> configure_Latlng = new ArrayList<>();
    ArrayList<LatLng> circle_Latlng = new ArrayList<>();
    Polygon polygon;
    HotpointMission hotpointMission;
    HotpointAction hotpointAction;
    HotpointMissionOperator hotpointMissionOperator=new HotpointMissionOperator();
    int cameraSize,totalcam=0;
    boolean disableEvent;
    Button current_location, path_location, battery_btn, settings_btn, wifi_btn, info_btn, reset_button;
    double distance;
    private PopupWindow settingsPopupWindow, batteyPopupWindow;
    private boolean isSettings, isBattey;
    private int numberPickerLength = 160;
    private String[] numbers;
    RelativeLayout root_view;
    TextView altTv;
    LinearLayout dis_lnr;
    float mSpeed = 5.0f;
    int cameraAngle = 90;
    int noOfCaptureImages = 10;
    String wifisignal, alt_height, pop_gps;
    DisplayMetrics displaymetrics;
    Marker start_marker, end_marker;
    private MissionControl missionControl;
    private FlightController flightController;
    private TimelineEvent preEvent;
    private TimelineElement preElement;
    private DJIError preError;
    List<LatLng> setzoomLat;
    LatLngBounds.Builder boundsBuilder;
    LatLngBounds latLngBounds;
    int routePadding = 190;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_widgets);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        ScreenUtils.createCubeProgressDialog(FlightMapActivity.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(AirCraftApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);
        mapWidget = (MapWidget) findViewById(R.id.map_widget);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        handler = new Handler();
        mapWidget.initGoogleMap(new MapWidget.OnMapReadyListener() {
            @Override
            public void onMapReady(@NonNull DJIMap map) {
                googleMap = (GoogleMap) mapWidget.getMap().getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //googleMap.getUiSettings().setAllGesturesEnabled(false);
                sattelite_Btn.setVisibility(View.VISIBLE);
                if (getIntent().hasExtra(Mission.class.getName())) {
                    mission = getIntent().getExtras().getParcelable(Mission.class.getName());
                }
                if (getIntent().hasExtra(FinalPath.class.getName())) {
                    finalPath = getIntent().getExtras().getParcelable(FinalPath.class.getName());
                }
                if (getIntent().hasExtra(LatLng.class.getName())) {
                    coordinates = getIntent().getParcelableArrayListExtra(LatLng.class.getName());
                    Log.d("cord", coordinates.toString());
                    if (coordinates.size() > 0) {
                        loadFromWeb(googleMap, mapWidget);
                    }
                }
                if (getIntent().hasExtra("resume_action_latlng")) {
                    latlng = getIntent().getStringExtra("resume_action_latlng");
                }
                if (getIntent().hasExtra(finalPath.getGridtype())) {
                    flight_type = getIntent().getStringExtra(finalPath.getGridtype());
                    mflighttpye = finalPath.getGridtype();
                    if(mflighttpye != null ) {
                        if (mflighttpye.equalsIgnoreCase("singlegrid") || mflighttpye.equalsIgnoreCase("doublegrid")) {
                            reset_layout.setVisibility(View.INVISIBLE);
                            upload.setVisibility(View.INVISIBLE);
                            numberPicker.setVisibility(View.VISIBLE);
                            altTv.setVisibility(View.VISIBLE);
                        } else if (mflighttpye.equalsIgnoreCase("circulargrid")) {
                            reset_layout.setVisibility(View.INVISIBLE);
                            upload.setVisibility(View.INVISIBLE);
                            numberPicker.setVisibility(View.VISIBLE);
                            altTv.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        mapWidget.setFlightPathVisible(false);
        icon = BitmapFactory.decodeResource(getResources(), R.drawable.dronema);
        mapWidget.setAircraftBitmap(icon);
        mapWidget.onCreate(savedInstanceState);
        home_point_tv = (TextView) findViewById(R.id.home_point_tv);
        upload = (Button) findViewById(R.id.upload);
        reset_button = (Button) findViewById(R.id.reset_path);
        reset_layout=(LinearLayout)findViewById(R.id.reset_layout);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        current_location = (Button) findViewById(R.id.current_location);
        info_btn = (Button) findViewById(R.id.flight_info);
        settings_btn = (Button) findViewById(R.id.settings_btn);
        wifi_btn = (Button) findViewById(R.id.wifi_btn);
        path_location = (Button) findViewById(R.id.path_location);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        sattelite_Btn = (Button) findViewById(R.id.sattelite_Btn);
        street_Btn = (Button) findViewById(R.id.street_Btn);
        addListener();
        FrameLayout secondaryVideoView = (FrameLayout) findViewById(R.id.secondary_video_view);
        FPVWidget secondaryFPVWidget = findViewById(R.id.secondary_fpv_widget);
        numberPicker = findViewById(R.id.numberpicker);
        altTv = (TextView) findViewById(R.id.alt_tv);
        dis_lnr = (LinearLayout) findViewById(R.id.dis_lnr);
        root_view = findViewById(R.id.rl_root_view);
        initUIControlButtons();
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        numbers = new String[numberPickerLength - 9];
        for (int i = 0; i <= numberPickerLength - 10; i++) {
            numbers[i] = i + 10 + " " + "m";
        }
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMaxValue(numberPickerLength - 10);
        numberPicker.setMinValue(10);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int altValue) {
                altitude = altValue+0f;
                if (altitude > 10) {
                    double f = altValue;
                    float missionAltValue = altValue + 0F;

                    configWayPointMission(missionAltValue);


                }
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mFlightController!=null&&product.isConnected()) {
                    initTimeline();
                    drawCircle(new LatLng(droneLocationLat, droneLocationLng), 50);
                }

            }
        });
        sattelite_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                street_Btn.setVisibility(View.VISIBLE);
                sattelite_Btn.setVisibility(View.GONE);
            }
        });
        street_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                sattelite_Btn.setVisibility(View.VISIBLE);
                street_Btn.setVisibility(View.GONE);
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (batteyPopupWindow != null && batteyPopupWindow.isShowing()) {
                    batteyPopupWindow.dismiss();
                    isBattey = false;
                }
                if (!isSettings) {
                    isSettings = true;
                    final int width = (int) ((int) displaymetrics.widthPixels * 0.35);
                    final int height = (int) ((int) displaymetrics.heightPixels * 0.32);
                    View customView = getLayoutInflater().inflate(R.layout.settings_options_menu, null);
                    angle = customView.findViewById(R.id.angle_txt);
                    overlap = customView.findViewById(R.id.overlap_txt);
                    speed_txt = customView.findViewById(R.id.speed_txt);
                    sp_drone = customView.findViewById(R.id.sp_drone);
                    sp_drones = customView.findViewById(R.id.sp_drones);
                    angle_skbar = customView.findViewById(R.id.angle_seekbar);
                    speed_skbar = customView.findViewById(R.id.speed_skbar);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        updateSettingdUI(10, 90);
                        speed_skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if (speed_skbar.getProgress() > 40 && speed_skbar.getProgress() < 70) {
                                    mSpeed = 5.0f;
                                    speed_txt.setText("speed:\nmedium");

                                }
                                if (speed_skbar.getProgress() > 70) {
                                    mSpeed = 10.0f;
                                    speed_txt.setText("speed:\n fast");


                                }
                                if (speed_skbar.getProgress() > 1 && speed_skbar.getProgress() < 40) {
                                    mSpeed = 3.0f;
                                    speed_txt.setText("speed:\n low");

                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String ftype = null;
                        if (getIntent().hasExtra(finalPath.getGridtype())) {
                            flight_type = getIntent().getStringExtra(finalPath.getGridtype());
                            mflighttpye = finalPath.getGridtype();
                        }
                        if (mflighttpye != null&mflighttpye.equalsIgnoreCase("singlegrid")) {
                            updateSettingdUI(45, 90);
                            disableViews(true, true, true, true);
                            angle.setText(String.format("angle: \n 45 %s", (char) 0x00B0));
                            angle_skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int cam_angle, boolean b) {
                                    cameraAngle = cam_angle;
                                    int progress = ((int) Math.round(cam_angle / 5)) * 5;
                                    angle.setText(String.format("angle: \n" + progress + "%s", (char) 0x00B0));

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });
                        } else if (mflighttpye != null && mflighttpye.equalsIgnoreCase("doublegrid")) {
                            updateSettingdUI(10, 90);
                            disableViews(true, true, true, true);
                            angle.setText(String.format("angle: \n 10 %s", (char) 0x00B0));
                            angle_skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int cam_angle, boolean b) {
                                    cameraAngle = cam_angle;
                                    int progress = ((int) Math.round(cam_angle / 10)) * 10;
                                    angle.setText(String.format("angle: \n" + progress + "%s", (char) 0x00B0));
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });
                        } else if (mflighttpye != null && mflighttpye.equalsIgnoreCase("circulargrid")) {
                            updateSettingdUI(2, 20);
                            disableViews(false, false, false, false);
                            angle.setText(String.format("capture angle: \n 2 %s", (char) 0x00B0));
                            angle_skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int cam_angle, boolean b) {
                                    int progress = ((int) Math.round(cam_angle / 2)) * 2;
                                    if (updated_Latlng != null) {
                                        updated_Latlng.clear();
                                        polylines.remove();
                                        start_marker.remove();
                                        end_marker.remove();
                                    }
                                    angle.setText(String.format("capture angle: \n" + progress + "%s", (char) 0x00B0));
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });


                        }
                    }
                    overlap.setText(String.format("overlap: \n%s", String.format("%1$d%%", 80)));
                    settingsPopupWindow = new PopupWindow(customView, width, height);
                    settingsPopupWindow.showAtLocation(root_view, Gravity.TOP | Gravity.END, 10, 90);
                } else {
                    if (settingsPopupWindow.isShowing())
                        settingsPopupWindow.dismiss();
                    isSettings = false;
                }
            }
        });

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsPopupWindow != null && settingsPopupWindow.isShowing()) {
                    settingsPopupWindow.dismiss();
                    isSettings = false;
                }
                if (!isBattey) {
                    isBattey = true;
                    int width = (int) ((int) displaymetrics.widthPixels * 0.22);
                    int height = (int) ((int) displaymetrics.heightPixels * 0.42);
                    View customView = getLayoutInflater().inflate(R.layout.batery_options_menu, null);
                    batteyPopupWindow = new PopupWindow(customView, width, height);
                    gps_text = customView.findViewById(R.id.gps);
                    drone_battery_percentage = customView.findViewById(R.id.aircraft_percentage);
                    sdcard_text = customView.findViewById(R.id.sdcard);
                    altitude_text = customView.findViewById(R.id.alt);
                    TextView speed_text = customView.findViewById(R.id.speeds);
                    if (mFlightController != null) {
                        speed_text.setText(String.valueOf(mSpeed));
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                sdcard_text.setText(String.valueOf(memory));
                                gps_text.setText(String.valueOf(gpslevel));
                                altitude_text.setText(alt_height);
                                drone_battery_percentage.setText(String.valueOf(bat_percentage) + "%");
                                handler.postDelayed(this, 1000);
                            }
                        }, 1000);


                    }
                    batteyPopupWindow.showAtLocation(root_view, Gravity.TOP | Gravity.RIGHT, 10, 90);
                } else {
                    if (batteyPopupWindow.isShowing())
                        batteyPopupWindow.dismiss();
                    isBattey = false;
                }
            }

        });

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraUpdate();
            }
        });

        path_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerOptions.position(new LatLng(first_lat, first_long));
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(coordinates.get(0).latitude, coordinates.get(0).longitude), zoom);
                googleMap.moveCamera(cu);
            }
        });
    }

    private void setMapFitZoom(ArrayList<LatLng> coordinate) {
        setzoomLat=coordinate;
        boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : setzoomLat)
            boundsBuilder.include(latLngPoint);
        latLngBounds = boundsBuilder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }


    private void initTimeline() {
        List<TimelineElement> elements = new ArrayList<>();
        cameraSize=circle_Latlng.size();
        missionControl = MissionControl.getInstance();
        final TimelineEvent preEvent = null;
        listener = new MissionControl.Listener() {
            @Override
            public void onEvent(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {
                updateTimelineStatus(element, event, error);
            }
        };

        if(listener!=null){
            addListener2();
            setResultToToast("listener");
        }
        elements.add(new TakeOffAction());
        hotpointMission = new HotpointMission();
        hotpointMission.setHotpoint(new LocationCoordinate2D(droneLocationLat, droneLocationLng));
        hotpointMission.setAltitude(50);
        hotpointMission.setRadius(50);
        hotpointMission.setAngularVelocity(10);
        HotpointStartPoint startPoint = HotpointStartPoint.EAST;
        hotpointMission.setStartPoint(startPoint);
        HotpointHeading heading = HotpointHeading.TOWARDS_HOT_POINT;
        hotpointMission.setHeading(heading);
        hotpointMission.setClockwise(true);
        hotpointAction=new HotpointAction(hotpointMission,360);

        elements.add(hotpointAction);
        if (missionControl.scheduledCount() > 0) {
            missionControl.unscheduleEverything();
            missionControl.removeAllListeners();
        }

        missionControl.scheduleElements(elements);
        missionControl.addListener(listener);
    }
    private void updateTimelineStatus(@Nullable TimelineElement element, final TimelineEvent event, DJIError error) {
        if (element == preElement && event == preEvent && error == preError) {
            return;
        }
        if (element != null) {
            if (element instanceof TimelineMission) {

            } else {


            }
        } else {
            setResultToToast("Circular Mission is " + event.toString() + " " + (error == null
                    ? ""
                    : "Failed:"
                    + error.getDescription()));
            preEvent = event;
            preElement = element;
            preError = error;
        }
    }
    private void enableLocationPlugin(GoogleMap googleMap, MapWidget mapWidget) {
        initializeLocationEngine(googleMap,mapWidget);

    }
    private void initializeLocationEngine(GoogleMap googleMap, MapWidget mapWidget) {
        if (latlng != null && !latlng.isEmpty()) {
            String[] latlong = latlng.split(",");
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(latlong[0]));
            location.setLongitude(Double.parseDouble(latlong[1]));
            setCameraPosition(location,mapWidget);
        }
    }

    private void setCameraPosition(Location location, MapWidget mapWidget) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    private void loadFromWeb(GoogleMap googleMap, MapWidget mapWidget) {
        distanceCal();
        Log.i(TAG, "computeArea " + SphericalUtil.computeArea(coordinates));
        //setResultToToast("Area" + SphericalUtil.computeArea(coordinates));
        enableLocationPlugin(googleMap,mapWidget);
        List<LatLng> positions = new ArrayList<>();
        positions.add(coordinates.get(0));
        positions.add(coordinates.get(coordinates.size() - 1));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng position : positions) {
            builder.include(position);
        }
        LatLngBounds initialBounds = builder.build();

        n = coordinates.size();

        for (LatLng point : coordinates) {
            latitude += point.latitude;
            longitude += point.longitude;
        }
        LatLng targetNorteast = SphericalUtil.computeOffset(initialBounds.northeast, distance * Math.sqrt(2), 45);
        LatLng targetSouthwest = SphericalUtil.computeOffset(initialBounds.southwest, distance * Math.sqrt(2), 225);
        builder.include(targetNorteast);
        builder.include(targetSouthwest);
        LatLngBounds finalBounds = builder.build();
        first_lat = coordinates.get(0).latitude;
        first_long = coordinates.get(0).longitude;
        last_lat = coordinates.get(coordinates.size() - 1).latitude;
        last_long = coordinates.get(coordinates.size() - 1).longitude;
        drawCircle(new LatLng(droneLocationLat,droneLocationLng),20);

    }
    private double getLongitudinalPictureDistance() {
        return getLongitudinalFootPrint() * (1 - 70 * .01);

    }

    private double getLongitudinalFootPrint() {
        return altitude * 4.22
                / 5.0;
    }
    private void initUIControlButtons() {
/*
        upload.setVisibility(View.VISIBLE);
*/

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };

    private void onProductConnectionChange() {
        initFlightController();
        loginAccount();
    }

    private void loginAccount() {

        UserAccountManager.getInstance().logIntoDJIUserAccount(this,
                new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
                    @Override
                    public void onSuccess(final UserAccountState userAccountState) {
                        Log.e(TAG, "Login Success");
                    }

                    @Override
                    public void onFailure(DJIError error) {
                        setResultToToast("Login Error:"
                                + error.getDescription());
                    }
                });
    }


    private void setResultToToast(final String string) {
        FlightMapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FlightMapActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFlightController();
        mapWidget.onResume();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog alertDialog = new AlertDialog.Builder(FlightMapActivity.this).create();
        alertDialog.setMessage("Are you sure want to exit?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


    @Override
    protected void onPause() {
        mapWidget.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapWidget.onDestroy();

        super.onDestroy();
        unregisterReceiver(mReceiver);
        removeListener();
    }

    /**
     * @Description : RETURN Button RESPONSE FUNCTION
     */
    public void onReturn(View view) {
        Log.d(TAG, "onReturn");
        this.finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapWidget.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapWidget.onLowMemory();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private void initFlightController() {
        product = AirCraftApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            upload.setVisibility(View.VISIBLE);
            //setMapFitZoom(coordinates);
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {

                @Override
                public void onUpdate(FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                    updateDroneLocation();
                }
            });
            mFlightController.getSerialNumber(new CommonCallbacks.CompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });


            DJISDKManager.getInstance().getProduct().getName(new CommonCallbacks.CompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    String product_name=s;
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });


            DJISDKManager.getInstance().getKeyManager().getValue(aircraftHomeLocationLatitude, new GetCallback() {
                @Override
                public void onSuccess(Object o) {
                    aircraftHomeLatitude = String.valueOf(o);
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });


            DJISDKManager.getInstance().getKeyManager().getValue(aircraftHomeLocationLatitude, new GetCallback() {
                @Override
                public void onSuccess(Object o) {
                    aircraftHomeLatitude = String.valueOf(o);
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });


            DJISDKManager.getInstance().getKeyManager().getValue(aircraftHomeLocationLongitude, new GetCallback() {
                @Override
                public void onSuccess(Object o) {
                    aircraftHomeLongitude = String.valueOf(o);
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            DJISDKManager.getInstance().getKeyManager().getValue(aircraftHomeLocationAltitude, new GetCallback() {
                @Override
                public void onSuccess(Object o) {
                    aircraftHomeAltitude = String.valueOf(o);
                }

                @Override
                public void onFailure(DJIError djiError) {
                }
            });

            if ((Double.parseDouble(aircraftHomeLatitude) > -90 && Double.parseDouble(aircraftHomeLatitude) < 90 && Double.parseDouble(aircraftHomeLongitude) > -180 && Double.parseDouble(aircraftHomeLongitude) < 180) && (Double.parseDouble(aircraftHomeLatitude) != 0f && Double.parseDouble(aircraftHomeLongitude) != 0f)) {

                home_point_tv.setText("Lat: " + aircraftHomeLatitude + ",Lng : " + aircraftHomeLongitude + ",Altitude : " + aircraftHomeAltitude);
            }
        }
    }
    private float distanceCal() {
        Location loc1 = new Location(provider);
        loc1.setLatitude(17.468890);
        loc1.setLongitude(78.399190);

        Location loc2 = new Location(provider);
        loc2.setLatitude(17.46952533155917);
        loc2.setLongitude(78.39599657497669);

        float distanceInMeters = loc1.distanceTo(loc2);
        distance= distanceInMeters;
        return distanceInMeters;
    }

    private void addListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().addListener(eventNotificationListener);
        }
    }

    private void removeListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().removeListener(eventNotificationListener);
        }
    }



    //Add Listener for pointMissionOperator
    private void addListener2() {
        if (getHotpointMissionOperator() != null) {
            getHotpointMissionOperator().addListener(eventNotificationListener2);
        }
    }


    private IntelligentHotpointMissionOperatorListener eventNotificationListener1 = new IntelligentHotpointMissionOperatorListener() {
        @Override
        public void onExecutionUpdate(@NonNull IntelligentHotpointMissionEvent intelligentHotpointMissionEvent) {

        }

        @Override
        public void onExecutionStart() {

        }

        @Override
        public void onExecutionFinish(@Nullable DJIError djiError) {
            getIntelligentHotpointMissionOperator().removeListener(eventNotificationListener1);

        }
    };




    private HotpointMissionOperatorListener  eventNotificationListener2 = new HotpointMissionOperatorListener() {

        @Override
        public void onExecutionUpdate(@NonNull final HotpointMissionEvent hotpointMissionEvent) {
            if(hotpointMissionEvent.getCurrentState().equals(HotpointMissionState.EXECUTING))
                if(circle_points.size()>0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Camera camera = DJIBaseApplication.getCameraInstance();
                            if (camera != null) {
                                SettingsDefinitions.ShootPhotoMode photoMode = SettingsDefinitions.ShootPhotoMode.SINGLE; // Set the camera capture mode as Single mode
                                camera.setShootPhotoMode(photoMode, new CommonCallbacks.CompletionCallback(){
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (null == djiError) {
                                            camera.startShootPhoto(new CommonCallbacks.CompletionCallback() {
                                                @Override
                                                public void onResult(DJIError djiError) {
                                                    if (djiError == null) {
                                                        circle_points.remove(0);
                                                    } else {
                                                        setResultToToast(djiError.getDescription());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                        }
                    });
                }
        }

        @Override
        public void onExecutionStart() {
        }

        @Override
        public void onExecutionFinish(@Nullable DJIError djiError) {
            getHotpointMissionOperator().removeListener(eventNotificationListener2);
            stopCamera();
        }
    };





    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
        @Override
        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {

        }

        @Override
        public void onUploadUpdate(final WaypointMissionUploadEvent uploadEvent) {
            if (uploadEvent.getCurrentState().equals(WaypointMissionState.READY_TO_EXECUTE)) {
                rotateGimbal(Float.parseFloat("-"+cameraAngle+".0f"), 0.1);
                if (uploadEvent.getProgress() != null) {
                }
            }

        }

        @Override
        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {

            if (executionEvent.getProgress() != null && executionEvent.getProgress().targetWaypointIndex == 1) {

                if (!cameraStarted) {
                    cameraStarted = true;

                }

            }
             if(executionEvent.getProgress().targetWaypointIndex>0){
                final Camera camera = DJIBaseApplication.getCameraInstance();
                if (camera != null) {
                    SettingsDefinitions.ShootPhotoMode photoMode = SettingsDefinitions.ShootPhotoMode.SINGLE; // Set the camera capture mode as Single mode
                    camera.setShootPhotoMode(photoMode, new CommonCallbacks.CompletionCallback(){
                        @Override
                        public void onResult(DJIError djiError) {
                            if (null == djiError) {
                                camera.startShootPhoto(new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {
                                        if (djiError == null) {
                                        } else {
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
            if (executionEvent.getProgress().totalWaypointCount - 1 == executionEvent.getProgress().targetWaypointIndex) {
                setResultToToast("Mission Completed Successfully");
                stopAction();
                stopCamera();

            }

        }

        @Override
        public void onExecutionStart() {

        }

        @Override
        public void onExecutionFinish(@Nullable final DJIError error) {
            stopCamera();
            getWaypointMissionOperator().removeListener(eventNotificationListener);
            upload.setVisibility(View.INVISIBLE);
        }

    };
    private void rotateGimbal(float pitchAngle, double rotateTime) {
        Gimbal gimbal = DJIBaseApplication.getAircraftInstance().getGimbal();
        Rotation.Builder rotationBuilder = new Rotation.Builder();
        rotationBuilder.mode(RotationMode.ABSOLUTE_ANGLE);
        rotationBuilder.pitch(pitchAngle);
        rotationBuilder.time(rotateTime);

        Rotation rotation = rotationBuilder.build();
        gimbal.rotate(rotation, new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null) {
                    Log.i(TAG, "Successfully rotated gimbal");
                }
            }
        });

    }

    private void stopCamera() {
        final Camera camera = DJIBaseApplication.getCameraInstance();
        if (cameraStarted) {
            if (camera != null) {
                camera.stopShootPhoto(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            Log.e(TAG, "Camera Stopped");
                            cameraStarted = false;
                        } else {
                            Log.e(TAG, "stop camera error: " + djiError.getDescription());
                        }
                    }
                });
            }
        }
    }

    private void stopAction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doPathAction(mission.get_id(), finalPath.getId(), "END",null, "Mission Completed Successfully!");
            }
        });
    }

    private WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            if (DJISDKManager.getInstance().getMissionControl() != null) {
                instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
            }
        }
        return instance;
    }



    private IntelligentHotpointMissionOperator getIntelligentHotpointMissionOperator() {
        if (hotspotinstance == null) {
            if (DJISDKManager.getInstance().getMissionControl() != null) {
                hotspotinstance = DJISDKManager.getInstance().getMissionControl().getIntelligentHotpointMissionOperator();
            }
        }
        return hotspotinstance;
    }
    private HotpointMissionOperator getHotpointMissionOperator() {
        if (hotspotinstance1 == null) {
            if (DJISDKManager.getInstance().getMissionControl() != null) {
                hotspotinstance1 = DJISDKManager.getInstance().getMissionControl().getHotpointMissionOperator();
            }
        }
        return hotspotinstance1;
    }
    private static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }
    // Update the drone location based on states from MCU.
    private void updateDroneLocation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (droneMarker != null) {
                    droneMarker.remove();

                }
                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    home_point_tv.setText("Lat:" + droneLocationLat + ",Lng:" + droneLocationLng);
                    DJISDKManager.getInstance().getKeyManager().getValue(FlightControllerKey.create(FlightControllerKey.IS_GOING_HOME), new GetCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            if (String.valueOf(o).equalsIgnoreCase("true")) {
                                DJISDKManager.getInstance().getKeyManager().getValue(FlightControllerKey.create(FlightControllerKey.ALTITUDE), new GetCallback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        hieght = String.valueOf(o);
                                        if (hieght.equalsIgnoreCase("5.0")) {
                                            mFlightController.cancelGoHome(new CommonCallbacks.CompletionCallback() {
                                                @Override
                                                public void onResult(DJIError djiError) {
                                                    if (djiError == null) {
                                                    }

                                                }

                                            });


                                        }
                                    }

                                    @Override
                                    public void onFailure(DJIError djiError) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                        }
                    });

                    DJISDKManager.getInstance().getKeyManager().getValue(sdCardAvailableSpace, new GetCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            String aircraftFlightModeString = String.valueOf(o);
                            memory_Space = Integer.parseInt(aircraftFlightModeString);
                            DJISDKManager.getInstance().getKeyManager().getValue(FlightControllerKey.create(FlightControllerKey.ALTITUDE), new GetCallback() {
                                @Override
                                public void onSuccess(Object o) {
                                    alt_height = String.valueOf(o);

                                }

                                @Override
                                public void onFailure(DJIError djiError) {
                                }
                            });

                            if (memory_Space == 0) {
                                djiActionperform.performAction(getApplicationContext());
                                String latlng = "17.4275148,78.4105523";
                                doPathAction(mission.get_id(), finalPath.getId(), "PAUSE", latlng, "Memory full.Mission Paused!");
                            } else {
                                memory=memory_Space;
                            }
                        }
                        @Override
                        public void onFailure(DJIError djiError) {
                        }
                    });
                    DJISDKManager.getInstance().getKeyManager().getValue(remainingBatteryPercentage, new GetCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            String remainigbattery = String.valueOf(o);
                            bat_percentage = Integer.parseInt(remainigbattery);
                            Button batery_btn = (Button) findViewById(R.id.battery_btn);
                            if (bat_percentage > 75 && bat_percentage < 100) {
                                batery_btn.setBackground(getResources().getDrawable(R.drawable.bhd));
                            } else if (bat_percentage > 55 && bat_percentage < 75) {
                                batery_btn.setBackground(getResources().getDrawable(R.drawable.bez));
                            } else if (bat_percentage > 35 && bat_percentage < 55) {
                                batery_btn.setBackground(getResources().getDrawable(R.drawable.bsz));
                            } else if (bat_percentage > 15 && bat_percentage < 35) {
                                batery_btn.setBackground(getResources().getDrawable(R.drawable.bfz));
                            } else if (bat_percentage > 10 && bat_percentage < 20) {
                                upload.setVisibility(View.INVISIBLE);
                                djiActionperform.performAction(getApplicationContext());
                                String latlng = "17.4275148,78.4105523";
                                doPathAction(mission.get_id(), finalPath.getId(), "PAUSE", latlng, "Mission Paused!");
                                batery_btn.setBackground(getResources().getDrawable(R.drawable.bfft));
                            }

                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                        }
                    });

                    DJISDKManager.getInstance().getKeyManager().getValue(airLinkKey, new GetCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            wifisignal = String.valueOf(o);
                        }

                        @Override
                        public void onFailure(DJIError djiError) {
                        }
                    });

                }


            }

        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.upload: {
                if(upload.getText().toString().equalsIgnoreCase("upload")){
                    uploadWayPointMission();
                }
                else if(upload.getText().toString().equalsIgnoreCase("start")){
                    popCheckListDialog();
                }

                else if(upload.getText().toString().equalsIgnoreCase("stop")){
                    // TODO Auto-generated method stub
                    AlertDialog alertDialog = new AlertDialog.Builder(FlightMapActivity.this).create();
                    alertDialog.setMessage("Are you sure  want to  cancel mission?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    djiActionperform.performAction(getApplicationContext());
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                break;
            }
            case R.id.start: {
                popCheckListDialog();
                break;
            }
            case R.id.stop: {
                // TODO Auto-generated method stub
                AlertDialog alertDialog = new AlertDialog.Builder(FlightMapActivity.this).create();
                //alertDialog.setTitle("");
                alertDialog.setMessage("Are you sure want to to cancel mission?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                djiActionperform.performAction(getApplicationContext());
                                dialog.dismiss();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            }
            default:
                break;
        }
    }
    private void popCheckListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FlightMapActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.checklist_layout1, null);
        connected_to_drone = (CheckBox) dialogView.findViewById(R.id.connected_to_drone_chk);
        camera_is_ready = (CheckBox) dialogView.findViewById(R.id.camera_is_ready_chk);
        home_point = (CheckBox) dialogView.findViewById(R.id.home_point_chk);
        missoin_uploading = (CheckBox) dialogView.findViewById(R.id.missoin_uploading_chk);
        switch_position = (CheckBox) dialogView.findViewById(R.id.switch_position_chk);
        calibration = (CheckBox) dialogView.findViewById(R.id.calibration_chk);
        drone_storage = (CheckBox) dialogView.findViewById(R.id.drone_storage_chk);
        drone_gps = (CheckBox) dialogView.findViewById(R.id.drone_gps_chk);
        cancel_btn = (Button) dialogView.findViewById(R.id.cancel_btn);
        resolve_issues_btn = (Button) dialogView.findViewById(R.id.resolve_issues_btn);
        dialogBuilder.setView(dialogView);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) ((int) displaymetrics.widthPixels * 0.8);
        int height = (int) ((int) displaymetrics.heightPixels * 0.9);
        alertDialog = dialogBuilder.create();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(width, height);

        handler.postDelayed(new Runnable() {
            public void run() {
                checklistData();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


    private void cameraUpdate() {
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoom);
        googleMap.moveCamera(cu);

    }

    private void configWayPointMission(float missionAltValue) {
        ScreenUtils.showCubeDialogProgress();
        waypointList.clear();
        imageDistance = (float) getLongitudinalPictureDistance();
        triggerSpeed= imageDistance/mSpeed;
        if(circle_points.size()>0) {
            for (LatLng latLng : circle_points) {
                mWaypoint = new Waypoint(latLng.latitude, latLng.longitude, missionAltValue);
                //Add Waypoints to Waypoint arraylist;
                if (waypointMissionBuilder != null) {
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size()).setPointOfInterest(new LocationCoordinate2D(droneLocationLat, droneLocationLng));
                } else {
                    waypointMissionBuilder = new WaypointMission.Builder();
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size()).setPointOfInterest(new LocationCoordinate2D(droneLocationLat, droneLocationLng));
                }
            }

            WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
            if (waypointMissionBuilder == null) {
                waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                        .headingMode(mHeadingMode)
                        .autoFlightSpeed(mSpeed)
                        .maxFlightSpeed(mSpeed)
                        .setPointOfInterest(new LocationCoordinate2D(droneLocationLat, droneLocationLng))
                        .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

            } else {
                mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
                waypointMissionBuilder.finishedAction(mFinishedAction)
                        .headingMode(mHeadingMode)
                        .autoFlightSpeed(mSpeed)
                        .maxFlightSpeed(mSpeed)
                        .setPointOfInterest(new LocationCoordinate2D(droneLocationLat, droneLocationLng))
                        .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

            }

            if (DJIBaseApplication.isFirmwareNewVersion() != null && DJIBaseApplication.isFirmwareNewVersion()) {
                if (waypointMissionBuilder.getWaypointList().size() > 0) {
                    for (int i = 0; i < waypointMissionBuilder.getWaypointList().size(); i++) {
                        waypointMissionBuilder.getWaypointList().get(i).altitude = missionAltValue;
                        waypointMissionBuilder.getWaypointList().get(i).shootPhotoDistanceInterval
                                = triggerSpeed;
                        waypointMissionBuilder.getWaypointList().get(i).cornerRadiusInMeters = 0.3f;
                        waypointMissionBuilder.getWaypointList().get(i).gimbalPitch = -90f;
                    }
                }
            } else {
                float triggerSpeed = imageDistance/mSpeed;
                initCamera((int) triggerSpeed);
            }
        }
        DJIError error = getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
        if (error == null) {
            setResultToToast("Waypoints loaded sucessfully");
            upload.setVisibility(View.VISIBLE);
            ScreenUtils.dismissCubeDialogProgress();
        } else {
            setResultToToast("loadWaypoint failed " + waypointList.size() + error.getDescription());
            ScreenUtils.dismissCubeDialogProgress();
        }

    }
        /*
    Old firmware camera controls
     */

    private void initCamera(int photoTime) {
        final Camera camera = DJIBaseApplication.getCameraInstance();
        if (camera != null) {
            SettingsDefinitions.ShootPhotoMode photoMode = SettingsDefinitions.ShootPhotoMode.INTERVAL;
            SettingsDefinitions.PhotoTimeIntervalSettings photoTimeIntervalSettings = new SettingsDefinitions.PhotoTimeIntervalSettings(255, photoTime);
            camera.setShootPhotoMode(photoMode, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                    } else {
                    }

                }
            });

            camera.setPhotoTimeIntervalSettings(photoTimeIntervalSettings, new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                    }
                }
            });

        }
    }



    private LatLng getPoint(LatLng center, int radius, double angle) {
        // Get the coordinates of a circle point at the given angle
        double east = radius * Math.cos(angle);
        double north = radius * Math.sin(angle);

        double cLat = center.latitude;
        double cLng = center.longitude;
        double latRadius = EARTH_RADIUS * Math.cos(cLat / 180 * Math.PI);

        double newLat = cLat + (north / EARTH_RADIUS / Math.PI * 180);
        double newLng = cLng + (east / latRadius / Math.PI * 180);

        return new LatLng(newLat, newLng);
    }

    public Polyline drawCircle(LatLng center, int radius) {
        // Clear the map to remove the previous circle
        // Generate the points
        if(polylines!=null){
            polylines.remove();
            start_marker.remove();
            end_marker.remove();
        }
        circle_points = new ArrayList<LatLng>();
        int totalPonts = 20; // number of corners of the pseudo-circle
        for (int i = 0; i < totalPonts; i++) {
            circle_points.add(getPoint(center, 30, i*2*Math.PI/totalPonts));

        }
        Toast.makeText(getApplicationContext(),"Toatal"+totalPonts,Toast.LENGTH_LONG).show();
        double startLat = circle_points.get(0).latitude;
        double startLng = circle_points.get(0).longitude;
        double endStart = circle_points.get(circle_points.size() - 1).latitude;
        double endLng = circle_points.get(circle_points.size() - 1).longitude;
        markerOptions.position(new LatLng(startLat, startLng));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.path_start));
        start_marker = googleMap.addMarker(markerOptions);
        markerOptions.position(new LatLng(endStart,endLng));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.path_end));
        end_marker = googleMap.addMarker(markerOptions);
        polylines=googleMap.addPolyline(new PolylineOptions().addAll(circle_points).width(5f).color(R.color.dronePathColor));
        setMapFitZoom(circle_points);
        // Create and return the polygon
        return polylines;
    }
    private void uploadWayPointMission() {
        if(altitude>0.0) {
            ScreenUtils.showCubeDialogProgress();
            getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        ScreenUtils.dismissCubeDialogProgress();
                        setResultToToast("Mission uploaded successfully!");
                        upload.setText("start");
                        upload.setBackground(getResources().getDrawable(R.drawable.green_cornershapes));
                        dis_lnr.setVisibility(View.INVISIBLE);
                    } else {
                        ScreenUtils.dismissCubeDialogProgress();
                        setResultToToast(error.getDescription());
                        getWaypointMissionOperator().retryUploadMission(null);

                    }
                }
            });
        }
        else{
            setResultToToast("Please Set The Altitude!");
        }
    }

    private void startWaypointMission() {

        checkSdCardInserted();
    }


    private void checkSdCardInserted() {
        DJISDKManager.getInstance().getKeyManager().getValue(sdCardInsertedKey, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof Boolean) {
                    if ((Boolean) o) {
                        checkSdCardSpaceAvailable();

                    } else {
                        setResultToToast("Error: Cannot find SD card");

                    }
                }
            }

            @Override
            public void onFailure(DJIError djiError) {

                setResultToToast(djiError.getDescription());
            }
        });
    }

    public void checklistData() {

        if (mFlightController != null&&product!=null&&product.isConnected()) {
            connected_to_drone.setChecked(true);
            connected_to_drone.setText(" Connected to drone");
            connected_to_drone.setTextColor(getResources().getColor(R.color.colorLightGreen));
        }
        else{
            connected_to_drone.setChecked(false);
            connected_to_drone.setText(" Not connected to drone");
        }

        missoin_uploading.setChecked(true);
        missoin_uploading.setText("Mission uploaded Successfully");
        missoin_uploading.setTextColor(getResources().getColor(R.color.colorLightGreen));
        DJISDKManager.getInstance().getKeyManager().getValue(aircraftGpsSingalLevel, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                String aircraftGpsLevel = String.valueOf(o);
                gpslevel = Integer.parseInt(aircraftGpsLevel);
                if (gpslevel > 0) {
                    drone_gps.setChecked(true);
                    drone_gps.setText("Drone GPS satillites :" + "(" + "15/" + gpslevel + ")");
                    drone_gps.setTextColor(getResources().getColor(R.color.colorLightGreen));

                }

            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });
        DJISDKManager.getInstance().getKeyManager().getValue(aircraftHomePoint, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                String aircraftHomePoint = String.valueOf(o);
                if (aircraftHomePoint.equalsIgnoreCase("true")) {
                    home_point.setChecked(true);
                    home_point.setText("Homepoint is set");
                    home_point.setTextColor(getResources().getColor(R.color.colorLightGreen));

                }
                else{
                    home_point.setChecked(false);
                    home_point.setText("Homepoint is not set");

                }
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });

        DJISDKManager.getInstance().getKeyManager().getValue(aircraftFlightModeString, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                String aircraftFlightModeString = String.valueOf(o);
                if (aircraftFlightModeString.equalsIgnoreCase("Gps")) {
                    switch_position.setChecked(true);
                    switch_position.setText("Switch is in \"P\" position");
                    switch_position.setTextColor(getResources().getColor(R.color.colorLightGreen));

                }
                else{
                    switch_position.setChecked(false);
                    switch_position.setText("Switch is not in \"P\" position");

                }
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });


        DJISDKManager.getInstance().getKeyManager().getValue(aircraftCompassState, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                String aircraftCompassState = String.valueOf(o);
                if (aircraftCompassState.equalsIgnoreCase("false")) {
                    calibration.setChecked(true);
                    calibration.setText("Drone is calibrated");
                    calibration.setTextColor(getResources().getColor(R.color.colorLightGreen));

                }
                else {
                    calibration.setChecked(false);
                    calibration.setText("Drone is not calibrated");

                }
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });


        DJISDKManager.getInstance().getKeyManager().getValue(sdCardAvailableSpace, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                String aircraftFlightModeString = String.valueOf(o);
                memory_Space = Integer.parseInt(aircraftFlightModeString);
                if (memory_Space > 0) {
                    drone_storage.setChecked(true);
                    drone_storage.setText("Drone SD card " + "(" + memory_Space + "MB found");
                    drone_storage.setTextColor(getResources().getColor(R.color.colorLightGreen));
                }
                else {
                    drone_storage.setChecked(false);
                    drone_storage.setText("Drone storage status unknown");
                }
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });


        final Camera camera = DJIBaseApplication.getCameraInstance();
        if (camera != null) {
            camera_is_ready.setChecked(true);
            camera_is_ready.setText("Camera is ready");
            camera_is_ready.setTextColor(getResources().getColor(R.color.colorLightGreen));
        } else {
            camera_is_ready.setChecked(false);
            camera_is_ready.setText("Camera not ready");
        }


        if (connected_to_drone.isChecked() == true && home_point.isChecked() == true && camera_is_ready.isChecked() == true && drone_storage.isChecked() == true && drone_gps.isChecked() == true && calibration.isChecked() == true && switch_position.isChecked() == true && missoin_uploading.isChecked() == true) {

            resolve_issues_btn.setEnabled(true);
            resolve_issues_btn.setBackgroundColor(getResources().getColor(R.color.colorLightGreen));
            resolve_issues_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (memory_Space >100) {
                        startWaypointMission();
                        alertDialog.dismiss();
                    } else {
                        setResultToToast("SD card insufficient memory.");
                        alertDialog.dismiss();
                    }
                }
            });
        } else {
            resolve_issues_btn.setBackgroundColor(getResources().getColor(R.color.red));
            resolve_issues_btn.setEnabled(false);
        }

    }

    private void checkSdCardSpaceAvailable() {
        DJISDKManager.getInstance().getKeyManager().getValue(sdCardCaptureCountKey, new GetCallback() {
            @Override
            public void onSuccess(Object value) {
                getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError error) {
                        setResultToToast("Mission Started: " + (error == null ? "Successfully" : error.getDescription()));
                        if (error == null) {
                            KeyManager.getInstance().addListener(isShootingPhotoKey, isShootingPhotoListener);
                            rotateGimbal(Float.parseFloat("-" + cameraAngle + ".0f"), 0.1);
                            upload.setText("stop");
                            upload.setBackground(getResources().getDrawable(R.drawable.red_cornershapes));
                            disableEvent = false;

                        } else {
                            setResultToToast(error.getDescription());

                        }
                    }
                });
            }

            @Override
            public void onFailure(DJIError djiError) {
                setResultToToast(djiError.getDescription());
            }
        });
    }

    private KeyListener isShootingPhotoListener = new KeyListener() {
        @Override
        public void onValueChange(Object o, Object o1) {
            if (o1 instanceof Boolean) {
                if ((Boolean) o1) {
                    aircraftLatitude = null;
                    aircraftLongitude = null;
                    getDroneLocation();
                }
            }
        }
    };

    private void setImagePosition(final MapWidget mapWidget) {
        if (aircraftLatitude != null && aircraftLongitude != null && aircraftYaw != null) {
            LatLng triggerLocation = new LatLng(aircraftLatitude, aircraftLongitude);
            cameraPoints.add(triggerLocation);
            Log.d("trigger", "trigger" + cameraPoints);
            if (cameraPoints.size() > 0) {
                if(cameraPoints.size()==1){
                    doPathAction(mission.get_id(), finalPath.getId(), "START", null,"Action START Send Successfully!");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        markerOptions.position(cameraPoints.get(cameraPoints.size() - 1));
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rcam));
                        googleMap.addMarker(markerOptions).setRotation(aircraftYaw);

                    }
                });
            }
            System.out.print("trs" + cameraPoints.size());

        }
    }

    private void getDroneLocation() {
        DJISDKManager.getInstance().getKeyManager().getValue(aircraftLatitudeKey, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                aircraftLatitude = (Double) o;
                setImagePosition(mapWidget);
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });

        DJISDKManager.getInstance().getKeyManager().getValue(aircraftLongitudeKey, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                aircraftLongitude = (Double) o;
                setImagePosition(mapWidget);
            }

            @Override
            public void onFailure(DJIError djiError) {
            }
        });

        DJISDKManager.getInstance().getKeyManager().getValue(aircraftYawKey, new GetCallback() {
            @Override
            public void onSuccess(Object o) {
                aircraftYaw = ((Double) o).floatValue();
                setImagePosition(mapWidget);
            }

            @Override
            public void onFailure(DJIError djiError) {

            }
        });
    }

    private void doPathAction(String jobId, String pathId, String actionType, String latlng,final String msg) {
        userAPIService.getMissionPathAction(jobId, pathId, actionType).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (msg.equalsIgnoreCase("Action START Send Successfully!")) {
                    }
                    else if(msg.equalsIgnoreCase("Mission Completed Successfully!")) {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to start mission", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to start mission", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateSettingdUI(int min, int max) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            angle_skbar.setMin(min);
            angle_skbar.setMax(max);

        }
    }

    private void disableViews(boolean b, boolean b1, boolean b2, boolean b3){
        if(b==true){
            sp_drone.setVisibility(View.VISIBLE);

        }
        else{
            sp_drone.setVisibility(View.GONE);

        }
        if(b1==true){
            sp_drones.setVisibility(View.VISIBLE);
        }
        else{
            sp_drones.setVisibility(View.GONE);

        }
        if(b2==true){
            speed_txt.setVisibility(View.VISIBLE);

        }
        else {
            speed_txt.setVisibility(View.GONE);
        }
        if(b3==true){
            speed_skbar.setVisibility(View.VISIBLE);
        }
        else{
            speed_skbar.setVisibility(View.GONE);
        }
    }
}





