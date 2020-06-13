package com.xyz.druber.flight;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * Created by SANDEEP on 22/05/2019
 */
public class CalculateCircleDistance {
    ArrayList<LatLng> updated_Latlng = new ArrayList<>();
    float circle_Distance;
    float [] total_Distance = new float[2];
    double startlat,startlng,endlat,endlng;
    float dist;
    public CalculateCircleDistance(Context applicationContext, ArrayList<LatLng> updated_Latlng) {

        this.updated_Latlng = updated_Latlng;

         startlat = updated_Latlng.get(0).latitude;
         startlng = updated_Latlng.get(0).longitude;
         endlat = updated_Latlng.get(1).latitude;
        endlng = updated_Latlng.get(1).longitude;
        Location.distanceBetween(startlat, startlng,
                endlat, endlng, total_Distance);
        dist=total_Distance[0];

        this.circle_Distance = total_Distance[0] * updated_Latlng.size();
    }
}