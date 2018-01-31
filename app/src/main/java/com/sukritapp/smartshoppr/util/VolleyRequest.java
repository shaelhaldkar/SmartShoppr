package com.sukritapp.smartshoppr.util;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sukritapp.smartshoppr.listner.WebResponseListner;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shailendra on 8/05/17.
 */
public class VolleyRequest {
    private String TAG = AppLog.getClassName();
    private static RequestQueue mRequestQueue;

    private static VolleyRequest volleyRequest;
    private WebResponseListner webResponseListner;


    private int TIME_OUT = 80000;

    public static VolleyRequest getvolleyRequest() {
        if (volleyRequest == null) {
            volleyRequest = new VolleyRequest();
        }

        return volleyRequest;
    }
/*

    public void initInterface(Context mContext) {
        webResponseListner = (WebResponseListner) mContext;
    }

    public void initInterface(Fragment mContext) {
        webResponseListner = (WebResponseListner) mContext;
    }
*/

    private RequestQueue getRequestQueue(Context mContext) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private <T> void addToRequestQueue(Context mContext, Request<T> request, String tag) {
        // set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag));
        getRequestQueue(mContext).add(request);
    }

    public void getResponseFromGetMethod(Context mContext, String url, final String tag, final WebResponseListner webResponseListner) {
        AppLog.info(TAG, AppLog.getMethodName());

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                webResponseListner.onResponseReceived(null, response, tag);

                System.out.println("response" + response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                String mess = "Error";
                System.out.println("error" + error);

                int resCode = 0;

                try {

                    resCode = error.networkResponse.statusCode;

                    mess = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.getMessage();
                }


                if (error instanceof NoConnectionError) {
                    webResponseListner.onResponseReceived("No internet available. Please enable data and try again later.", "NO_INTERNET", tag);
                } else if (resCode == 401 && error instanceof AuthFailureError) {
                    webResponseListner.onResponseReceived("Your authorization code is not valid. Do you want to login again.", "401", tag);
                } else {
                    webResponseListner.onResponseReceived(mess, null, tag);
                }


                //   error.networkResponse.

            }
        }) {




/*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization",AUTH);

                return headers;
            }*/
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(mContext, strReq, tag);

    }

    public void getResponseFromJson(Context mContext, String url, final String tag, JSONObject parm, final WebResponseListner webResponseListner) {
        AppLog.info("TAG", "url : " + url);
        AppLog.info("TAG", "tag : " + tag);
        AppLog.info("TAG", "parm : " + parm);

        JsonObjectRequest req = new JsonObjectRequest(url, parm, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    webResponseListner.onResponseReceived(null, response.toString(), tag);
                    AppLog.info("TAG", "response : " + response.toString());
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Error";

                try {

                    mess = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (error instanceof NoConnectionError) {
                    webResponseListner.onResponseReceived("No Network", null, tag);
                } else if (mess.contains("Invalid Credentials")) {
                    webResponseListner.onResponseReceived(mess, null, tag);
                } else {
                    webResponseListner.onResponseReceived("Internal server error", null, tag);
                }
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                //  headers.put("token", APPConstants.tokenHeader);

                return headers;
            }
        };
        req.setShouldCache(false);// todo added this to remove cache from request
        req.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(mContext, req, tag);
    }


    public void getResponseFromJsonImage(Context mContext, String url, final String tag, final String encodedSrting, JSONObject parm) {

        JsonObjectRequest req = new JsonObjectRequest(url, parm, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    webResponseListner.onResponseReceived(null, response.toString(), tag);
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Error";

                try {
                    mess = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (error instanceof NoConnectionError) {
                    webResponseListner.onResponseReceived("No Network", null, tag);
                } else if (mess.contains("Invalid Credentials")) {
                    webResponseListner.onResponseReceived(mess, null, tag);
                } else {
                    webResponseListner.onResponseReceived("Internal server error", null, tag);
                }
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

//                headers.put("token", APPConstants.tokenHeader);
                // headers.put("content",encodedSrting);

                return headers;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(mContext, req, tag);
    }

    public void getResponseForOlaETP(Context mContext, String url, final String tag, final WebResponseListner webResponseListner) {
        AppLog.info(TAG, AppLog.getMethodName());

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                webResponseListner.onResponseReceived(null, response, tag);

                System.out.println("response" + response);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                String mess = "Error";
                System.out.println("error" + error);

                int resCode = 0;

                try {

                    resCode = error.networkResponse.statusCode;

                    mess = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.getMessage();
                }


                if (error instanceof NoConnectionError) {
                    webResponseListner.onResponseReceived("No internet available. Please enable data and try again later.", "NO_INTERNET", tag);
                } else if (resCode == 401 && error instanceof AuthFailureError) {
                    webResponseListner.onResponseReceived("Your authorization code is not valid. Do you want to login again.", "401", tag);
                } else {
                    webResponseListner.onResponseReceived(mess, null, tag);
                }


                //   error.networkResponse.

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("x-app-token", "2c623e867c1a4cf39c43a4757a207f08");
                headers.put("authorization", "Bearer 631404119bfc4097b6aac95ce160933c");

                return headers;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(mContext, strReq, tag);

    }

}
