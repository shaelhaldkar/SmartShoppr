package com.sukritapp.smartshoppr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by abc on 4/20/2017.
 */

public class ContactUsFragment extends Fragment implements OnSukritResponseListner {
    private final String TAG = AppLog.getClassName();
    private ProgressDialog progressDialog;
    TextView tvFAQ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_about_us, container, false);
        tvFAQ = (TextView) rootView.findViewById(R.id.tv_aboutus);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.CONTACTUS, null, "http://www.mgsmapi.semseosmo.com/about/aboutList");
        showProgressBar(getActivity());

        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {
        AppLog.enter(TAG, AppLog.getMethodName());
        progressDialog.cancel();
        AppLog.info(TAG, "tag : " + tag);
        AppLog.info(TAG, "tag : " + response);
        String msg = "";
        if (error == null) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray jsonArray = jObject.getJSONArray("about_list");
                JSONObject menu = jsonArray.getJSONObject(0);

                Map<String, String> map = new HashMap<String, String>();
                Iterator iter = menu.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = menu.getString(key);

                    msg = msg + key + " : " + value + "\n";
                    map.put(key, value);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            AppLog.info(TAG, "msg : " + msg);
            tvFAQ.setText(msg);
        } else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
    }
}