package com.sukritapp.smartshoppr.fragments;

/**
 * Created by abc on 3/9/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.adapter.LanguageAdapter;
import com.sukritapp.smartshoppr.customview.DividerItemDecoration;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.listner.RecycleViewItemClickListner;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopperServiceHelper;
import com.sukritapp.smartshoppr.util.SmartShopprApp;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import java.util.ArrayList;

public class LanguageFragment extends Fragment implements OnSukritResponseListner, RecycleViewItemClickListner {
    private final String TAG = AppLog.getClassName();
    public static String FragName = "";
    private RecyclerView mRecVwLangauage = null;
    private LanguageAdapter mAdapterLanguage = null;
    private TextView mTxtVwSelectedLanguage = null;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        View rootView = inflater.inflate(R.layout.lay_language, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        initView(rootView);
        String country = LocalDataBase.getInstance().getCountry();
        if (country.isEmpty()) {
            SmartShopprUtils.getInstance().showDialog(getActivity(),"Select Country First");
        } else {
            new WebRetriveDataTask().retriveData(getActivity(), this, Constant.listtype.LANGUAGE, null, SmartShopperServiceHelper.URL_LANGUAGE_LIST + country);
            showProgressBar(getActivity());

        }


        AppLog.exit(TAG, AppLog.getMethodName());
        return rootView;
    }

    private void initView(View rootView) {
        mRecVwLangauage = (RecyclerView) rootView.findViewById(R.id.recyVwLanguage);
        mTxtVwSelectedLanguage = (TextView) rootView.findViewById(R.id.txtVw_selected_language);

        mRecVwLangauage.setHasFixedSize(true);
        mRecVwLangauage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.VERTICAL_LIST);
        mRecVwLangauage.addItemDecoration(itemDecoration);

        //set langauage
        setLanguageText();

    }

    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {

        AppLog.enter(TAG, AppLog.getMethodName());
        progressDialog.cancel();
        AppLog.info(TAG, "tag : " + tag);
        AppLog.info(TAG, "tag : " + response);
        if (error == null) {
            ArrayList<String> languageList = SmartShopprUtils.getInstance().parseJsonGetLanguageList(response);
            mAdapterLanguage = new LanguageAdapter(languageList, this, getActivity().getApplicationContext());
            mRecVwLangauage.setAdapter(mAdapterLanguage);
        } else {
            SmartShopprUtils.getInstance().showErrorDialog(getActivity(), error);
        }
//


        AppLog.exit(TAG, AppLog.getMethodName());
    }

    @Override
    public void onItemClick(String language, Drawable drawable, String s) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, "language : " + language);
        LocalDataBase.getInstance().setLanguage(language);
        setLanguageText();
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    private void setLanguageText() {
        mTxtVwSelectedLanguage.setText(LocalDataBase.getInstance().getLangugae());
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

}
