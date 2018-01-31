package com.sukritapp.smartshoppr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abc on 4/12/2017.
 */

public class SeeAllCategoriesModel implements Parcelable{

   public String TAG="";
    public String categories_url="";

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getCategories_url() {
        return categories_url;
    }

    public void setCategories_url(String categories_url) {
        this.categories_url = categories_url;
    }

    public SeeAllCategoriesModel() {
    }

    protected SeeAllCategoriesModel(Parcel in) {
        TAG = in.readString();
        categories_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TAG);
        dest.writeString(categories_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SeeAllCategoriesModel> CREATOR = new Creator<SeeAllCategoriesModel>() {
        @Override
        public SeeAllCategoriesModel createFromParcel(Parcel in) {
            return new SeeAllCategoriesModel(in);
        }

        @Override
        public SeeAllCategoriesModel[] newArray(int size) {
            return new SeeAllCategoriesModel[size];
        }
    };
}
