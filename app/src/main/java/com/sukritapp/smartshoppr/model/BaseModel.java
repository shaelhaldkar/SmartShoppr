
package com.sukritapp.smartshoppr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaseModel {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("ride_estimate")
    @Expose
    private OlaRideEstimate olaRideEstimate;

    /**
     * 
     * @return
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The olaRideEstimate
     */
    public OlaRideEstimate getOlaRideEstimate() {
        return olaRideEstimate;
    }

    /**
     * 
     * @param olaRideEstimate
     *     The ride_estimate
     */
    public void setOlaRideEstimate(OlaRideEstimate olaRideEstimate) {
        this.olaRideEstimate = olaRideEstimate;
    }

}
