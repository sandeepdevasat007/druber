package com.xyz.druber.flight.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dji.ux.druber.R;
import com.google.android.gms.maps.model.LatLng;
import com.xyz.druber.flight.FlightMapActivity;
import com.xyz.druber.flight.FlightPathsActivity;
import com.xyz.druber.flight.network.model.FinalPath;
import com.xyz.druber.flight.network.model.FlightPaths;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.service.UserAPIService;
import com.xyz.druber.flight.utils.NetworkUtils;
import com.xyz.druber.flight.utils.ScreenUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@SuppressLint("ViewConstructor")
public class CustomFlightPathView extends LinearLayout {

    @BindView(R.id.altitude_txt)
    TextView altitude_txt;
    @BindView(R.id.gridtype)
    TextView gridType;
    @BindView(R.id.card_view)
    CardView cardView;
    private String typeSting;
    private Context mContext;
    private FlightPaths flightPaths;
    private ArrayList<FlightPaths> rootFlightPaths;
    private FinalPath finalPath;
    private View itemView;
    private Mission mission;
    private boolean completed, isPending;
    private RotateAnimation r;
    private UserAPIService userAPIService;
    private static final float ROTATE_FROM = 30.0f;
    private static final float ROTATE_TO = 360.0f;

    public CustomFlightPathView(Context context, ArrayList<FlightPaths> rootFlightPaths, String typeSting) {
        super(context);
        this.mContext = context;
        this.typeSting = typeSting;
        this.rootFlightPaths = rootFlightPaths;
        init();
    }

    @SuppressLint({"RestrictedApi", "NewApi"})
    private void init() {
        itemView = inflate(mContext, R.layout.flightpath_item_layout, this);
        ButterKnife.bind(this, itemView);
        userAPIService = NetworkUtils.provideUserAPIService(mContext, "https://missions.");

    }

    public void setData(Mission mission, final FlightPaths flightPaths, int position) {
        this.flightPaths = flightPaths;
        this.mission = mission;
        ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(VISIBLE);
        userAPIService.getMissionPath(flightPaths.getPath()).enqueue(new Callback<FinalPath>() {
            @SuppressLint({"RestrictedApi", "NewApi"})
            @Override
            public void onResponse(Response<FinalPath> response, Retrofit retrofit) {
                ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(GONE);
                if (response.isSuccess()) {

                    finalPath = response.body();
                    if (flightPaths.getEnd_time() != null && !flightPaths.getEnd_time().isEmpty()
                            && flightPaths.getStart_time() != null && !flightPaths.getStart_time().isEmpty()) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
                        }
                    } else if (flightPaths.getLatlng() != null && !flightPaths.getLatlng().isEmpty()
                            && flightPaths.getEnd_time() == null) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        }
                    } else if (flightPaths.getStart_time() == null && flightPaths.getEnd_time() == null || flightPaths.getStart_time() != null) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatTextView) altitude_txt).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
                        } else {
                            altitude_txt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
                        }
                    }
                    gridType.setText(finalPath.getGridtype());
                    gridType.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(R.color.colorOrange));
                    altitude_txt.setText(String.format("altitude: %s m", String.valueOf(response.body().getAltitude())));
                } else {
                    Log.d("Error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ((FlightPathsActivity) mContext).getProgress_bar().setVisibility(GONE);
                Log.d("Failure", "onFailure: " + t.getMessage());
            }
        });

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean not_completed = false;
                boolean not_started = false;
                //isPending=true;
                for (FlightPaths rootFlightPath : rootFlightPaths) {
                    if (rootFlightPath.getStart_time() != null && !rootFlightPath.getStart_time().isEmpty()
                            && rootFlightPath.getEnd_time() == null) {
                        not_completed = true;
                    }
                }
               /* if (not_completed) { enable
                    if (flightPaths.getLatlng() != null && !flightPaths.getLatlng().isEmpty()) {
                        not_completed = true;
                        isPending = true;
                    }
                }*/

                if (flightPaths.getStart_time() != null && !flightPaths.getStart_time().isEmpty() &&
                        flightPaths.getEnd_time() != null && !flightPaths.getEnd_time().isEmpty())
                    completed = true;

                if (flightPaths.getStart_time() == null && flightPaths.getEnd_time() == null)
                    not_started = true;



                if (completed) {
                    ScreenUtils.showToast(mContext, "Path already completed!");
                }  else if (not_completed||not_started) {
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    if (finalPath != null) {
                        String geometryType = finalPath.getGeometry().getType();
                        List<com.google.maps.android.geometry.Point> points = null;
                        String parsedResponse = finalPath.getGeometry().getCoordinates().toString();
                        if (geometryType.equalsIgnoreCase("LineString")) {
                            addPathFromWeb(parsedResponse);

                        } else if (geometryType.equalsIgnoreCase("Polygon") || geometryType.equalsIgnoreCase("MultiLineString")) {
                            try {
                                //JSONArray coords_Array=new JSONArray(finalPath.getGeometry().getCoordinates().toString());
                                JSONArray coords_Array = new JSONArray(finalPath.getGeometry().getCoordinates().toString());
                                String sarr1 = coords_Array.get(0).toString();
                                if(coords_Array.length()>1){
                                    sarr1 = sarr1.substring(1, sarr1.length() - 1);
                                    String sarr2 = coords_Array.get(1).toString();
                                    sarr2 = sarr2.substring(1, sarr2.length() - 1);
                                    String fnalString = "[" + sarr1 + "," + sarr2 + "]";
                                    Log.d("finalsss", fnalString);
                                    addPathFromWeb(fnalString);

                                }
                                else {
                                    addPathFromWeb(sarr1);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // points = TurfMeta.coordAll(Polygon.fromJson(ScreenUtils.getDoubleArrayJson(finalPath.getGeometry().getCoordinates().toString())), true);
                        }



                    } else {
                        ScreenUtils.showToast(mContext, "No paths found");
                    }
                } /*else
                    ScreenUtils.showToast(mContext, "Please complete existing path!");*/
            }
        });
    }

    private void addPathFromWeb(String parsedResponse) {
        try {
            JSONArray finalCoordinates = new JSONArray(parsedResponse);
            ArrayList<LatLng> coords = new ArrayList<>();
            for (int i = 0; i < finalCoordinates.length(); i++) {

                JSONArray latlong = finalCoordinates.getJSONArray(i);
                double lat = latlong.getDouble(1);
                double lng = latlong.getDouble(0);
                coords.add(new LatLng(lat, lng));
            }
            Log.d("coflight", coords.toString());

            if (coords.size() > 0) {
                Intent flightPathIntent = new Intent(mContext, FlightMapActivity.class);
                flightPathIntent.putParcelableArrayListExtra(com.google.android.gms.maps.model.LatLng.class.getName(), coords);
                flightPathIntent.putExtra(Mission.class.getName(), mission);
                flightPathIntent.putExtra(FinalPath.class.getName(), finalPath);
                flightPathIntent.putExtra(finalPath.getGridtype(),FlightMapActivity.class);
                if (flightPaths.getLatlng() != null)
                    flightPathIntent.putExtra("resume_action_latlng", flightPaths.getLatlng());
                (mContext).startActivity(flightPathIntent);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}