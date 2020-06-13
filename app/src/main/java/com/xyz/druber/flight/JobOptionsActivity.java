package com.xyz.druber.flight;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dji.ux.druber.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.xyz.druber.flight.constant.Constant;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.service.UserAPIService;
import com.xyz.druber.flight.utils.NetworkUtils;
import com.xyz.druber.flight.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 Created by SANDEEP
 **/


public class JobOptionsActivity extends AppCompatActivity {
    @BindView(R.id.typeStartBtn)
    Button typeStartBtn;

    @BindView(R.id.successTxt)

    TextView successTxt;

    @BindView(R.id.typeStopBtn)
    TextView typeStopBtn;

    @BindView(R.id.coordinator_layout)

    CoordinatorLayout coordinatorLayout;

     String typeSting;
     Unbinder unbinder;
     Mission mission;
     UserAPIService userAPIService;
     String[] droneTypes;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_options);
        unbinder = ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        droneTypes = getResources().getStringArray(R.array.drones);
        ScreenUtils.createProgressDialoge(this, "Please wait...");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getIntent().hasExtra(Mission.class.getName()))
            mission = getIntent().getExtras().getParcelable(Mission.class.getName());

        if (getIntent().hasExtra(Constant.JOB_TYPE))
            typeSting = getIntent().getExtras().getString(Constant.JOB_TYPE);

        if (typeSting != null && typeSting.contains(Constant.PENDING)) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorOrange)));
            typeStartBtn.setText(Constant.START_JOB);
            typeStopBtn.setVisibility(View.GONE);
            successTxt.setVisibility(View.GONE);
            typeStartBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
        } else if (typeSting != null && typeSting.contains(Constant.INPROGRESS)) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBlue)));
            typeStopBtn.setText(Constant.STOP_JOB);
            typeStartBtn.setVisibility(View.GONE);
            successTxt.setVisibility(View.GONE);
            typeStartBtn.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
        } else if (typeSting != null && typeSting.contains(Constant.COMPLETE)) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorGreen)));
            successTxt.setText(" ");
            typeStartBtn.setVisibility(View.GONE);
            typeStopBtn.setVisibility(View.GONE);
            successTxt.setVisibility(View.VISIBLE);
            successTxt.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            successTxt.setText(R.string.mission_success);
        }
    }

    @OnClick(R.id.typeStartBtn)
    public void startMission() {
        new AlertDialog.Builder(this, R.style.CustomDialog)
                .setSingleChoiceItems(droneTypes, -1, null)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition < 0) {
                            ScreenUtils.showToast(JobOptionsActivity.this, "Nothing selected");
                        } else {
                            ScreenUtils.showProgress();
                            dialog.dismiss();
                            doJob("START", droneTypes[selectedPosition]);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.typeStopBtn)
    public void endMission() {
        doJob("END", "");
    }

    private void doJob(String jobAction, String droneType) {
        JSONObject jobJson = new JSONObject();
        JSONObject jobData = new JSONObject();
        JSONObject jobDroneData = new JSONObject();
        try {
            jobJson.put("stepName", mission.getMissionSteps().get(0).getName());
            jobJson.put("action", jobAction);
            jobDroneData.put("time", getTime());
            if (droneType != null) {
                jobData.put("drone_type", droneType);
                jobDroneData.put("data", jobData);
            }
            jobJson.put("stepData", jobDroneData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestBody jobRequestBody = RequestBody.create(MediaType.parse("application/json"), jobJson.toString());
        userAPIService.startJob(mission.get_id(), jobRequestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                ScreenUtils.dissmisProgress();
                if (response.isSuccess()) {
                    ScreenUtils.showToast(JobOptionsActivity.this, "Success!");
                    finish();
                } else {
                    ScreenUtils.getSnackbar(coordinatorLayout, "Unable to process request");
                    Log.d("Error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ScreenUtils.dissmisProgress();
                ScreenUtils.getSnackbar(coordinatorLayout, "Unable to process request");
                Log.d("Failure", "onFailure: " + t.getMessage());
            }
        });
    }

    private String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}