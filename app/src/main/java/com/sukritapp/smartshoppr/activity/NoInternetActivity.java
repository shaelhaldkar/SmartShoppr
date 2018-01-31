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

public class NoInternetActivity extends Activity {

    private final String TAG = AppLog.getClassName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_no_internet_connection);

    }

    @Override
    protected void onResume() {
        AppLog.enter(TAG, AppLog.getMethodName());

        super.onResume();
        if (SmartShopprUtils.getInstance().isConnectedToInternet()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
