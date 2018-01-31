package com.sukritapp.smartshoppr.sosservice;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;


import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by hp on 17-Jul-17.
 */

public class ScreenReceiver extends BroadcastReceiver  {
    private static String TAG = AppLog.getClassName();
    public static boolean isUserPresent = true;
    private int countPowerOff = 0;
    private Context mContext = null;
    long startTime = 0;
    private Location mLocation = null;
    private LocationManager mLocationManager;



    @Override
    public void onReceive(final Context context, final Intent intent) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "-----count intent.getAction()" + intent.getAction());
        this.mContext = context;
        if (countPowerOff > 3) {
            countPowerOff = 0;
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            isUserPresent = false;
            countPowerOff = countPowerOff + 1;
            AppLog.info(TAG, "-----count countPowerOff" + countPowerOff);
            if (startTime == 0 || !isWithinSecond()) {
                Calendar calendar2 = Calendar.getInstance();
                startTime = calendar2.getTimeInMillis();
            }


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            AppLog.info(TAG, "-----count ACTION_SCREEN_ON countPowerOff" + countPowerOff);

            isUserPresent = false;
            countPowerOff = countPowerOff + 1;
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            AppLog.info(TAG, "-----count ACTION_USER_PRESENT countPowerOff" + countPowerOff);
            isUserPresent = true;
            countPowerOff = 0;
            startTime = 0;
        }

        if (!isUserPresent && isWithinSecond() && countPowerOff > 3) {
            Location loc=fetchLoc();
            String latitude="";
            String longtitute="";
            if(loc!=null){
                latitude=""+loc.getLatitude();
                longtitute=""+loc.getLongitude();
            }
//            sendSMS("8802423589", "hello saviour http://maps.google.com/maps?saddr="+loc.getLatitude() +","+loc.getLongitude());
//            sendSMS("9818555384", "hello saviour http://maps.google.com/maps?saddr="+loc.getLatitude() +","+loc.getLongitude());
//            https://www.google.co.in/maps/dir//
            if(LocalDataBase.getInstance().getContactList()!=null && !LocalDataBase.getInstance().getContactList().isEmpty()) {
                for (int i = 0; i < LocalDataBase.getInstance().getContactList().size(); i++) {
                    String contact=     LocalDataBase.getInstance().getContactList().get(i).getContact();
                    if (!contact.isEmpty()) {
                        AppLog.info(TAG, "-----count sendSms sent to : " + LocalDataBase.getInstance().getContactList().get(i).getContact()+" location  "+ latitude + "," + longtitute);

                        sendSMS(contact, "hello  https://maps.google.com/maps/dir//" + latitude + "," + longtitute,latitude,longtitute);
                    }
                }
            }
            countPowerOff = 0;
            startTime = 0;
        }
    }

    private boolean isWithinSecond() {
        Calendar calendar2 = Calendar.getInstance();
        long timedifference = calendar2.getTimeInMillis() - startTime;
        AppLog.info(TAG, "----timedifference" + timedifference);
        if (timedifference > 0 && timedifference <= 1500) { //1.5 second
            return true;
        } else {
            return false;
        }

    }

    private void sendSMS(String phoneNumber, String message, String latitude, String longtitute) {


        //Utility.getInstatnce().sendSMSService(phoneNumber,message,latitude,longtitute,this);
      Intent deliveryIntent=  new Intent(mContext, SmsDeliveredReceiver.class);
        deliveryIntent.putExtra("phoneNumber",phoneNumber);
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
                new Intent(mContext, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
                deliveryIntent, 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

//            volleyMethods.sendSMSinfo(SaviourApp.getApplication(), LocalDataBase.getInstance().getUserId(),message,
//                    phoneNumber, latitude, longtitute,Utility.getInstatnce().getCurrentDateTime(), volleyRequest, this);
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("LOB", "exception while sending message : " + e.toString());
        }
    }


    private Location fetchLoc() {
        if (Build.VERSION.SDK_INT <= 23 ||ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {
                mLocation = mLocationManager.getLastKnownLocation(provider);
                Log.d("last known location, provider: %s, location: %s", provider);

                if (mLocation == null) {
                    continue;
                }
            }
        }
        return mLocation;

    }


}