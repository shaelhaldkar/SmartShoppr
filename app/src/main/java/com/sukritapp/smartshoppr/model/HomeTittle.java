package com.sukritapp.smartshoppr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by abc on 2/19/2017.
 */

public class HomeTittle implements  Parcelable{

    private String tittle;
    private ArrayList<ResponseModel> mlist;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    private String logo;

    public HomeTittle(String tittle, ArrayList<ResponseModel> mlist) {
        this.tittle = tittle;
        this.mlist = mlist;
    }

    protected HomeTittle(Parcel in) {
        tittle = in.readString();
        mlist = in.createTypedArrayList(ResponseModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tittle);
        dest.writeTypedList(mlist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeTittle> CREATOR = new Creator<HomeTittle>() {
        @Override
        public HomeTittle createFromParcel(Parcel in) {
            return new HomeTittle(in);
        }

        @Override
        public HomeTittle[] newArray(int size) {
            return new HomeTittle[size];
        }
    };

    public String getTittle() {
        return tittle;
    }
    public ArrayList<ResponseModel> getMlist() {
        return mlist;
    }
}
