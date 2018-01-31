package com.sukritapp.smartshoppr.listner;

/**
 * Created by Shailendra on 04-May-17.
 */

public interface WebResponseListner {
     void onResponseReceived(String error, String response, String tag);
}
