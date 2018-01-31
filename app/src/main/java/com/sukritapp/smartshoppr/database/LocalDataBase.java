package com.sukritapp.smartshoppr.database;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sukritapp.smartshoppr.model.ContactModel;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprApp;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LocalDataBase {
    private static final String TAG = AppLog.getClassName();
    SharedPreferences.Editor editor;

    SharedPreferences prefs;

    private static LocalDataBase localDataBase = null;

    private LocalDataBase() {

    }

    public static LocalDataBase getInstance() {
        AppLog.enter(TAG, AppLog.getMethodName());

        if (localDataBase == null) {
            localDataBase = new LocalDataBase();
            localDataBase.initLocalDataPrefernce();
        }
        AppLog.enter(TAG, AppLog.getMethodName());
        return localDataBase;
    }

    private void initLocalDataPrefernce() {
        prefs = PreferenceManager
                .getDefaultSharedPreferences(SmartShopprApp.getApplication());
        editor = PreferenceManager
                .getDefaultSharedPreferences(SmartShopprApp.getApplication()).edit();
    }
    public void setIsLogin(Boolean country) {
        editor.putBoolean(Constant.KEY_IS_USER_LOGIN, country);
        editor.commit();
    }
    public boolean getIsUSerLogin() {
        return prefs.getBoolean(Constant.KEY_IS_USER_LOGIN, false);
    }

    public void setCountry(String country) {
        editor.putString(Constant.KEY_COUNTRY, country);
        editor.commit();
    }

    public void setLanguage(String language) {
        editor.putString(Constant.KEY_LANGUAGE, language);
        editor.commit();
    }

    public String getCountry() {
        String default_country = SmartShopprApp.getApplication().getResources().getConfiguration().locale.getCountry();

        return prefs.getString(Constant.KEY_COUNTRY, "");
    }

    public String getLangugae() {
        return prefs.getString(Constant.KEY_LANGUAGE, Constant.DEFAULT_LANGUAGE);
    }

    public void setUserDetail(String Name, String Email, String Phone) {
        editor.putString(Constant.KEY_NAME, Name);
        editor.putString(Constant.KEY_EMAIL, Email);
        editor.putString(Constant.KEY_PHONE, Phone);
        editor.commit();
    }

    public String getUserName() {

        return prefs.getString(Constant.KEY_NAME, Constant.DEFAULT_USER_NAME);
    }

    public String getUserEmail() {

        return prefs.getString(Constant.KEY_EMAIL, Constant.DEFAULT_USER_EMAIL);
    }

    public String getUserMobile() {

        return prefs.getString(Constant.KEY_PHONE, Constant.DEFAULT_USER_MOBILE);
    }

    private String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        AppLog.info(TAG, imageEncoded);
        return imageEncoded;
    }

    public void saveCountryImage(Bitmap bmp) {

        editor.putString(Constant.KEY_COUNTRY_IMAGE, encodeToBase64(bmp));
        editor.commit();
    }

    public Bitmap getBitmap() {
        String bitmapString = prefs.getString(Constant.KEY_COUNTRY_IMAGE, "");
        byte[] decodedByte = Base64.decode(bitmapString, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void setSignUp(Boolean skiped) {
        editor.putBoolean(Constant.KEY_ISSKIPPED_SINGUP, skiped);
        editor.commit();
    }

    public boolean isSkippedSignUP() {

        return prefs.getBoolean(Constant.KEY_ISSKIPPED_SINGUP, false);

    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }
    public void saveContactData(ArrayList<ContactModel> arrayList) {
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);
        AppLog.info(TAG, "json string to save is : " + json);
        editor.putString("contactList", json);
        editor.commit();
    }
    public ArrayList<ContactModel> getContactList() {
        ArrayList<ContactModel> arrayList =new ArrayList<>();
        Gson gson = new Gson();
        String json = prefs.getString("contactList", null);
        AppLog.info(TAG, "json string from save is : " + json);
        Type type = new TypeToken<ArrayList<ContactModel>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);
        if(arrayList ==null){
            arrayList = new ArrayList<>();
        }

        return arrayList;
    }
    public void setSOSServiceEnable(boolean b) {
        editor.putBoolean("sosEnable", b);
        editor.commit();
    }

    public void saveOlaAccessToken(String accessToken) {
        editor.putString("olaAccessToken", accessToken);
        editor.commit();
    }
    public String getOlaAccessToken() {
       return prefs.getString("olaAccessToken", "");
    }

    public String getUberRideID() {
        return prefs.getString("rideId", "");
    }
    public LatLng getSrcLat() {
        return new LatLng(Double.parseDouble(prefs.getString("srclat", "")),Double.parseDouble(prefs.getString("srclng", "")));
    }
    public LatLng getDesLat() {
        return new LatLng(Double.parseDouble(prefs.getString("deslat", "")),Double.parseDouble(prefs.getString("deslng", "")));
    }

    public void saveUberRideDetails(String rideId,LatLng srclatLng,LatLng deslatLng) {
        editor.putString("rideId", rideId);
        editor.putString("srclat",""+srclatLng.latitude);
        editor.putString("srclng", ""+srclatLng.longitude);
        editor.putString("deslat",""+deslatLng.latitude);
        editor.putString("deslng", ""+deslatLng.longitude);
        editor.commit();
    }
    public void saveRideId(String rideId){
        editor.putString("rideId", rideId);
        editor.commit();
    }
    public Boolean isSOSServiceEnable() {
        return prefs.getBoolean("sosEnable", false);
    }
}
