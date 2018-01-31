package com.sukritapp.smartshoppr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by abc on 1/15/2017.
 */

public class ResponseModel implements Parcelable {


    String categories_url = "";
    private String tag = "";
    private String logo = "";
    private String webURl = "";
    private String discription = "";
    private Boolean isBookmarked;

    private ArrayList<String> couponList = null;

    public ArrayList<String> getCouponList() {
        return couponList;
    }

    public void setCouponList(ArrayList<String> couponList) {
        this.couponList = couponList;
    }

    protected ResponseModel(Parcel in) {
        tag = in.readString();
        logo = in.readString();
        webURl = in.readString();
        discription = in.readString();
        categories_url = in.readString();
        couponList = in.readArrayList(null);
        isBookmarked = (in.readByte() != 0);
    }

    public static final Creator<ResponseModel> CREATOR = new Creator<ResponseModel>() {
        @Override
        public ResponseModel createFromParcel(Parcel in) {
            return new ResponseModel(in);
        }

        @Override
        public ResponseModel[] newArray(int size) {
            return new ResponseModel[size];
        }
    };

    public ResponseModel() {

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebURl() {
        return webURl;
    }

    public void setWebURl(String webURl) {
        this.webURl = webURl;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public boolean getIsBookmarked() {
        return this.isBookmarked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(logo);
        parcel.writeString(webURl);
        parcel.writeString(discription);
        parcel.writeString(categories_url);
        parcel.writeList(couponList);
        parcel.writeByte((byte) (isBookmarked ? 1 : 0));
    }
}
