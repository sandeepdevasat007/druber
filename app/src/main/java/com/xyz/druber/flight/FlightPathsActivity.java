package com.xyz.druber.flight;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dji.ux.druber.R;
import com.xyz.druber.flight.adapter.FlightAdapter;
import com.xyz.druber.flight.constant.Constant;
import com.xyz.druber.flight.network.model.FlightPaths;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.service.UserAPIService;
import com.xyz.druber.flight.utils.DataUtils;
import com.xyz.druber.flight.utils.ItemOffsetDecoration;
import com.xyz.druber.flight.utils.NetworkUtils;
import com.xyz.druber.flight.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FlightPathsActivity extends AppCompatActivity {

    @BindView(R.id.flight_paths_recyler_view)
    RecyclerView flightpaths_recycler_view;
    @BindView(R.id.progress_bar)
    FrameLayout progress_bar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    private Unbinder unbinder;
    Mission mission;
    private String typeSting;
    private UserAPIService userAPIService;
    private ArrayList<Mission> missions;
    private ArrayList<FlightPaths> flightPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_paths);

        unbinder = ButterKnife.bind(this);
        userAPIService = NetworkUtils.provideUserAPIService(this, "https://missions.");
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.nav_header_vertical_spacing);
        flightpaths_recycler_view.addItemDecoration(itemDecoration);
        flightpaths_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Path List");
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (getIntent().hasExtra(Mission.class.getName())) {
            mission = getIntent().getExtras().getParcelable(Mission.class.getName());
        }

        if (getIntent().hasExtra(Constant.JOB_TYPE))
            typeSting = getIntent().getExtras().getString(Constant.JOB_TYPE);

        /*if (getIntent().hasExtra("getFlightPaths"))
            flightPaths = getIntent().getParcelableArrayListExtra("getFlightPaths");

        if (flightPaths.size() > 0) {
            progress_bar.setVisibility(View.GONE);
            flightpaths_recycler_view.setAdapter(new FlightAdapter(this, flightPaths, mission, typeSting));
        } else
            progress_bar.setVisibility(View.GONE);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        userAPIService.getMissionsList(DataUtils.getId(this)).enqueue(new Callback<ArrayList<Mission>>() {
            @Override
            public void onResponse(Response<ArrayList<Mission>> response, Retrofit retrofit) {
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    missions = response.body();
                    getMissions(missions);
                } else {
                    Log.d("Error:", "onResponse: " + response.errorBody());
                    ScreenUtils.getSnackbar(coordinatorLayout, "Unable to process request!");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Log.d("Failure:", "onFailure: " + t.getMessage());
                ScreenUtils.getSnackbar(coordinatorLayout, "Unable to process request!");
            }
        });
    }

    private void getMissions(ArrayList<Mission> missions) {
        for (Mission childmission : missions) {
            if (childmission.get_id().contains(mission.get_id())) {
                flightPaths = childmission.getFlightPaths();
            }
        }
        if (flightPaths.size() > 0)
            flightpaths_recycler_view.setAdapter(new FlightAdapter(FlightPathsActivity.this, flightPaths, mission, typeSting));
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
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

    public FrameLayout getProgress_bar() {
        return progress_bar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
