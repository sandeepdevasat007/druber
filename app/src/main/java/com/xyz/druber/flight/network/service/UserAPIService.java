package com.xyz.druber.flight.network.service;

import com.squareup.okhttp.RequestBody;
import com.xyz.druber.flight.network.model.ActivateUserRequest;
import com.xyz.druber.flight.network.model.AuthenticateUserRequest;
import com.xyz.druber.flight.network.model.FinalPath;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.model.PathMarker;
import com.xyz.druber.flight.network.model.RegisterUserRequest;
import com.xyz.druber.flight.network.model.User;

import java.util.ArrayList;

import retrofit.Call;

/**
 * Created by KT on 23/12/15.
 */
public interface UserAPIService {

    Call<User> registerUser(RegisterUserRequest request);

    Call<User> authenticate(AuthenticateUserRequest request);

    Call<Void> activateUser(ActivateUserRequest request);

    Call<PathMarker> getPathMarkerList();

    Call<ArrayList<Mission>> getMissionsList(String user_id);

    Call<Object> getElevation(String longitude, String latitude);

    Call<Object> startJob(String jobId, RequestBody requestBody);

    Call<FinalPath> getMissionPath(String path);

    Call<Object> getMissionPathAction(String jobId, String pathId, String action);

}
