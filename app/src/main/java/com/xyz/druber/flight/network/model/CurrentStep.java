package com.xyz.druber.flight.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 Created by SANDEEP
 **/


public class CurrentStep implements Parcelable {
    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    protected CurrentStep(Parcel in) {
        status = in.readString();
    }

    public static final Creator<Assignee> CREATOR = new Creator<Assignee>() {
        @Override
        public Assignee createFromParcel(Parcel in) {
            return new Assignee(in);
        }

        @Override
        public Assignee[] newArray(int size) {
            return new Assignee[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }
}
