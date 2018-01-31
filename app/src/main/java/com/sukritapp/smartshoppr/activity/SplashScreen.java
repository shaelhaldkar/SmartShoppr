package com.sukritapp.smartshoppr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;

/**
 * Created by abc on 1/8/2017.
 */

public class SplashScreen extends Activity {

    private final String TAG = AppLog.getClassName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        super.onCreate(savedInstanceState);
        AppLog.exit(TAG, AppLog.getMethodName());

    }

    @Override
    protected void onResume() {
        AppLog.enter(TAG, AppLog.getMethodName());

        super.onResume();


//        startActivity(new Intent(this, LoginScreen.class));
//        finish();

        if (SmartShopprUtils.getInstance().isConnectedToInternet()) {
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        } else {
            SmartShopprUtils.getInstance().showDialog(this,getResources().getString(R.string.no_internet_connection));
//            Intent intent = new Intent(this, NoInternetActivity.class);
//            startActivity(intent);
        }


        AppLog.exit(TAG, AppLog.getMethodName());

    }

    @Override
    protected void onStop() {
        AppLog.enter(TAG, AppLog.getMethodName());

        super.onStop();
        AppLog.exit(TAG, AppLog.getMethodName());

    }

    @Override
    protected void onPause() {
        AppLog.enter(TAG, AppLog.getMethodName());

        super.onPause();
        AppLog.exit(TAG, AppLog.getMethodName());

    }

    @Override
    protected void onDestroy() {
        AppLog.exit(TAG, AppLog.getMethodName());

        super.onDestroy();
        AppLog.exit(TAG, AppLog.getMethodName());

    }
}
