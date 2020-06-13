package com.xyz.druber.flight.network.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 Created by SANDEEP
 **/

public class MMission implements Parcelable {

    private String name;

    private String _id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    MMission(Parcel in) {
        name = in.readString();
        _id = in.readString();
    }

    public static final Creator<MMission> CREATOR = new Creator<MMission>() {
        @Override
        public MMission createFromParcel(Parcel in) {
            return new MMission(in);
        }

        @Override
        public MMission[] newArray(int size) {
            return new MMission[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(_id);
    }
}
