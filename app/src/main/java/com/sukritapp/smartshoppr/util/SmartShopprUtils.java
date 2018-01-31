package com.sukritapp.smartshoppr.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.fragments.ShowOnWebFragment;
import com.sukritapp.smartshoppr.model.CountryModel;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.PriceModel;
import com.sukritapp.smartshoppr.model.ResponseModel;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by abc on 12/25/2016.
 */

public class SmartShopprUtils {


    private final String TAG = AppLog.getClassName();

    private static SmartShopprUtils sSmartShopprUtils = null;

    private String TAG_NEXT_FRAGMENT_NAME = "";
    private ArrayList<ResponseModel> LIST_DETAIL = null;
    private ArrayList<ResponseModel> LIST_BANNER = null;
    private FragmentManager fragmentManager = null;
    public static boolean isShowing = false;

    private SmartShopprUtils() {
    }

    public static SmartShopprUtils getInstance() {

        if (null == sSmartShopprUtils) {
            sSmartShopprUtils = new SmartShopprUtils();
        }
        return sSmartShopprUtils;
    }

    public boolean isConnectedToInternet() {
        AppLog.enter(TAG, AppLog.getMethodName());

        boolean result = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) SmartShopprApp
                .getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        AppLog.exit(TAG, AppLog.getMethodName());
        return result;
    }

    public boolean isWifiEnabled() {
        AppLog.enter(TAG, AppLog.getMethodName());

        boolean bStatus = false;

        WifiManager wifiManager = (WifiManager) SmartShopprApp.getApplication().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        bStatus = wifiManager.isWifiEnabled();

        AppLog.exit(TAG, AppLog.getMethodName());
        return bStatus;
    }

    public boolean hasActiveInternetConnection() {
        AppLog.enter(TAG, AppLog.getMethodName());
        boolean bResult = false;

        if (isWifiEnabled() && isConnectedToInternet()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                AppLog.exit(TAG, AppLog.getMethodName());
                if (urlc.getResponseCode() == 200) {
                    bResult = true;
                }
            } catch (SocketTimeoutException e) {
                AppLog.error(TAG, "Error checking internet connection, Timeout : ", e);
                return bResult;
            } catch (IOException e) {
                AppLog.error(TAG, "Error checking internet connection : ", e);
                return bResult;
            }
        } else {
            AppLog.error(TAG, "Please check wifi settings.");
        }
        AppLog.exit(TAG, AppLog.getMethodName());
        return bResult;
    }

    public void showToastMSG(String MSG) {
        Toast.makeText(SmartShopprApp.getApplication(), MSG, Toast.LENGTH_LONG).show();
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) SmartShopprApp.getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    public void showInfoDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })

                .show();
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void showErrorDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(msg)
                .setTitle("warning !!")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })

                .show();
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    public void showOnweb(String URL) {
        AppLog.enter(TAG, AppLog.getMethodName());
//        String url = "http://www.google.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://" + URL));
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        SmartShopprApp.getApplication().startActivity(i);
        AppLog.exit(TAG, AppLog.getMethodName());
    }


    public void showWebFragment(Bundle bundle) {
        Fragment nextFrag = new ShowOnWebFragment();
        nextFrag.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {


        this.fragmentManager = fragmentManager;
    }

    public ArrayList<ResponseModel> parseJsonGetBannerlist(String response, String arrayName) {
        AppLog.enter(TAG, AppLog.getMethodName());

        ArrayList<ResponseModel> responseModelList = null;
        if (null != response && !response.isEmpty()) {
            responseModelList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(arrayName);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    ResponseModel responseModel = new ResponseModel();

                    responseModel.setLogo(jObj.getString(Constant.TAG_LOGO));
                    responseModel.setWebURl(jObj.getString(Constant.TAG_WEB_URL));

                    if (jObj.has(Constant.TAG_NAME))
                        responseModel.setDiscription(jObj.getString(Constant.TAG_WEB_URL));


                    responseModelList.add(responseModel);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        AppLog.exit(TAG, AppLog.getMethodName());

        return responseModelList;
    }

    public ArrayList<ResponseModel> parseJsonGetCategoryBannerlist(String response, String arrayName) {
        AppLog.enter(TAG, AppLog.getMethodName());

        ArrayList<ResponseModel> responseModelList = null;
        if (null != response && !response.isEmpty()) {
            responseModelList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("category_banners");
                JSONObject jObjct = jsonArray.getJSONObject(0);
                JSONArray jArray = jObjct.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    ResponseModel responseModel = new ResponseModel();

//                    responseModel.setLogo(jObj.getString(Constant.TAG_LOGO));
                    responseModel.setWebURl(jObj.getString("logo"));
                    responseModelList.add(responseModel);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        AppLog.exit(TAG, AppLog.getMethodName());

        return responseModelList;
    }

    public ArrayList<ResponseModel> parseJsonGetTopStorelist(String response) {
        AppLog.enter(TAG, AppLog.getMethodName());

        ArrayList<ResponseModel> responseModelList = null;
        if (null != response && !response.isEmpty()) {
            responseModelList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_TOPSTORE);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    ResponseModel responseModel = new ResponseModel();

                    responseModel.setLogo(jObj.getString(Constant.TAG_LOGO));
                    responseModel.setWebURl(jObj.getString(Constant.TAG_WEB_URL));

                    if (jObj.has(Constant.TAG_NAME))
                        responseModel.setDiscription(jObj.getString(Constant.TAG_WEB_URL));


                    responseModelList.add(responseModel);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        AppLog.exit(TAG, AppLog.getMethodName());

        return responseModelList;
    }

    public void setFragmentName(String fragmentName) {
        TAG_NEXT_FRAGMENT_NAME = fragmentName;
    }

    public String getFragmentName() {
        return TAG_NEXT_FRAGMENT_NAME;
    }

    public void setDetailList(ArrayList<ResponseModel> mDetailList) {

//        LIST_DETAIL=new ArrayList<>();
        LIST_DETAIL = mDetailList;
    }

    public ArrayList<ResponseModel> getDetailList() {

//        LIST_DETAIL=new ArrayList<>();
        return LIST_DETAIL;
    }

    public void setBannerList(ArrayList<ResponseModel> BannerList) {
        this.LIST_BANNER = BannerList;
    }

    public ArrayList<ResponseModel> getBannerList() {

        return this.LIST_BANNER;
    }

    public ArrayList<CountryModel> parseJsonGetCountryList(String response) {
        AppLog.enter(TAG, AppLog.getMethodName());
        ArrayList<CountryModel> mCountryList = null;
        if (null != response && !response.isEmpty()) {
            mCountryList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_COUNTRY_LIST);
                for (int i = 0; i <= jsonArray.length(); i++) {

                    JSONObject jObje = new JSONObject(jsonArray.get(i).toString());
                    String countryName = jObje.getString(Constant.TAG_COUNTRY_NAME);
                    String countryIconURL = jObje.getString(Constant.TAG_COUNTRY_ICON_URL);
                    String countryDefaultLanguage = jObje.getString(Constant.TAG_COUNTRY_DEFAULT_LANGUAGE);
                    CountryModel countryModel = new CountryModel(countryName, countryIconURL, countryDefaultLanguage);
                    mCountryList.add(countryModel);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        AppLog.exit(TAG, AppLog.getMethodName());

        return mCountryList;
    }

    public ArrayList<String> parseJsonGetLanguageList(String response) {
        AppLog.enter(TAG, AppLog.getMethodName());
        ArrayList<String> mCountryList = new ArrayList<>();
        ;
        if (null != response && !response.isEmpty()) {
            mCountryList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("language_list");
                for (int i = 0; i <= jsonArray.length(); i++) {

                    JSONObject jObje = new JSONObject(jsonArray.get(i).toString());
                    String countryName = jObje.getString(Constant.TAG_LANGUAGE);
                    mCountryList.add(countryName);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        AppLog.exit(TAG, AppLog.getMethodName());

        return mCountryList;
    }
//    public ArrayList<HomeTittle> getHomeContentList() {
//        ArrayList<HomeTittle> mArrayList = new ArrayList<>();
//        String[] tittle = new String[]{"Top store", "Top deal", "Electronics", "Men Fashion", "Women Fashion", "Shoes", "Mobilerecharge", "dining"};
//
//        for (int i = 0; i <= 7; i++) {
//            ArrayList<ResponseModel> response = new ArrayList<>();
//            for (int j = 0; j < 4; j++) {
//                ResponseModel responseModel = new ResponseModel();
//                if (j / 2 == 0) {
//                    responseModel.setDiscription("Snapdeal");
//                    responseModel.setWebURl("www.snapdeal.com");
//                    responseModel.setLogo("http://mgsm.semseosmo.com/images/_website_logo_2912161321.png");
//                } else {
//                    responseModel.setDiscription("shopclues");
//                    responseModel.setWebURl("www.shopclues.com");
//                    responseModel.setLogo("http://mgsm.semseosmo.com/images/_website_logo_2912161327.png");
//                }
//                response.add(responseModel);
//            }
//
//            HomeTittle home = new HomeTittle(tittle[i], response);
//            mArrayList.add(home);
//        }
//
//        return mArrayList;
//
//    }

    public ArrayList<HomeTittle> parseHomedata(String responseString) {
        ArrayList<HomeTittle> mArrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("home_list");

            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<ResponseModel> response = new ArrayList<>();
                JSONObject jsoObj = new JSONObject(jsonArray.get(i).toString());

                String tittle = jsoObj.getString("category");
                if (jsoObj.has("data")) {
                    JSONArray jArrayData = jsoObj.getJSONArray("data");
                    if (jArrayData != null && jArrayData.length() > 0) {


                        for (int j = 0; j < jArrayData.length(); j++) {
                            JSONObject jObj = new JSONObject(jArrayData.get(j).toString());

                            ResponseModel responseModel = new ResponseModel();
                            String name = "NA";
                            String url = "NA";
                            String imageurl = "NA";
                            String bookmark = "";
                            boolean isbookmark = false;
                            if (jObj.has("name")) {
                                name = jObj.getString("name");
                            }

                            if (jObj.has("url")) {
                                url = jObj.getString("url");
                            }
                            responseModel.setWebURl(url);
                            if (jObj.has("image_url")) {
                                imageurl = jObj.getString("image_url");
                            }
                            if (jObj.has("url")) {
                                url = jObj.getString("url");
                            }
                            if (jObj.has("bookmark")) {
                                bookmark = jObj.getString("bookmark");
                                if (bookmark.equalsIgnoreCase("yes")) {
                                    isbookmark = true;
                                } else {
                                    isbookmark = false;
                                }
                            }
                            JSONArray jsonArrayCoupon = jObj.getJSONArray("coupon");
                            ArrayList<String> mlist = new ArrayList<>();
                            for (int k = 0; k < jsonArrayCoupon.length(); k++) {
                                mlist.add(jsonArrayCoupon.getString(k));

                            }
                            responseModel.setCouponList(mlist);
                            responseModel.setDiscription(name);
                            responseModel.setWebURl(url);
                            responseModel.setLogo(imageurl);
                            responseModel.setIsBookmarked(isbookmark);
                            response.add(responseModel);
                        }
                    }
                }
                HomeTittle home = new HomeTittle(tittle, response);

                mArrayList.add(home);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set book cab option

        ArrayList<ResponseModel> response = new ArrayList<>();

        ResponseModel responseModel1 = new ResponseModel();
        ResponseModel responseModel2 = new ResponseModel();
        ResponseModel responseModel3 = new ResponseModel();
        responseModel1.setDiscription("Comapre Price");
        responseModel2.setDiscription("Uber");
        responseModel3.setDiscription("Ola");

        response.add(responseModel1);
        response.add(responseModel2);
        response.add(responseModel3);

        mArrayList.add(2, new HomeTittle("Book Cab", response));


        return mArrayList;
    }

    public void showDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                }).show();
    }

    public ArrayList<HomeTittle> parseAllCategoryData(String responseString) {
        ArrayList<HomeTittle> mArrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("all_category");

            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<ResponseModel> response = new ArrayList<>();
                JSONObject jsoObj = new JSONObject(jsonArray.get(i).toString());

                String tittle = jsoObj.getString("name");
                String logo = jsoObj.getString("logo");
                JSONArray jArrayData = jsoObj.getJSONArray("data");

                if (jArrayData != null && jArrayData.length() > 0)


                    for (int j = 0; j < jArrayData.length(); j++) {
                        JSONObject jObj = new JSONObject(jArrayData.get(j).toString());

                        ResponseModel responseModel = new ResponseModel();
                        String name = "NA";
                        String url = "NA";
                        String imageurl = "NA";
                        boolean isbookmark = false;
                        String bookmark = "";
                        if (jObj.has("name")) {
                            name = jObj.getString("name");
                        }

                        if (jObj.has("bookmark")) {
                            bookmark = jObj.getString("bookmark");
                            if (bookmark.equalsIgnoreCase("yes")) {
                                isbookmark = true;
                            } else {
                                isbookmark = false;
                            }
                        }
                        if (jObj.has("url")) {
                            url = jObj.getString("url");
                        }
                        responseModel.setWebURl(url);
                        if (jObj.has("image_url")) {
                            imageurl = jObj.getString("image_url");
                        }
                        JSONArray jsonArrayCoupon = jObj.getJSONArray("coupon");
                        ArrayList<String> mlist = new ArrayList<>();
                        for (int k = 0; k < jsonArrayCoupon.length(); k++) {
                            mlist.add(jsonArrayCoupon.getString(k));

                        }
                        responseModel.setCouponList(mlist);
                        responseModel.setDiscription(name);
                        responseModel.setWebURl(url);
                        responseModel.setLogo(imageurl);
                        responseModel.setIsBookmarked(isbookmark);
                        response.add(responseModel);
                    }
                HomeTittle home = new HomeTittle(tittle, response);
                home.setLogo(logo);
                mArrayList.add(home);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

    public ArrayList<ResponseModel> parseBookmarkdata(String responseString) {
        ArrayList<ResponseModel> mArrayList = null;
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("all_bookmark");
            for (int i = 0; i < jsonArray.length(); i++) {
                mArrayList = new ArrayList<>();
                JSONObject jsoObj = new JSONObject(jsonArray.get(i).toString());
                JSONArray jArrayData = jsoObj.getJSONArray("data");
                if (jArrayData != null && jArrayData.length() > 0)
                    for (int j = 0; j < jArrayData.length(); j++) {
                        JSONObject jObj = new JSONObject(jArrayData.get(j).toString());

                        ResponseModel responseModel = new ResponseModel();
                        String name = "NA";
                        String url = "NA";
                        String imageurl = "NA";
                        boolean isbookmark = true;
                        String bookmark = "";
                        if (jObj.has("name")) {
                            name = jObj.getString("name");
                        }

//                        if (jObj.has("bookmark")) {
//                            bookmark = jObj.getString("bookmark");
//                            if (bookmark.equalsIgnoreCase("yes")) {
//                                isbookmark = true;
//                            } else {
//                                isbookmark = false;
//                            }
//                        }
                        if (jObj.has("url")) {
                            url = jObj.getString("url");
                        }
                        responseModel.setWebURl(url);
                        if (jObj.has("image_url")) {
                            imageurl = jObj.getString("image_url");
                        }
//                        for coupon
                    /*    JSONArray jsonArrayCoupon = jObj.getJSONArray("coupon");
                        ArrayList<String> mlist = new ArrayList<>();
                        for (int k = 0; k < jsonArrayCoupon.length(); k++) {
                            mlist.add(jsonArrayCoupon.getString(k));

                        }
                        responseModel.setCouponList(mlist);
                        */
                        responseModel.setDiscription(name);
                        responseModel.setWebURl(url);
                        responseModel.setLogo(imageurl);
                        responseModel.setIsBookmarked(isbookmark);
                        mArrayList.add(responseModel);
                    }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mArrayList;
    }

    public ArrayList<ResponseModel> getSearchList(String response) {
//        {"search_list":[{"name":"Shopclues","url":"www.shopclues.com","image_url":"http:\/\/mgsm.semseosmo.com\/images\/_website_logo_2912161327.png","bookmark":"No"}]}
        ArrayList<ResponseModel> listSearch = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("search_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ResponseModel responseModel = new ResponseModel();
                String name = "NA";
                String url = "NA";
                String imageurl = "NA";
                boolean isbookmark = false;
                String bookmark = "";
                if (jsonObject1.has("name")) {
                    name = jsonObject1.getString("name");
                }

                if (jsonObject1.has("bookmark")) {
                    bookmark = jsonObject1.getString("bookmark");
                    if (bookmark.equalsIgnoreCase("yes")) {
                        isbookmark = true;
                    } else {
                        isbookmark = false;
                    }
                }
                if (jsonObject1.has("url")) {
                    url = jsonObject1.getString("url");
                }
                responseModel.setWebURl(url);
                if (jsonObject1.has("image_url")) {
                    imageurl = jsonObject1.getString("image_url");
                }
//                        for coupon
                    /*    JSONArray jsonArrayCoupon = jObj.getJSONArray("coupon");
                        ArrayList<String> mlist = new ArrayList<>();
                        for (int k = 0; k < jsonArrayCoupon.length(); k++) {
                            mlist.add(jsonArrayCoupon.getString(k));

                        }
                        responseModel.setCouponList(mlist);
                        */
                responseModel.setDiscription(name);
                responseModel.setWebURl(url);
                responseModel.setLogo(imageurl);
                responseModel.setIsBookmarked(isbookmark);
                listSearch.add(responseModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return listSearch;
    }

    public void showExitAlert(final Activity context) {
        AppLog.enter(TAG, AppLog.getMethodName());
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to close app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }

                })
                .setNegativeButton("No", null)
                .show();
        AppLog.exit(TAG, AppLog.getMethodName());
    }


    public void showAlertDialog(Context mContext, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * this getUniqueID method returns combine string of AndroidId and mobileNo
     *
     * @param context
     * @return androidID+mobileNo
     */
    public String getUniqueID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public void hideKeyBoard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);

            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        } catch (Exception e) {

        }
    }

    public void showSettingsAlert(final Context mContext) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void setDialogWindow(Dialog dialog) {
        AppLog.enter(TAG, AppLog.getMethodName());
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        AppLog.exit(TAG, AppLog.getMethodName());

    }



}
