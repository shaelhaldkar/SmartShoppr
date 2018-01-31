package com.sukritapp.smartshoppr.listner;

import com.sukritapp.smartshoppr.util.Constant;

/**
 * Created by abc on 1/14/2017.
 */

public interface OnSukritResponseListner {

    public void onResponseReceived(String error,Constant.listtype tag, String response);

}