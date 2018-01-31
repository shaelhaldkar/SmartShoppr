package com.sukritapp.smartshoppr.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eagers on 12/01/17.
 */

public class WebRetriveDataTask {
    private final String TAG = AppLog.getClassName();

    public void retriveData(Context context, final OnSukritResponseListner onSukritResponseListner, final Constant.listtype listtype, final JSONObject postParam, String URL) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "" + listtype);
        AppLog.info(TAG, "URL" + URL);

        RequestQueue queue = Volley.newRequestQueue(context);
//        String requestdata = postParam.toString();
        Log.i("Tag ---", "A");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse for :" + listtype);
                AppLog.info(TAG, "response :" + listtype);

                onSukritResponseListner.onResponseReceived(null, listtype, response);
//                webResponseListner.onResponseReceived(null, response, "tag");
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
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
                    onSukritResponseListner.onResponseReceived("No internet available. Please enable data and try again later.", listtype, "tag");
                } else if (resCode == 401 && error instanceof AuthFailureError) {
                    onSukritResponseListner.onResponseReceived("Your authorization code is not valid. Do you want to login again.", listtype, "tag");
                } else {
                    onSukritResponseListner.onResponseReceived(mess, null, "tag");
                }

                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());

    }

    ;


//    public  void retriveTopStoreData(Context context, final OnSukritResponseListner onSukritResponseListner, final JSONObject postParam) {
//        AppLog.enter(TAG, AppLog.getMethodName());
//
//        RequestQueue queue = Volley.newRequestQueue(context);
////        String requestdata = postParam.toString();
//        Log.i("Tag ---", "A");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_BASE+Constant.URL_BANNER_LIST, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                AppLog.enter(TAG, AppLog.getMethodName());
//
//                onSukritResponseListner.onResponseReceived(Constant.listtype.BANNNER,response);
//                AppLog.exit(TAG, AppLog.getMethodName());
//
//            }
//        }, new Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                AppLog.enter(TAG, AppLog.getMethodName());
//                AppLog.exit(TAG, AppLog.getMethodName());
//
//            }
//
//        });
//        queue.add(stringRequest);
//        AppLog.exit(TAG, AppLog.getMethodName());
//
//    };

    public void registerUser(Context context, final OnSukritResponseListner onSukritResponseListner, Constant.listtype signup, String url, final String name, final String email, final String contactNo, final String deviceID, final String password) {
        AppLog.enter(TAG, AppLog.getMethodName());
//
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse  :" + response);
//
                onSukritResponseListner.onResponseReceived(null, Constant.listtype.SIGNUP, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
                onSukritResponseListner.onResponseReceived(error.toString(), Constant.listtype.SIGNUP, null);
                SmartShopprUtils.getInstance().showToastMSG(Constant.ERROR_MEG_INTERNET);
                AppLog.info(TAG, "error" + error);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("email_id", email);
                params.put("user_name", name);
                params.put("mobile", contactNo);
                params.put("device_id", deviceID);
                params.put("password", password);
                return params;
            }
        };

        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());


    }

    public void setTimeOUT(Request myRequest) {
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void signInUser(Context context, String url, final String PhoneNumber, final String password, final OnSukritResponseListner onSukritResponseListner) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "url : " + url);
        AppLog.info(TAG,"PhoneNumber : "+PhoneNumber);
        AppLog.info(TAG,"password : "+password);
//
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse  :" + response);
//
                onSukritResponseListner.onResponseReceived(null, Constant.listtype.SIGNIN, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
                SmartShopprUtils.getInstance().showToastMSG(Constant.ERROR_MEG_INTERNET);
                onSukritResponseListner.onResponseReceived(error.toString(), Constant.listtype.SIGNIN, null);
                AppLog.info(TAG, "error" + error);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("mobile", PhoneNumber);
                params.put("password", password);

                return params;
            }
        };

        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void getUserPassword(Context context, String url, final String PhoneNumber, final OnSukritResponseListner onSukritResponseListner) {

        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "url : " + url);
        AppLog.info(TAG, "PhoneNumber : " + PhoneNumber);
//
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse  :" + response);
//
                onSukritResponseListner.onResponseReceived(null, Constant.listtype.GETPASSWORD, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
//                SmartShopprUtils.getInstance().showToastMSG(Constant.ERROR_MEG_INTERNET);
                onSukritResponseListner.onResponseReceived(error.getMessage(), Constant.listtype.VALIDATEOTP, null);
                AppLog.info(TAG, "error" + error);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("mobile", PhoneNumber);

                return params;
            }
        };

        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void getValidateUserOTP(Context context, String url, final String PhoneNumber, final String otp, final OnSukritResponseListner onSukritResponseListner) {

        AppLog.enter(TAG, AppLog.getMethodName());
//
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse  :" + response);
//
                onSukritResponseListner.onResponseReceived(null, Constant.listtype.VALIDATEOTP, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
                SmartShopprUtils.getInstance().showToastMSG(Constant.ERROR_MEG_INTERNET);

                onSukritResponseListner.onResponseReceived(error.getMessage(), Constant.listtype.VALIDATEOTP, null);
                AppLog.info(TAG, "error" + error);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("mobile", PhoneNumber);
                params.put("otp", otp);

                return params;
            }
        };

        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void sendUserOTP(Context context, String url, final String PhoneNumber, final String OTP, final OnSukritResponseListner onSukritResponseListner) {

        AppLog.enter(TAG, AppLog.getMethodName());
//
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse  :" + response);
//
                onSukritResponseListner.onResponseReceived(null, Constant.listtype.SENDOTP, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
                SmartShopprUtils.getInstance().showToastMSG(Constant.ERROR_MEG_INTERNET);
                onSukritResponseListner.onResponseReceived(error.getMessage(), Constant.listtype.SENDOTP,null);

                AppLog.info(TAG, "error" + error);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("mobile", PhoneNumber);
                params.put("otp", OTP);

                return params;
            }
        };

        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void changePassword(Context context, final Constant.listtype listtype, String URL, final OnSukritResponseListner onSukritResponseListner) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "" + listtype);
        AppLog.info(TAG, "URL" + URL);

        RequestQueue queue = Volley.newRequestQueue(context);
//        String requestdata = postParam.toString();
        Log.i("Tag ---", "A");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLog.enter(TAG, AppLog.getMethodName());
                AppLog.info(TAG, "onResponse for :" + listtype);
                AppLog.info(TAG, "response :" + listtype);

                onSukritResponseListner.onResponseReceived(null, listtype, response);
                AppLog.exit(TAG, AppLog.getMethodName());

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.enter(TAG, AppLog.getMethodName());
                onSukritResponseListner.onResponseReceived(error.getMessage(), listtype, null);
                AppLog.exit(TAG, AppLog.getMethodName());

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        setTimeOUT(stringRequest);
        queue.add(stringRequest);
        AppLog.exit(TAG, AppLog.getMethodName());

    }
}