package com.xyz.druber.flight.network;


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
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by KT on 23/12/15.
 */
public interface RemoteServerAPI {

    String BASE_CONTEXT = "/api";
    //String BASE_API_IP="https://3rdi-staging.xyzinnotech.com";

    @Multipart
    @POST(BASE_CONTEXT + "/users/image")
    Call<Void> uploadImage(@Header("x-auth-token") String fcmId, @Part("file") RequestBody request);

    @POST(BASE_CONTEXT + "/register")
    Call<User> registerUser(@Body RegisterUserRequest request);

    @POST(BASE_CONTEXT + "/v1/auth/authenticate")
    Call<User> authenticate(@Body AuthenticateUserRequest request);

    @POST("/activate")
    Call<Void> activateUser(@Body ActivateUserRequest request);

    @GET("/api/mission-management/job/")
    Call<ArrayList<Mission>> getDroneMissionList(@Query("assignee") String user_id);

    @GET("http://www.mocky.io/v2/5c63bc0f3200004f1693f54d")
    Call<PathMarker> getPathMarkerList();

    @GET("https://api.mapbox.com/v4/mapbox.mapbox-terrain-v2/tilequery/" + "{longitude}" + "," + "{latitudue}" + ".json?&access_token=pk.eyJ1IjoiZGV2ZWxvcGVyeHl6IiwiYSI6ImNqa2poMXV1cjFibzQza2p5c3YyZGl6cGMifQ.uMu2kiKV4918S7ITBjdXZQ")
    Call<Object> getElevation(@Path("longitude") String longitude, @Path("latitudue") String latitude);

    @PATCH("/api/mission-management/job/{jobId}")
    Call<Object> startJob(@Path("jobId") String jobId, @Body RequestBody requestBody);

    @GET("/api/mission-management/mission/path/{path}")
    Call<FinalPath> getMissionPath(@Path("path") String path);

    @PATCH("/api/mission-management/job/{jobId}/path/{pathId}")
    Call<Object> getMissionPathAction(@Path("jobId") String jobId, @Path("pathId") String pathId, @Query("action") String action);
}

